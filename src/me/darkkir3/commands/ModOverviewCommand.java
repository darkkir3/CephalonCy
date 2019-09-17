package me.darkkir3.commands;

import java.io.File;

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
				return false;
			}
			
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
					valuesSet.append("dmg **" + mod.getBaseDamage() + "**\n");
				}
				if(mod.getMultishot() != 0f)
				{
					valuesSet.append("multi **" + mod.getMultishot() + "**\n");
				}
				if(mod.getCriticalChance() != 0f)
				{
					valuesSet.append("cc **" + mod.getCriticalChance() + "**\n");
				}
				if(mod.getCriticalDamage() != 0f)
				{
					valuesSet.append("cd **" + mod.getCriticalDamage() + "**\n");
				}
				if(mod.getFireRate() != 0f)
				{
					valuesSet.append("fr **" + mod.getFireRate() + "**\n");
				}
				if(mod.getMagazine() != 0f)
				{
					valuesSet.append("mag **" + mod.getMagazine() + "**\n");
				}
				if(mod.getReload() != 0f)
				{
					valuesSet.append("reload **" + mod.getReload() + "**\n");
				}
				if(mod.getStatusChance() != 0f)
				{
					valuesSet.append("sc **" + mod.getStatusChance() + "**\n");
				}
				if(mod.getToxin() != 0f)
				{
					valuesSet.append("toxin **" + mod.getToxin() + "**\n");
				}
				if(mod.getElectricity() != 0f)
				{
					valuesSet.append("elec **" + mod.getElectricity() + "**\n");
				}
				if(mod.getCold() != 0f)
				{
					valuesSet.append("cold **" + mod.getCold() + "**\n");
				}
				if(mod.getHeat() != 0f)
				{
					valuesSet.append("heat **" + mod.getHeat() + "**\n");
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
				channel.sendMessage(embed).addFile(polarityFile).queue(t -> 
				{
					if(modConfigured)
					{
						t.addReaction(ConfigReader.readConfigS("checkMark")).queue();
					}
				});
			}
		}
		
		return true;
	}

}
