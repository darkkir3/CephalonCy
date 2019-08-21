package me.darkkir3.status;

import me.darkkir3.cephalonCy.Enemy;
import me.darkkir3.weapons.BaseWeapon;

public abstract class BaseStatusProc 
{
	/**
	 * the time at which we procced this status type
	 */
	protected float timeProcced;
	/*
	 * the duration of this status proc
	 */
	protected float procDuration;
	
	/**the status type that caused this proc
	 * @return
	 */
	public abstract StatusTypes getStatusType();
	
	/**apply this status proc
	 * @param enemy the enemy to proc this on
	 * @param timeProcced the time this proc occured
	 * @param critMultiplier the damage multiplier from triggering crits
	 * @param isHeadshot flag indicating whether the dmg source triggered a headshot multiplier
	 * @param baseWeapon the weapon that triggered the status proc
	 */
	public abstract void applyStatus(Enemy enemy, float timeProcced, float critMultiplier, boolean isHeadshot, BaseWeapon baseWeapon);
	
	public abstract void updateProc(float currentTime);
}
