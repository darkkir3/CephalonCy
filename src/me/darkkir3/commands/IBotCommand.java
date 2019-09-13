package me.darkkir3.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface IBotCommand 
{
	public String getCommandUsage();
	public String getCommandDescription();
	public String getCommandName();
	public String getUserFriendlyCommandName();
	public boolean handleCommand(MessageListener listener, MessageReceivedEvent event, String userText);
}
