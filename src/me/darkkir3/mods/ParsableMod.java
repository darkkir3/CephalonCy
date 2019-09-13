package me.darkkir3.mods;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.darkkir3.utils.ConfigReader;

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
	
	public String getMaxRankDescription()
	{
		StringBuilder maxRankDescription = new StringBuilder(this.description);
		
		float maxRankMultiplier = fusionLimit + 1;
		    
		//extract all decimals
		Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
		Matcher matcher = pattern.matcher(maxRankDescription);
		
		int end = 0;
		while (matcher.find(end)) 
		{
			String match = matcher.group();
			double numberFound = Double.valueOf(match);
		    numberFound *= maxRankMultiplier;
		    int i = (int) numberFound;
		    
		    maxRankDescription = maxRankDescription.replace(matcher.start(), matcher.end(), numberFound == i ? String.valueOf(i) : String.valueOf(numberFound));
		    end = matcher.end() + 1;
		 }
		 
		
		return maxRankDescription.toString();
	}
	
	public Color getRarityColor()
	{
		String rarity = this.rarity.name().toLowerCase();
		
		return ConfigReader.readColor(rarity + "ModColor");
	}
}
