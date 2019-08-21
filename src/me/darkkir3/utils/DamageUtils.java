package me.darkkir3.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import me.darkkir3.armor.ArmorTypes;
import me.darkkir3.cephalonCy.Enemy;
import me.darkkir3.status.StatusTypes;

public final class DamageUtils 
{
	private static final Random rand = new Random();
	
	private DamageUtils() {}
	
	/**Calculates the individual chances for a specific status chance to proc
	 * @param damageByStatus a hashmap mapping status types with their corresponding damage values
	 * @return a hashmap mapping status types with their effective proc rate ranging from 0 to 1
	 */
	public static HashMap<StatusTypes, Float> calculateStatusWeighting(HashMap<StatusTypes, Float> damageByStatus)
	{
		HashMap<StatusTypes, Float> weightingMap = new HashMap<StatusTypes, Float>();
		
		boolean hasElementals = false;
		boolean hasPhysicals = false;
		
		for(Entry<StatusTypes, Float> entry : damageByStatus.entrySet())
		{
			StatusTypes statusType = entry.getKey();
			
			hasElementals |= statusType.isElemental();
			hasPhysicals |= statusType.isPhysical();
		}
		
		
		final float physicalModifier = 
				(hasPhysicals && hasElementals) ? 4f : 1f;
		
		float totalDamage = 0f;
		for(Entry<StatusTypes, Float> entry : damageByStatus.entrySet())
		{
			float damageToAdd = entry.getValue();
			if(entry.getKey().isPhysical())
			{
				damageToAdd *= physicalModifier;
			}
			
			totalDamage += damageToAdd;
			
			weightingMap.put(entry.getKey(), damageToAdd);
		}
		
		for(Entry<StatusTypes, Float> entry : weightingMap.entrySet())
		{
			entry.setValue(entry.getValue() / totalDamage);
		}
		
		return weightingMap;
	}
	
	/**applies the crit multiplier to the specified damage value
	 * @param damage the damage to hit with
	 * @param critChance the crit chance to use (1f = 100%)
	 * @param critDamage the crit multiplier to use
	 * @return
	 */
	public static float getCritMultiplier(float critChance, float critDamage, boolean isHeadshot)
	{
		float result = 1f;
		
		//yellow crit = tier 0, orange crit = tier 1, ...
		float critTier = (float)Math.floor(critChance);
		
		boolean didCrit = rand.nextFloat() <= (critChance % 1f);
		
		//chance to hit the next crit tier
		if(critChance > 1f && didCrit)
		{
			critTier += 1f;
		}
		else if(critChance < 1f && !didCrit)
		{
			//crit chance was below 100% and we did not land a crit
			return result;
		}
		
		//headshots double dip into both base dmg as well as crit
		float critMultiplier = (1f + critTier * ((isHeadshot ? 2f : 1f) * critDamage));
		result *= critMultiplier;
		
		return result;
	}
	
	public static float getMultishotBullets(float multishotModifier)
	{
		boolean nextMultishotTier = rand.nextFloat() <= (multishotModifier % 1f);
		return (float) (nextMultishotTier ? Math.ceil(multishotModifier) : Math.floor(multishotModifier));
	}
	
	public static boolean isStatusProcced(float statusChance)
	{
		return rand.nextFloat() <= statusChance;
	}
	
	public static StatusTypes getStatusTypeProcced(HashMap<StatusTypes, Float> statusWeighting)
	{
		StatusTypes statusTypeToReturn = statusWeighting.size() > 0 ? 
				(StatusTypes)statusWeighting.keySet().toArray()[0] : StatusTypes.TRUE;
		
		float procToTrigger = rand.nextFloat();
		for(Entry<StatusTypes, Float> entry : statusWeighting.entrySet())
		{
			float chanceToProc = entry.getValue();
			if(procToTrigger > chanceToProc)
			{
				procToTrigger -= chanceToProc;
			}
			else
			{
				return entry.getKey();
			}
		}
		
		return statusTypeToReturn;
	}
	
	/** simulates a direct hit and calculates the proper damage to apply without using crit and/or status effects
	 * @param enemy the enemy to hit
	 * @param statusType the status type to use
	 * @param damage the damage to hit with
	 * @param damageModifiers a list of modifiers that change the base dmg (for example critical hits or rhino roar)
	 * @return the damage to apply to the target enemy
	 */
	public static float calculateDamageAgainst(Enemy enemy, StatusTypes statusType, float damage, float... damageModifiers)
	{
		float result = damage;
		
		//we are hitting on armor, apply the corresponding damage reduction
		if(enemy.getShield() <= 0f && enemy.getArmor() > 0f && enemy.getArmorType() != ArmorTypes.NONE)
		{
			float armorModifier = enemy.getMultiplierAgainst(statusType) - 1f;
			float healthModifier = enemy.getHealthType().getMultiplierAgainst(statusType) - 1f;
		
			float armor = enemy.getArmor();
			
			float damageModifier = (1f + healthModifier) * (1f + armorModifier);
			
			//apply other modifiers
			for(float value : damageModifiers)
			{
				damageModifier *= (1f + value);
			}
			
			//reduce by armor
			damageModifier /= 1 + ((armor * (1 - armorModifier)) / 300f);
			result *= damageModifier;
		}
		else
		{
			result *= enemy.getMultiplierAgainst(statusType);
		}
		
		return result;
	}
}
