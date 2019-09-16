package me.darkkir3.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import me.darkkir3.cephalonCy.BuildOverviewGenerator;
import me.darkkir3.mods.ParsableMod;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.ObjectParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BuildCommand implements IBotCommand
{
	@Override
	public String getCommandUsage()
	{
		return ConfigReader.readConfigS("commandPrefix") + this.getCommandName() + " [Mod1]+[Mod2]+...";
	}
	
	@Override
	public String getCommandDescription() 
	{
		return ConfigReader.readLangFile("BUILD_OVERVIEW");
	}

	@Override
	public String getCommandName() 
	{
		return "build";
	}
	
	@Override
	public String getUserFriendlyCommandName() 
	{
		return ConfigReader.readLangFile("BUILD_FRIENDLY");
	}

	@Override
	public boolean handleCommand(MessageListener listener, MessageReceivedEvent event, String userText) 
	{
		String[] modsToApply = userText.split(Pattern.quote("+"));
		if(modsToApply == null || modsToApply.length == 0)
		{
			return false;
		}
		
		ArrayList<ParsableMod> modsToUse = new ArrayList<ParsableMod>();
		for(String value : modsToApply)
		{
			ParsableMod modToUse = ObjectParser.fetchMod(value.trim());
			if(modToUse != null)
			{
				modsToUse.add(modToUse);
			}
		}
		
		File fileToSend = BuildOverviewGenerator.createOverview(modsToUse);
		MessageChannel channel = event.getChannel();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(this.getUserFriendlyCommandName());
		builder.setColor(ConfigReader.readDetailColor());
		builder.setImage("attachment://" + fileToSend.getName());
		channel.sendMessage(builder.build()).addFile(fileToSend).submit();
		
		return true;
	}

}
