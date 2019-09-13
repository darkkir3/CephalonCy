package me.darkkir3.commands;

import java.io.File;

import me.darkkir3.cephalonCy.BuildOverviewGenerator;
import me.darkkir3.utils.ConfigReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BuildCommand implements IBotCommand
{
	@Override
	public String getCommandUsage()
	{
		return ConfigReader.readConfigS("commandPrefix") + this.getCommandName();
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
		File fileToSend = BuildOverviewGenerator.createOverview();
		MessageChannel channel = event.getChannel();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(this.getUserFriendlyCommandName());
		builder.setImage("attachment://" + fileToSend.getName());
		channel.sendMessage(builder.build()).addFile(fileToSend).submit();
		
		return true;
	}

}
