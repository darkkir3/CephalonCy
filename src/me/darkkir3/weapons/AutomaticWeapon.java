package me.darkkir3.weapons;

import java.util.HashMap;
import java.util.Map.Entry;

import me.darkkir3.cephalonCy.Enemy;
import me.darkkir3.shield.ShieldTypes;
import me.darkkir3.status.StatusTypes;
import me.darkkir3.utils.DamageUtils;

public class AutomaticWeapon extends BaseWeapon
{
	/**
	 * marks the last time we shot a bullet
	 */
	private float shotMarker;
	private float timeToFinishReload;
	
	/**
	 * our current magazine size
	 */
	private float currentMagazine = 0f;
	/**
	 * delay between shots
	 */
	private float inverseFireRate = 0f;
	
	private HashMap<StatusTypes, Float> statusWeightTable;
	
	/**
	 * treat every damage instance as headshot damage
	 */
	private boolean useHeadshots = false;
	
	public AutomaticWeapon(ParsableWeapon weapon)
	{
		super(weapon);		
		this.resetWeaponState();
	}
	
	public void updateWeaponStats()
	{
		this.shotMarker = 0f;
		this.timeToFinishReload = 0f;
		//delay between shots in seconds
		this.inverseFireRate = 1f / this.fireRate;
		
		this.currentMagazine = this.magazine;
		this.statusWeightTable = DamageUtils.calculateStatusWeighting(this.baseDamage);
	}
	
	@Override
	public void resetWeaponState()
	{
		this.currentMagazine = this.magazine;
		this.updateWeaponStats();
	}
	
	@Override
	public void updateWeaponState(Enemy enemy, float currentTime)
	{
		if(this.timeToFinishReload != 0f)
		{
			if(this.timeToFinishReload > currentTime)
			{
				//we are still reloading the weapon
				return;
			}
			else
			{
				//we just finished reloading
				this.timeToFinishReload = 0f;
				//refill our current magazine
				this.currentMagazine = this.magazine;
			}
		}
		
		//before we fire: is our magazine empty?
		if(this.currentMagazine <= 0f)
		{
			//initiate reload
			timeToFinishReload = currentTime + this.reloadSpeed;
			return;
		}
		
		//shoot a single bullet (without multishot)
		if(currentTime - shotMarker > inverseFireRate)
		{
			this.shotMarker = currentTime;
			this.magazine -= 1f;
			
			int bulletsPerShot = this.multishot;
			float timesToShoot = DamageUtils.getMultishotBullets(this.multishotModifier);
			
			int bulletsToShoot = bulletsPerShot * (int)timesToShoot;
			
			for(int i = 0; i < bulletsToShoot; i++)
			{
				this.fireSingleBullet(currentTime, enemy);
			}
		}
	}

	private void fireSingleBullet(float currentTime, Enemy enemy) 
	{
		float critMultiplier = DamageUtils.getCritMultiplier(this.criticalRate, this.criticalDamage, useHeadshots);
		
		if(DamageUtils.isStatusProcced(this.statusChance))
		{
			StatusTypes statusToUse = DamageUtils.getStatusTypeProcced(this.statusWeightTable);
			enemy.applyStatus(currentTime, statusToUse, critMultiplier, useHeadshots, this);
		}
		
		for(Entry<StatusTypes, Float> entry : this.baseDamage.entrySet())
		{
			float modifiedDamage = DamageUtils.calculateDamageAgainst(enemy, entry.getKey(), entry.getValue(), useHeadshots ? 1f : 0f);
			modifiedDamage *= critMultiplier;
			if(enemy.getShield() > 0f && enemy.getShieldType() != ShieldTypes.NONE)
			{
				if(enemy.getShield() < modifiedDamage)
				{
					//hit the health as well with the remaining damage
					float damageSpilled = 1f - (enemy.getShield() / modifiedDamage);
					enemy.setShield(0f);
					float damageToHealth = DamageUtils.calculateDamageAgainst(enemy, entry.getKey(), entry.getValue() * damageSpilled, useHeadshots ? 1f : 0f);
					damageToHealth *= critMultiplier;
					enemy.setHealth(enemy.getHealth() - damageToHealth);
				}
				else
				{
					enemy.setShield(enemy.getShield() - modifiedDamage);
				}
			}
			else
			{
				enemy.setHealth(enemy.getHealth() - modifiedDamage);
			}
		}
	}

}
