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
	
	private String getCriticalChanceKey()
	{
		return "mod." + this.uniqueName + ".criticalChance";
	}
	
	private String getCriticalDamageKey()
	{
		return "mod." + this.uniqueName + ".criticalDamage";
	}
	
	private String getBaseDamageKey()
	{
		return "mod." + this.uniqueName + ".baseDamage";
	}
	
	private String getMultishotKey()
	{
		return "mod." + this.uniqueName + ".multishot";
	}
	
	private String getStatusChanceKey()
	{
		return "mod." + this.uniqueName + ".statusChance";
	}
	
	private String getFireRateKey()
	{
		return "mod." + this.uniqueName + ".fireRate";
	}
	
	public String getElectricityKey()
	{
		return "mod." + this.uniqueName + ".electricity";
	}
	
	public String getToxinKey()
	{
		return "mod." + this.uniqueName + ".toxin";
	}
	
	public String getColdKey()
	{
		return "mod." + this.uniqueName + ".cold";
	}
	
	public String getHeatKey()
	{
		return "mod." + this.uniqueName + ".heat";
	}
	
	public String getMagazineKey()
	{
		return "mod." + this.uniqueName + ".magazine";
	}
	
	public String getReloadKey()
	{
		return "mod." + this.uniqueName + ".reload";
	}
	
	public String getImpactKey()
	{
		return "mod." + this.uniqueName + ".impact";
	}
	
	public String getPunctureKey()
	{
		return "mod." + this.uniqueName + ".puncture";
	}
	
	public String getSlashKey()
	{
		return "mod." + this.uniqueName + ".slash";
	}
	
	public String getPunchThroughKey()
	{
		return "mod." + this.uniqueName + ".punchThrough";
	}
	
	public float getCriticalChance()
	{
		return ConfigReader.readConfigF(this.getCriticalChanceKey());
	}
	
	public float getCriticalDamage()
	{
		return ConfigReader.readConfigF(this.getCriticalDamageKey());
	}
	
	public float getBaseDamage()
	{
		return ConfigReader.readConfigF(this.getBaseDamageKey());
	}
	
	public float getMultishot()
	{
		return ConfigReader.readConfigF(this.getMultishotKey());
	}
	
	public float getStatusChance()
	{
		return ConfigReader.readConfigF(this.getStatusChanceKey());
	}
	
	public float getFireRate()
	{
		return ConfigReader.readConfigF(this.getFireRateKey());
	}
	
	public float getElectricity()
	{
		return ConfigReader.readConfigF(this.getElectricityKey());
	}
	
	public float getToxin()
	{
		return ConfigReader.readConfigF(this.getToxinKey());
	}
	
	public float getCold()
	{
		return ConfigReader.readConfigF(this.getColdKey());
	}
	
	public float getHeat()
	{
		return ConfigReader.readConfigF(this.getHeatKey());
	}
	
	public float getMagazine()
	{
		return ConfigReader.readConfigF(this.getMagazineKey());
	}
	
	public float getReload()
	{
		return ConfigReader.readConfigF(this.getReloadKey());
	}
	
	public float getImpact()
	{
		return ConfigReader.readConfigF(this.getImpactKey());
	}
	
	public float getPuncture()
	{
		return ConfigReader.readConfigF(this.getPunctureKey());
	}
	
	public float getSlash()
	{
		return ConfigReader.readConfigF(this.getSlashKey());
	}
	
	public float getPunchThrough()
	{
		return ConfigReader.readConfigF(this.getPunchThroughKey());
	}
	
	public void setCriticalChance(float value)
	{
		ConfigReader.setConfig(this.getCriticalChanceKey(), String.valueOf(value));
	}
	
	public void setCriticalDamage(float value)
	{
		ConfigReader.setConfig(this.getCriticalDamageKey(), String.valueOf(value));
	}
	
	public void setBaseDamage(float value)
	{
		ConfigReader.setConfig(this.getBaseDamageKey(), String.valueOf(value));
	}
	
	public void setMultishot(float value)
	{
		ConfigReader.setConfig(this.getMultishotKey(), String.valueOf(value));
	}
	
	public void setStatusChance(float value)
	{
		ConfigReader.setConfig(this.getStatusChanceKey(), String.valueOf(value));
	}
	
	public void setFireRate(float value)
	{
		ConfigReader.setConfig(this.getFireRateKey(), String.valueOf(value));
	}
	
	public void setImpact(float value)
	{
		ConfigReader.setConfig(this.getImpactKey(), String.valueOf(value));
	}
	
	public void setPuncture(float value)
	{
		ConfigReader.setConfig(this.getPunctureKey(), String.valueOf(value));
	}
	
	public void setSlash(float value)
	{
		ConfigReader.setConfig(this.getSlashKey(), String.valueOf(value));
	}
	
	public void setPunchThrough(float value)
	{
		ConfigReader.setConfig(this.getPunchThroughKey(), String.valueOf(value));
	}
	
	public void setElectricity(float value)
	{
		ConfigReader.setConfig(this.getElectricityKey(), String.valueOf(value));
	}
	
	public void setToxin(float value)
	{
		ConfigReader.setConfig(this.getToxinKey(), String.valueOf(value));
	}
	
	public void setCold(float value)
	{
		ConfigReader.setConfig(this.getColdKey(), String.valueOf(value));
	}
	
	public void setHeat(float value)
	{
		ConfigReader.setConfig(this.getHeatKey(), String.valueOf(value));
	}
	
	public void setMagazine(float value)
	{
		ConfigReader.setConfig(this.getMagazineKey(), String.valueOf(value));
	}
	
	public void setReload(float value)
	{
		ConfigReader.setConfig(this.getReloadKey(), String.valueOf(value));
	}
}

