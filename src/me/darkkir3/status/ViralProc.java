package me.darkkir3.status;

import me.darkkir3.weapons.BaseWeapon;

/**
 *A viral proc halfs the enemy current and max hp in half for 6 seconds. Further procs refresh the duration, but do not affect the effect
 */
public class ViralProc extends StatusProc
{
	@Override
	public float getDuration(BaseWeapon baseWeapon) 
	{
		//6 seconds base duration
		return baseWeapon.getStatusDuration() * 6f;
	}

	@Override
	public void onProcStarted(float currentTime) 
	{
		//if there is no viral effect active already
		if(!this.enemyToProcOn.hasStatusProcOfType(this.getStatusType()))
		{
			this.enemyToProcOn.setHealth(this.enemyToProcOn.getHealth() * 0.5f);
		}
	}

	@Override
	public void onProcUpdated(float currentTime) 
	{
		//Nothing to do here
	}

	@Override
	public void onProcFinished(float currentTime) 
	{
		if(!this.enemyToProcOn.hasStatusProcOfType(this.getStatusType()))
		{
			this.enemyToProcOn.setHealth(this.enemyToProcOn.getHealth() * 2f);
		}
	}

	@Override
	public StatusTypes getStatusType() 
	{
		return StatusTypes.VIRAL;
	}

}
