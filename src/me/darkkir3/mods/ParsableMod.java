package me.darkkir3.mods;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.StringUtils;

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
	
	public String getMaxRankDescription(float umbraMultiplier)
	{
		String modifiedDescription = this.description;
		//The description of hunter munitions only contains tags for slash instead of the actual word slash
		if("Hunter Munitions".equals(this.name))
		{
			modifiedDescription = modifiedDescription.replace("<DT_SLASH>", "Slash");
		}
		
		//clear everything in brackets (aka "(x2 for heavy attacks) or (x2 for bows)"
		String result = StringUtils.multiplyEffectNumbers(
				//The description of hunter munitions only contains tags for slash
				modifiedDescription
				.replaceAll("([\\\\(<][^)]*[\\\\)>])+", ""), 
				fusionLimit + 1);
		if(this.polarity == ModPolarity.UMBRA)
		{
			 result = StringUtils.multiplyEffectNumbers(result, umbraMultiplier);
		}
		
		return result;
	}
	
	public ModType getModType()
	{
		return this.type;
	}
	
	public int getTotalDrain()
	{
		return this.baseDrain + this.fusionLimit;
	}
	
	public ArrayList<String> getModEffects(float umbraMultiplier)
	{
		String maxRankEffect = this.getMaxRankDescription(umbraMultiplier);
		ArrayList<String> separateEffects = new ArrayList<String>(Arrays.asList(maxRankEffect.split(",")));
		return separateEffects;
	}
	
	/**Merge the values of all passed mods with the current instance and return every single effect as separate string
	 * @param mods the mods to merge with
	 * @return single mod effects
	 */
	public ArrayList<String> mergeModEffects(ArrayList<ParsableMod> mods)
	{
		int umbraModCount = (int) mods.stream().filter(T -> T.polarity == ModPolarity.UMBRA).count();
		//umbra mod bonus increases all values by an additional 0.25 for each umbra mod (1 -> 1.25 -> 1.75 -> 2.5...)
		float umbraMultiplier = 1f;
		if(umbraModCount > 1)
		{
			int i = umbraModCount - 1;
			float currentMultiplier = 0f;
			while(i > 0)
			{
				currentMultiplier += 0.25f;
				umbraMultiplier += currentMultiplier;
				i--;
			}
		}
		
		ArrayList<String> effectsAvailable = new ArrayList<String>();
		effectsAvailable.addAll(this.getModEffects(umbraMultiplier));
		
		for(ParsableMod mod : mods)
		{
			if(mod == null || mod == this)
			{
				continue;
			}
			
			ArrayList<String> effectsToMerge = mod.getModEffects(umbraMultiplier);
			
			for(String value : effectsToMerge)
			{
				//Strip the percentage values of the effect in order to check for similar values
				String effect = value.replaceAll("([ +-]?)\\d+(?:\\.\\d+)?%?( ?)", "");
				boolean merged = false;
				
				for(int i = 0; i < effectsAvailable.size(); i++)
				{
					String existingEffect = effectsAvailable.get(i).replaceAll("([ +-]?)\\d+(?:\\.\\d+)?%?( ?)", "");
					//we found an effect that seems to match the current mod
					if(existingEffect.equalsIgnoreCase(effect))
					{
						ArrayList<Double> existingNumbers = StringUtils.extractNumbers(effectsAvailable.get(i));
						if(existingNumbers != null && !existingNumbers.isEmpty())
						{
							String newEffect = StringUtils.adjustEffectNumbers(value, existingNumbers);
							effectsAvailable.set(i, newEffect);
							merged = true;
						}
					}
				}
				
				if(!merged)
				{
					effectsAvailable.add(value);
				}
			}
		}
		
		return effectsAvailable;
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

