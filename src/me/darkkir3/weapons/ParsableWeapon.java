package me.darkkir3.weapons;

import java.util.HashMap;

import me.darkkir3.status.StatusTypes;

public class ParsableWeapon 
{
	public String name;
	public float[] damagePerShot;
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
	public String noise;
	public int masteryReq;
	public String type;
	//https://cdn.warframestat.us/img/${item.imageName}
	public String imageName;
	
	public HashMap<StatusTypes, Float> calculateBaseDamageTable()
	{
		HashMap<StatusTypes, Float> damageTable = new HashMap<StatusTypes, Float>();
		
		StatusTypes[] statusTypes = StatusTypes.values();
		
		for(int i = 0; i < damagePerShot.length; i++)
		{
			if(damagePerShot[i] > 0)
			{
				damageTable.put(statusTypes[i], damagePerShot[i]);
			}
		}
		
		return damageTable;
	}
	
	public String getImageURL()
	{
		return "https://cdn.warframestat.us/img/" + imageName;
	}
}
