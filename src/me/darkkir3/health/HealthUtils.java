package me.darkkir3.health;

public final class HealthUtils 
{
	private HealthUtils() {}
	
	/**Calculates the total health an enemy should spawn in
	 * @param baseHealth the base health the enemy should spawn at if their level equals baseLevel
	 * @param baseLevel the initial level an enemy can spawn in
	 * @param currentLevel the modified level of the enemy
	 * @return the total health of the enemy
	 */
	public static float getTotalHealthOf(float baseHealth, float baseLevel, float currentLevel)
	{
		float result = baseHealth;
		result *= 1 + Math.pow(currentLevel - baseLevel, 2f) * 0.015f;
		return result;
	}
}
