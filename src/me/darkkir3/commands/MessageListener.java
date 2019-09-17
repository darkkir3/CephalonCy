package me.darkkir3.commands;

import java.util.HashMap;

import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.MessageUtils;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{
	private HelpCommand helpCommand = new HelpCommand();
	private HashMap<String, IBotCommand> botCommands = new HashMap<>();
	
	public MessageListener()
	{
		this.initialize();
	}
	
	private void initialize()
	{
		this.registerBotCommand(new WeaponOverviewCommand());
		this.registerBotCommand(new ModOverviewCommand());
		this.registerBotCommand(new BuildCommand());
		this.registerBotCommand(helpCommand);
	}
	
	private void registerBotCommand(IBotCommand command)
	{
		botCommands.put(command.getCommandName(), command);
	}
	
	public HashMap<String, IBotCommand> getBotCommands()
	{
		return this.botCommands;
	}
	
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

    	User author = event.getAuthor();
    	if(!author.isBot() && !(author instanceof SelfUser))
    	{
    		String textMessage = event.getMessage().getContentRaw();
    		if(!textMessage.startsWith(ConfigReader.readConfigS("commandPrefix")))
    		{
    			return;
    		}
    		
    		String userTextRaw = event.getMessage().getContentRaw();
			String[] splitParams = userTextRaw.split(" ", 2);
    		String commandToUse = textMessage.split(" ")[0].substring(1).toLowerCase();
    		if(botCommands.containsKey(commandToUse))
    		{
    			IBotCommand botCommandToUse = botCommands.get(commandToUse);
    			if(!botCommandToUse.handleCommand(this, event, splitParams.length > 1 ? splitParams[1] : null))
    			{
    				MessageUtils.sendErrorMessage(event.getChannel(), 
    						botCommandToUse.getCommandUsage(), 
    						botCommandToUse.getUserFriendlyCommandName(), 
    						botCommandToUse.getCommandDescription());
    			}
    		}
    		else
    		{
    			helpCommand.handleCommand(this, event, splitParams.length > 1 ? splitParams[1] : null);
    		}
    	}
    }
}
