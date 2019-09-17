package me.darkkir3.commands;

import java.io.File;

import me.darkkir3.cephalonCy.WeaponOverviewGenerator;
import me.darkkir3.mods.ParsableMod;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.MessageUtils;
import me.darkkir3.utils.ObjectParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ModOverviewCommand implements IBotCommand
{
	@Override
	public String getCommandUsage()
	{
		return ConfigReader.readConfigS("commandPrefix") + this.getCommandName() + " [" + ConfigReader.readLangFile("MOD") + "]";
	}
	
	@Override
	public String getCommandDescription() 
	{
		return ConfigReader.readLangFile("MOD_OVERVIEW");
	}

	@Override
	public String getCommandName() 
	{
		return "mod";
	}
	
	@Override
	public String getUserFriendlyCommandName() 
	{
		return ConfigReader.readLangFile("MOD_FRIENDLY");
	}

	@Override
	public boolean handleCommand(MessageListener listener, MessageReceivedEvent event, String userText) 
	{
		if(userText == null)
		{
			return false;
		}
		String textMessage = userText.trim();
		
		if(textMessage.contains(" ") && textMessage.split(" ", 2)[0].equals("set"))
		{
			//split without limit
			String[] splitMessages = userText.split(" ");
			//set [mod-name] [stat] [value] = at least 4 individual strings
			if(splitMessages.length < 4)
			{
				return false;
			}
			
			StringBuilder modName = new StringBuilder();
			for(int i = 0; i < splitMessages.length - 2; i++)
			{
				modName.append(splitMessages[i] + " ");
			}
			
			ParsableMod mod = ObjectParser.fetchMod(modName.substring(0, modName.length() - 1));
			
			String stat = splitMessages[splitMessages.length - 2].toLowerCase();
			String value = splitMessages[splitMessages.length - 1];
			
			float valueToSet = 0f;
			try
			{
				valueToSet = Float.parseFloat(value);
			}
			catch(NumberFormatException e)
			{
				MessageUtils.sendErrorMessage(
						event.getChannel(), 
						ConfigReader.readLangFile("NOT_A_VALID_NUMBER"), 
						null, 
						null);
			}
			
			//percentage value to multiplier
			if(valueToSet != 0f)
			{
				valueToSet /= 100f;
			}
			
			switch(stat)
			{
			case "dmg":
				mod.setBaseDamage(valueToSet);
				break;
			case "multi":
				mod.setMultishot(valueToSet);
				break;
			case "cc":
				mod.setCriticalChance(valueToSet);
				break;
			case "cd":
				mod.setCriticalDamage(valueToSet);
				break;
			case "fr":
				mod.setFireRate(valueToSet);
				break;
			case "mag":
				mod.setMagazine(valueToSet);
				break;
			case "reload":
				mod.setReload(valueToSet);
				break;
			case "sc":
				mod.setStatusChance(valueToSet);
				break;
			case "impact":
				mod.setImpact(valueToSet);
				break;
			case "puncture":
				mod.setPuncture(valueToSet);
				break;
			case "slash":
				mod.setSlash(valueToSet);
				break;
			case "pt":
				mod.setPunchThrough(valueToSet);
				break;
			case "toxin":
				mod.setToxin(valueToSet);
				break;
			case "elec":
				mod.setElectricity(valueToSet);
				break;
			case "cold":
				mod.setCold(valueToSet);
				break;
			case "heat":
				mod.setHeat(valueToSet);
				break;
			default:
				event.getMessage().addReaction(ConfigReader.readConfigS("crossMark")).queue();
				return false;
			}
			
			event.getMessage().addReaction(ConfigReader.readConfigS("checkMark")).queue();
			
			//display mod overview after setting a value
			this.handleCommand(listener, event, mod.name);
		}
		else
		{
			ParsableMod mod = ObjectParser.fetchMod(textMessage);
			
			if(mod != null)
			{			
				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle(mod.name + " (" + mod.type.name().toLowerCase() + ")");
				builder.setDescription(mod.getMaxRankDescription());
				builder.setThumbnail(mod.getImageURL());	
				
				String polarityName = mod.polarity.name().toLowerCase();
				String rarity = mod.rarity.name().toLowerCase();
				
				builder.addField(ConfigReader.readLangFile("MOD_DRAIN"), String.valueOf(mod.baseDrain + mod.fusionLimit), true);
				builder.addField(ConfigReader.readLangFile("MOD_RARITY"), rarity, true);
				
				StringBuilder valuesSet = new StringBuilder();
				if(mod.getBaseDamage() != 0f)
				{
					valuesSet.append("dmg **" + WeaponOverviewGenerator.formatValueToDraw(mod.getBaseDamage() * 100f) + "**\n");
				}
				if(mod.getMultishot() != 0f)
				{
					valuesSet.append("multi **" + WeaponOverviewGenerator.formatValueToDraw(mod.getMultishot() * 100f) + "**\n");
				}
				if(mod.getCriticalChance() != 0f)
				{
					valuesSet.append("cc **" + WeaponOverviewGenerator.formatValueToDraw(mod.getCriticalChance() * 100f) + "**\n");
				}
				if(mod.getCriticalDamage() != 0f)
				{
					valuesSet.append("cd **" + WeaponOverviewGenerator.formatValueToDraw(mod.getCriticalDamage() * 100f) + "**\n");
				}
				if(mod.getFireRate() != 0f)
				{
					valuesSet.append("fr **" + WeaponOverviewGenerator.formatValueToDraw(mod.getFireRate() * 100f) + "**\n");
				}
				if(mod.getMagazine() != 0f)
				{
					valuesSet.append("mag **" + WeaponOverviewGenerator.formatValueToDraw(mod.getMagazine() * 100f) + "**\n");
				}
				if(mod.getReload() != 0f)
				{
					valuesSet.append("reload **" + WeaponOverviewGenerator.formatValueToDraw(mod.getReload() * 100f) + "**\n");
				}
				if(mod.getStatusChance() != 0f)
				{
					valuesSet.append("sc **" + WeaponOverviewGenerator.formatValueToDraw(mod.getStatusChance() * 100f) + "**\n");
				}
				if(mod.getImpact() != 0f)
				{
					valuesSet.append("impact **" + WeaponOverviewGenerator.formatValueToDraw(mod.getImpact() * 100f) + "**\n");
				}
				if(mod.getPuncture() != 0f)
				{
					valuesSet.append("puncture **" + WeaponOverviewGenerator.formatValueToDraw(mod.getPuncture() * 100f) + "**\n");
				}
				if(mod.getSlash() != 0f)
				{
					valuesSet.append("slash **" + WeaponOverviewGenerator.formatValueToDraw(mod.getSlash() * 100f) + "**\n");
				}
				if(mod.getPunchThrough() != 0f)
				{
					valuesSet.append("pt **" + WeaponOverviewGenerator.formatValueToDraw(mod.getPunchThrough() * 100f) + "**\n");
				}
				if(mod.getToxin() != 0f)
				{
					valuesSet.append("toxin **" + WeaponOverviewGenerator.formatValueToDraw(mod.getToxin() * 100f) + "**\n");
				}
				if(mod.getElectricity() != 0f)
				{
					valuesSet.append("elec **" + WeaponOverviewGenerator.formatValueToDraw(mod.getElectricity() * 100f) + "**\n");
				}
				if(mod.getCold() != 0f)
				{
					valuesSet.append("cold **" + WeaponOverviewGenerator.formatValueToDraw(mod.getCold() * 100f) + "**\n");
				}
				if(mod.getHeat() != 0f)
				{
					valuesSet.append("heat **" + WeaponOverviewGenerator.formatValueToDraw(mod.getHeat() * 100f) + "**\n");
				}
				
				boolean modConfigured = valuesSet.length() > 0;
				
				if(modConfigured)
				{
					builder.addField(ConfigReader.readLangFile("MOD_STATS"), valuesSet.toString(), true);
				}
				
				MessageChannel channel = event.getChannel();
				
				File polarityFile = new File("data" + File.separator + "icons" + File.separator + polarityName + "_pol.png");
				builder.setAuthor(polarityName.substring(0, 1).toUpperCase() + polarityName.substring(1), null, "attachment://" + polarityName + "_pol.png");
				MessageEmbed embed = builder.build();
				channel.sendMessage(embed).addFile(polarityFile).queue();
			}
		}
		
		return true;
	}

}
