package me.darkkir3.mods;

public class ParsableMod 
{
	public String uniqueName;
	public String name;
	public ModPolarity polarity;
	public ModRarity rarity;
	public int baseDrain;
	public int fusionLimit;
	public String description;
	public ModType type;
	public String imageName;
	
	public String getImageURL()
	{
		return "https://cdn.warframestat.us/img/" + imageName;
	}
}
