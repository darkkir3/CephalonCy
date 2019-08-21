package me.darkkir3.health;

import me.darkkir3.status.StatusTypes;

public enum HealthTypes 
{
	FLESH,
	CLONED_FLESH,
	MACHINERY,
	ROBOTIC,
	INFESTED,
	INFESTED_FLESH,
	FOSSILIZED,
	INFESTED_SINEW,
	NONE;
	
	public float getMultiplierAgainst(StatusTypes statusType) 
	{
		float defaultMultiplier = 1f;
		switch(this)
		{
		case FLESH:
			switch(statusType)
			{
				case IMPACT:
					defaultMultiplier -= 0.25f;
					break;
				case SLASH:
					defaultMultiplier += 0.25f;
					break;
				case TOXIN:
					defaultMultiplier += 0.5f;
					break;
				case GAS:
					defaultMultiplier -= 0.25f;
					break;
				case VIRAL:
					defaultMultiplier += 0.5f;
					break;
				default:
					break;
			}
		break;
		case CLONED_FLESH:
			switch(statusType)
			{
				case IMPACT:
					defaultMultiplier -= 0.25f;
					break;
				case SLASH:
					defaultMultiplier += 0.25f;
					break;
				case HEAT:
					defaultMultiplier += 0.25f;
					break;
				case GAS:
					defaultMultiplier -= 0.5f;
					break;
				case VIRAL:
					defaultMultiplier += 0.75f;
					break;
				case VOID:
					defaultMultiplier -= 0.5f;
					break;
				default:
					break;
			}
			break;
		case MACHINERY:
			switch(statusType)
			{
				case IMPACT:
					defaultMultiplier += 0.25f;
					break;
				case ELECTRICITY:
					defaultMultiplier += 0.5f;
					break;
				case TOXIN:
					defaultMultiplier -= 0.25f;
					break;
				case BLAST:
					defaultMultiplier += 0.75f;
					break;
				case VIRAL:
					defaultMultiplier -= 0.25f;
					break;
				case VOID:
					defaultMultiplier -= 0.5f;
					break;
				default:
					break;
			}
			break;
		case ROBOTIC:
			switch(statusType)
			{
				case PUNCTURE:
					defaultMultiplier += 0.25f;
					break;
				case SLASH:
					defaultMultiplier -= 0.25f;
					break;
				case ELECTRICITY:
					defaultMultiplier += 0.5f;
					break;
				case TOXIN:
					defaultMultiplier -= 0.25f;
					break;
				case RADIATION:
					defaultMultiplier += 0.25f;
					break;
				default:
					break;
			}
			break;
		case INFESTED:
			switch(statusType)
			{
				case SLASH:
					defaultMultiplier += 0.25f;
					break;
				case HEAT:
					defaultMultiplier += 0.25f;
					break;
				case GAS:
					defaultMultiplier += 0.75f;
					break;
				case RADIATION:
					defaultMultiplier -= 0.5f;
					break;
				case VIRAL:
					defaultMultiplier -= 0.5f;
					break;
				default:
					break;
			}
			break;
		case INFESTED_FLESH:
			switch(statusType)
			{
				case SLASH:
					defaultMultiplier += 0.5f;
					break;
				case COLD:
					defaultMultiplier -= 0.5f;
					break;
				case HEAT:
					defaultMultiplier += 0.5f;
					break;
				case GAS:
					defaultMultiplier += 0.5f;
					break;
				default:
					break;
			}
			break;
		case FOSSILIZED:
			switch(statusType)
			{
				case SLASH:
					defaultMultiplier += 0.15f;
					break;
				case COLD:
					defaultMultiplier -= 0.25f;
					break;
				case TOXIN:
					defaultMultiplier -= 0.5f;
					break;
				case BLAST:
					defaultMultiplier += 0.5f;
					break;
				case CORROSIVE:
					defaultMultiplier += 0.75f;
					break;
				case RADIATION:
					defaultMultiplier -= 0.75f;
					break;
				case VOID:
					defaultMultiplier -= 0.5f;
				default:
					break;
			}
			break;
		case INFESTED_SINEW:
			switch(statusType)
			{
				case PUNCTURE:
					defaultMultiplier += 0.25f;
					break;
				case COLD:
					defaultMultiplier += 0.25f;
					break;
				case BLAST:
					defaultMultiplier -= 0.5f;
					break;
				case RADIATION:
					defaultMultiplier += 0.5f;
					break;
				default:
					break;
			}
			break;
		default:
			break;
		}
		
		return defaultMultiplier;
	}	
}
