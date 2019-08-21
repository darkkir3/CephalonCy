package me.darkkir3.status;

import me.darkkir3.cephalonCy.Enemy;
import me.darkkir3.weapons.BaseWeapon;

public abstract class StatusProc extends BaseStatusProc
{
	protected Enemy enemyToProcOn;
	protected float critMultiplier;
	protected boolean isHeadshot;
	protected BaseWeapon baseWeapon;
	
	@Override
	public void applyStatus(Enemy enemy, float timeProcced, float critMultiplier, boolean isHeadshot, BaseWeapon baseWeapon) 
	{
		this.timeProcced = timeProcced;
		this.procDuration = this.getDuration(baseWeapon);
		this.enemyToProcOn = enemy;
		this.critMultiplier = critMultiplier;
		this.isHeadshot = isHeadshot;
		this.baseWeapon = baseWeapon;
		
		this.onProcStarted(timeProcced);
		this.enemyToProcOn.addStatusProc(this);
	}

	@Override
	public void updateProc(float currentTime) 
	{
		this.onProcUpdated(currentTime);
		
		if(this.timeProcced + this.procDuration > currentTime)
		{
			this.enemyToProcOn.removeStatusProc(this);
			this.onProcFinished(currentTime);
		}
	}
	
	public abstract float getDuration(BaseWeapon baseWeapon);
	
	/**Called before the proc has been added
	 * @param currentTime
	 */
	public abstract void onProcStarted(float currentTime);
	/**Called during every update tick of the enemy this was applied to
	 * @param currentTime
	 */
	public abstract void onProcUpdated(float currentTime);
	/**Called after the proc already has been removed
	 * @param currentTime
	 */
	public abstract void onProcFinished(float currentTime);

}
