package me.darkkir3.utils;

import java.util.NavigableMap;
import java.util.TreeMap;

import me.darkkir3.cephalonCy.Enemy;
import me.darkkir3.weapons.BaseWeapon;

/**a mapping of the elapsed time to the corresponding health, armor and shield values
 * @author dstrecker
 */
public class EnemyStats 
{
	/**
	 * a navigable map to traverse the "timeline" of stat values in the order of health, shield and armor
	 */
	private NavigableMap<Float, Triplet<Float, Float, Float>> statMap = new TreeMap<>();
	private BaseWeapon weaponToUse;
	private Enemy enemyToUse;
	
	/**Snapshots all shield/health/armor values for a simulation of shooting an enemy
	 * @param baseWeapon the weapon to use
	 * @param enemy the enemy to use
	 */
	public EnemyStats(BaseWeapon baseWeapon, Enemy enemy)
	{
		this.weaponToUse = baseWeapon;
		this.enemyToUse = enemy.cloneEnemy();
		this.calculateStatMap();
	}
	
	/**retrieve enemy stats(health, shield, armor)
	 * @param currentTime the time to use
	 * @return the stats of the enemy at that time in ms (starting from 0)
	 */
	public Triplet<Float, Float, Float> getStatsAt(float currentTime)
	{
		return this.statMap.floorEntry(currentTime).getValue();
	}
	
	/**
	 * @return the time in ms that was needed to kill this enemy
	 */
	public float getTimeToKill()
	{
		return statMap.lastKey();
	}
	
	
	private void calculateStatMap()
	{
		this.weaponToUse.resetWeaponState();
		float currentTime = 0f;
		float timeToElapsePerTick = ConfigReader.readConfigF("timeElapsedPerTick");
		
		while(enemyToUse.isAlive())
		{
			statMap.put(currentTime, Triplet.create(enemyToUse.getHealth(), enemyToUse.getShield(), enemyToUse.getArmor()));
			
			//shoot, applying direct damage and status procs
			this.weaponToUse.updateWeaponState(enemyToUse, currentTime);
			//update the status procs
			enemyToUse.updateEnemy(currentTime);
			
			currentTime += timeToElapsePerTick;
		}
	}
}
