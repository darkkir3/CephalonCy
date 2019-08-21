package me.darkkir3.shield;

import me.darkkir3.status.StatusTypes;

public enum ShieldTypes 
{
	SHIELD,
	PROTO_SHIELD,
	NONE;
	
	public float getMultiplierAgainst(StatusTypes statusType) 
	{
		float defaultMultiplier = 1f;
		if(this == SHIELD)
		{
			switch(statusType)
			{
				case IMPACT:
					defaultMultiplier += 0.5f;
					break;
				case PUNCTURE:
					defaultMultiplier -= 0.2f;
					break;
				case COLD:
					defaultMultiplier += 0.5f;
					break;
				case MAGNETIC:
					defaultMultiplier += 0.75f;
					break;
				case RADIATION:
					defaultMultiplier -= 0.25f;
				default:
					break;
			}
		}
		else if(this == PROTO_SHIELD)
		{
			switch(statusType)
			{
				case IMPACT:
					defaultMultiplier += 0.15f;
					break;
				case PUNCTURE:
					defaultMultiplier -= 0.5f;
					break;
				case HEAT:
					defaultMultiplier -= 0.5f;
					break;
				case TOXIN:
					defaultMultiplier += 0.25f;
					break;
				case CORROSIVE:
					defaultMultiplier -= 0.5f;
					break;
				case MAGNETIC:
					defaultMultiplier += 0.75f;
				default:
					break;
			}
		}
		
		return defaultMultiplier;
	}
}
