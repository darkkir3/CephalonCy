package me.darkkir3.weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.darkkir3.status.StatusTypes;

public class ParsableWeapon 
{
	public String name;
	public Float[] damagePerShot;
	public int magazineSize;
	public float reloadTime;
	public float totalDamage;
	public float damagePerSecond;
	public String trigger;
	public String description;
	public float accuracy;
	public float criticalChance;
	public float criticalMultiplier;
	public float procChance;
	public float fireRate;
	public float disposition;
	public String noise;
	public int masteryReq;
	public String type;
	//https://cdn.warframestat.us/img/${item.imageName}
	public String imageName;
	public Map<String, Object> damageTypes;
	public Map<String, Object> secondary;
	
	public HashMap<StatusTypes, Float> calculateBaseDamageTable()
	{
		HashMap<StatusTypes, Float> damageTable = new HashMap<StatusTypes, Float>();
		
		StatusTypes[] statusTypes = StatusTypes.values();
		
		/*for(int i = 0; i < damagePerShot.length; i++)
		{
			if(damagePerShot[i] > 0)
			{
				damageTable.put(statusTypes[i], damagePerShot[i]);
			}
		}*/
		
		if(damageTypes != null)
		{
			for(StatusTypes statusType : statusTypes)
			{
				String statusName = statusType.name().toLowerCase();
				if(damageTypes.containsKey(statusName))
				{
					damageTable.put(statusType, ((Number)damageTypes.get(statusName)).floatValue());
				}
			}
		}
		
		return damageTable;
	}
	
	public String getImageURL()
	{
		return "https://cdn.warframestat.us/img/" + imageName;
	}
	
	public boolean hasSecondaryStats()
	{
		return this.secondary != null;
	}
	
	public ParsableWeapon displaySecondaryStats()
	{
		if(this.hasSecondaryStats())
		{
			Gson gson = new GsonBuilder().create();
			String text = gson.toJson(this);
			ParsableWeapon copy = gson.fromJson(text, ParsableWeapon.class);
			
			copy.name = this.name + "_secondary";
			copy.fireRate = ((Number)secondary.getOrDefault("speed", this.fireRate)).floatValue();
			copy.criticalChance = ((Number)secondary.getOrDefault("crit_chance", this.criticalChance)).floatValue() / 100f;
			copy.criticalMultiplier = ((Number)secondary.getOrDefault("crit_mult", this.criticalMultiplier)).floatValue();
			copy.procChance = ((Number)secondary.getOrDefault("status_chance", this.procChance)).floatValue();
			copy.trigger = (String) secondary.getOrDefault("shot_type", this.trigger);
			
			ArrayList<Float> damagePerShotCopy = new ArrayList<>();
			StatusTypes[] statusTypes = StatusTypes.values();
			
			for(int i = 0; i < statusTypes.length; i++)
			{
				String statusName = statusTypes[i].name().toLowerCase();
				damagePerShotCopy.add(((Number)secondary.getOrDefault(statusName, 0f)).floatValue());
			}
			
			copy.damagePerShot = damagePerShotCopy.toArray(new Float[0]);
			return copy;
		}
		
		return this;
	}
}
