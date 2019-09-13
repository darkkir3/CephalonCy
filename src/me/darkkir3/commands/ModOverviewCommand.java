package me.darkkir3.commands;

import java.io.File;

import me.darkkir3.mods.ParsableMod;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.ObjectParser;
import net.dv8tion.jda.api.EmbedBuilder;
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
		String textMessage = userText;
		if(userText == null)
		{
			return false;
		}
		textMessage = textMessage.substring(1);
		
		ParsableMod mod = ObjectParser.fetchMod(textMessage);
		
		if(mod != null)
		{			
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle(mod.name + " (" + mod.type.name().toLowerCase() + ")");
			builder.setDescription(mod.getMaxRankDescription());
			builder.setThumbnail(mod.getImageURL());	
			
			String polarityName = mod.polarity.name().toLowerCase();
			String rarity = mod.rarity.name().toLowerCase();
			
			builder.setColor(ConfigReader.readColor(rarity + "ModColor"));
			builder.addField(ConfigReader.readLangFile("MOD_DRAIN"), String.valueOf(mod.baseDrain + mod.fusionLimit), true);
			builder.addField(ConfigReader.readLangFile("MOD_RARITY"), rarity, true);
			
			File polarityFile = new File("data" + File.separator + "icons" + File.separator + polarityName + "_pol.png");
			builder.setAuthor(polarityName.substring(0, 1).toUpperCase() + polarityName.substring(1), null, "attachment://" + polarityName + "_pol.png");
			MessageEmbed embed = builder.build();
			event.getChannel().sendMessage(embed).addFile(polarityFile).submit();
		}
		
		return true;
	}

}
