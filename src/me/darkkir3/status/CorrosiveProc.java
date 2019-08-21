package me.darkkir3.status;

import me.darkkir3.armor.ArmorUtils;
import me.darkkir3.weapons.BaseWeapon;

public class CorrosiveProc extends StatusProc
{

	@Override
	public float getDuration(BaseWeapon baseWeapon) 
	{
		//Corrosive procs are permanent
		return 0f;
	}

	@Override
	public void onProcStarted(float currentTime) 
	{
		enemyToProcOn.setArmor(ArmorUtils.applyCorrosiveProc(enemyToProcOn.getArmor()));
	}

	@Override
	public void onProcUpdated(float currentTime) 
	{
		//Nothing to do here
	}

	@Override
	public void onProcFinished(float currentTime) 
	{
		//Nothing to do here
	}

	@Override
	public StatusTypes getStatusType() 
	{
		return StatusTypes.CORROSIVE;
	}

}
