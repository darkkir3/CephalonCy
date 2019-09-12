package me.darkkir3.cephalonCy;

import java.util.HashMap;
import java.util.Map.Entry;

import me.darkkir3.utils.ConfigReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand implements IBotCommand
{
	@Override
	public String getCommandUsage()
	{
		return ConfigReader.readConfigS("commandPrefix") + this.getCommandName();
	}
	
	@Override
	public String getCommandDescription() 
	{
		return ConfigReader.readLangFile("HELP_OVERVIEW");
	}

	@Override
	public String getCommandName() 
	{
		return "help";
	}
	
	@Override
	public String getUserFriendlyCommandName() 
	{
		return ConfigReader.readLangFile("HELP_FRIENDLY");
	}

	@Override
	public boolean handleCommand(MessageListener listener, MessageReceivedEvent event, String userText) 
	{
		HashMap<String, IBotCommand> botCommands = listener.getBotCommands();
		if(botCommands != null)
		{
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle(this.getUserFriendlyCommandName());
			builder.setColor(ConfigReader.readColor("infoColor"));
			for(Entry<String, IBotCommand> entry : botCommands.entrySet())
			{
				IBotCommand botCommand = entry.getValue();
				builder.addField(botCommand.getCommandUsage(), botCommand.getCommandDescription(), true);
			}
			
			event.getChannel().sendMessage(builder.build()).queue();
		}
		
		return true;
	}

}
