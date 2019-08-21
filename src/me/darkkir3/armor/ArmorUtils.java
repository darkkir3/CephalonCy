package me.darkkir3.armor;

public final class ArmorUtils 
{
	private ArmorUtils() {}
	
	/**Calculates the total armor an armored enemy should spawn in
	 * @param baseArmor the base armor the enemy should spawn at if their level equals baseLevel
	 * @param baseLevel the initial level an enemy can spawn in
	 * @param currentLevel the modified level of the enemy
	 * @return the total armor of the enemy
	 */
	public static float getTotalArmorOf(float baseArmor, float baseLevel, float currentLevel)
	{
		float result = baseArmor;
		result *= 1 + (Math.pow(currentLevel - baseLevel, 1.75f) / 200f);
		return result;
	}
	
	/**Simulates a corrosive proc, removing 25% of the current armor value
	 * @param currentArmor the current armor value
	 * @return the reduced armor amount
	 */
	public static float applyCorrosiveProc(float currentArmor)
	{
		currentArmor *= 0.75f;
		if(currentArmor < 1f)
		{
			currentArmor = 0f;
		}
		
		return currentArmor;
	}
	
	/**Calculates the damage reduction of armor without using status type modifiers
	 * @param currentArmor the armor value to use
	 * @return the dmg reduction multiplier
	 */
	public static float getDamageReduction(float currentArmor)
	{
		return currentArmor / (currentArmor + 300f);
	}
	
	/** Calculates the modified health based on armor values
	 * @param currentHealth the health value to use
	 * @param currentArmor the armor value to use
	 * @return the effective health
	 */
	public static float getEffectiveHealth(float currentHealth, float currentArmor)
	{
		return currentHealth / (1f - getDamageReduction(currentArmor));
	}
}
