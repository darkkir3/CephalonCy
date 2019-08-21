package me.darkkir3.status;

public enum StatusTypes 
{
	IMPACT,
	SLASH,
	PUNCTURE,
	HEAT,
	COLD,
	ELECTRICITY,
	TOXIN,
	VIRAL,
	CORROSIVE,
	RADIATION,
	BLAST,
	MAGNETIC,
	GAS,
	VOID,
	TRUE;
	
	/**IPS
	 * @return
	 */
	public boolean isPhysical()
	{
		return this == IMPACT || this == PUNCTURE || this == SLASH;
	}
	
	/**Anything that is not physical or void or finisher dmg is considered elemental
	 * @return
	 */
	public boolean isElemental()
	{
		return this != VOID && this != TRUE && !this.isPhysical();
	}
}
