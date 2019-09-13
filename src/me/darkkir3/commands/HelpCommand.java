package me.darkkir3.commands;

import java.io.File;
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
			builder.setColor(ConfigReader.readInfoColor());
			builder.addField(this.getCommandUsage(), this.getCommandDescription(), false);
			
			File informationFile = new File("data" + File.separator + "icons" + File.separator + "information.png");
			for(Entry<String, IBotCommand> entry : botCommands.entrySet())
			{
				IBotCommand botCommand = entry.getValue();
				if(botCommand == this)
				{
					//always display the help page as first entry
					continue;
				}
				builder.addField(botCommand.getCommandUsage(), botCommand.getCommandDescription(), false);
			}
			
			builder.setAuthor(this.getUserFriendlyCommandName(), null, "attachment://information.png");
			event.getChannel().sendMessage(builder.build()).addFile(informationFile).queue();
		}
		
		return true;
	}

}
