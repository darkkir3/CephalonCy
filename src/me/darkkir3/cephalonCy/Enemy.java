package me.darkkir3.cephalonCy;

import java.util.ArrayList;

import me.darkkir3.armor.ArmorTypes;
import me.darkkir3.armor.ArmorUtils;
import me.darkkir3.health.HealthTypes;
import me.darkkir3.health.HealthUtils;
import me.darkkir3.shield.ShieldTypes;
import me.darkkir3.shield.ShieldUtils;
import me.darkkir3.status.BaseStatusProc;
import me.darkkir3.status.StatusManager;
import me.darkkir3.status.StatusTypes;
import me.darkkir3.weapons.BaseWeapon;

public class Enemy 
{
	private float health, shield, armor;
	
	private HealthTypes healthType = HealthTypes.NONE;
	private ArmorTypes armorType = ArmorTypes.NONE;
	private ShieldTypes shieldType = ShieldTypes.NONE;
	
	private ArrayList<BaseStatusProc> statusProcs;
	
	public Enemy(float baseHealth, float baseShield, float baseArmor)
	{
		this.health = baseHealth;
		this.shield = baseShield;
		this.armor = baseArmor;
		
		this.statusProcs = new ArrayList<>();
	}
	
	public void applyScaling(float baseLevel, float currentLevel)
	{
		if(this.armor > 0f)
		{
			this.armor = ArmorUtils.getTotalArmorOf(this.armor, baseLevel, currentLevel);
		}
		
		if(this.shield > 0f)
		{
			this.shield = ShieldUtils.getTotalShieldOf(this.shield, baseLevel, currentLevel);
		}
		
		if(this.health > 0f)
		{
			this.health = HealthUtils.getTotalHealthOf(this.health, baseLevel, currentLevel);
		}
	}
	
	public float getHealth()
	{
		return this.health;
	}
	
	public float getShield()
	{
		return this.shield;
	}
	
	public float getArmor()
	{
		return this.armor;
	}
	
	public void setHealth(float health)
	{
		this.health = health;
	}
	
	public void setShield(float shield)
	{
		this.shield = shield;
	}
	
	public void setArmor(float armor)
	{
		this.armor = armor;
	}
	
	public HealthTypes getHealthType() 
	{
		return healthType;
	}

	public void setHealthType(HealthTypes healthType) 
	{
		this.healthType = healthType;
	}

	public ArmorTypes getArmorType() 
	{
		return armorType;
	}

	public void setArmorType(ArmorTypes armorType) 
	{
		this.armorType = armorType;
	}

	public ShieldTypes getShieldType() 
	{
		return shieldType;
	}

	public void setShieldType(ShieldTypes shieldType) 
	{
		this.shieldType = shieldType;
	}
	
	public boolean isAlive()
	{
		return this.health > 0f;
	}
	
	public float getMultiplierAgainst(StatusTypes statusType)
	{
		float statusTypeMultiplier = 1f;
		if(this.shield > 0f && this.shieldType != ShieldTypes.NONE)
		{
			statusTypeMultiplier = this.shieldType.getMultiplierAgainst(statusType);
		}
		else if(this.armor > 0f && this.armorType != ArmorTypes.NONE)
		{
			statusTypeMultiplier = this.armorType.getMultiplierAgainst(statusType);
		}
		else if(this.health > 0f && this.healthType != HealthTypes.NONE)
		{
			statusTypeMultiplier = this.healthType.getMultiplierAgainst(statusType);
		}
		
		return statusTypeMultiplier;
	}
	
	public void addStatusProc(BaseStatusProc statusProc)
	{
		this.statusProcs.add(statusProc);
	}
	
	public void removeStatusProc(BaseStatusProc statusProc)
	{
		this.statusProcs.remove(statusProc);
	}
	
	public boolean hasStatusProcOfType(StatusTypes statusType)
	{
		return this.statusProcs.stream().filter(
				T -> {return T.getStatusType() == statusType;}).findFirst().isPresent();
	}
	
	public void applyStatus(float timeProcced, StatusTypes statusType, float critMultiplier, boolean isHeadshot, BaseWeapon baseWeapon)
	{
		BaseStatusProc statusProc = StatusManager.getStatusProcForStatusType(statusType);
		if(statusProc != null)
		{
			statusProc.applyStatus(this, timeProcced, critMultiplier, isHeadshot, baseWeapon);
		}
	}
	
	public void updateEnemy(float currentTime)
	{
		//TODO: Call this on each "global" tick
		this.statusProcs.forEach(T -> {T.updateProc(currentTime);});
	}
	
	public Enemy cloneEnemy()
	{
		Enemy cloned = new Enemy(this.getHealth(), this.getShield(), this.getArmor());
		cloned.healthType = this.healthType;
		cloned.shieldType = this.shieldType;
		cloned.armorType = this.armorType;
		return cloned;
	}
}
