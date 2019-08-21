package me.darkkir3.weapons;

import java.util.HashMap;

import me.darkkir3.cephalonCy.Enemy;
import me.darkkir3.status.StatusTypes;

public abstract class BaseWeapon 
{
	public BaseWeapon(ParsableWeapon weapon) 
	{
		this.accuracy = weapon.accuracy;
		this.reloadSpeed = weapon.reloadTime;
		this.magazine = weapon.magazineSize;
		this.chargeAttack = 0f;
		this.criticalRate = weapon.criticalChance * 100f;
		this.criticalDamage = weapon.criticalMultiplier;
		this.statusChance = weapon.procChance * 100f;
		this.fireRate  = weapon.fireRate;
		this.statusDuration = 1f;
		this.multishot = 1;
		this.multishotModifier = 1f;
		this.baseDamage = weapon.calculateBaseDamageTable();
	}
	
	/**
	 * map status types with their respective base dmg value
	 */
	protected HashMap<StatusTypes, Float> baseDamage;
	protected WeaponSlot weaponSlot = WeaponSlot.PRIMARY;
	/**
	 * 100 accuracy = pinpoint; everything lower = more spread
	 */
	protected float accuracy;
	/**
	 * time in seconds needed to reload
	 */
	protected float reloadSpeed;
	/**
	 * amount of shots in magazine
	 */
	protected float magazine;
	/**
	 * How quickly a weapon can be charged
	 */
	protected float chargeAttack;
	/**
	 * crit chance (1 = 100%)
	 */
	protected float criticalRate;
	/**
	 * the listed damage multiplier for critical hits (as shown in the modding menu)
	 */
	protected float criticalDamage;
	/**
	 * status chance (1 = 100%)
	 */
	protected float statusChance;
	/**
	 * fire rate (shots per seconds; also influences charge rate)
	 */
	protected float fireRate;
	
	/**
	 * status duration multiplier
	 */
	protected float statusDuration;
	
	/**
	 * innate multishot on the weapon (1 = fires a single bullet)
	 */
	protected int multishot = 1;
	
	/**
	 * the modded multishot modifier as it appears in the modding stats (1f = fires base bullets)
	 */
	protected float multishotModifier = 1f;
	
	public abstract void resetWeaponState();
	public abstract void updateWeaponState(Enemy enemy, float currentTime);
	
	/**status duration multiplier
	 * @return
	 */
	public float getStatusDuration()
	{
		return this.statusDuration;
	}
	
	public float getDamageForStatusType(StatusTypes statusType)
	{
		return this.baseDamage.get(statusType);
	}
}
