package me.darkkir3.armor;

import me.darkkir3.status.StatusTypes;

public enum ArmorTypes 
{
	FERRITE,
	ALLOY,
	NONE;
	
	public float getMultiplierAgainst(StatusTypes statusType) 
	{
		float defaultMultiplier = 1f;
		if(this == ALLOY)
		{
			switch(statusType)
			{
				case PUNCTURE:
					defaultMultiplier += 0.15f;
					break;
				case SLASH:
					defaultMultiplier -= 0.5f;
					break;
				case COLD:
					defaultMultiplier += 0.25f;
					break;
				case ELECTRICITY:
					defaultMultiplier -= 0.5f;
					break;
				case MAGNETIC:
					defaultMultiplier -= 0.5f;
					break;
				case RADIATION:
					defaultMultiplier += 0.75f;
				default:
					break;
			}
		}
		else if(this == FERRITE)
		{
			switch(statusType)
			{
				case PUNCTURE:
					defaultMultiplier += 0.5f;
					break;
				case SLASH:
					defaultMultiplier -= 0.15f;
					break;
				case TOXIN:
					defaultMultiplier += 0.25f;
					break;
				case BLAST:
					defaultMultiplier -= 0.25f;
					break;
				case CORROSIVE:
					defaultMultiplier += 0.75f;
					break;
				default:
					break;
			}
		}
		
		return defaultMultiplier;
	}
}
