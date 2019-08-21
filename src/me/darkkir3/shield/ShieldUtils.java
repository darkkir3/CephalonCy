package me.darkkir3.shield;

public final class ShieldUtils 
{
	private ShieldUtils() {}
	
	/**Calculates the total shield an shielded enemy should spawn in
	 * @param baseShield the base shield the enemy should spawn at if their level equals baseLevel
	 * @param baseLevel the initial level an enemy can spawn in
	 * @param currentLevel the modified level of the enemy
	 * @return the total shield of the enemy
	 */
	public static float getTotalShieldOf(float baseShield, float baseLevel, float currentLevel)
	{
		float result = baseShield;
		result += Math.pow(currentLevel - baseLevel, 2f) * 0.0075f * baseShield;
		return result;
	}
}
