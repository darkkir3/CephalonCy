package me.darkkir3.cephalonCy;

import java.io.File;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;

public class MessageListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
    	boolean isPM = event.isFromType(ChannelType.PRIVATE);
    	MessageChannel channel = event.getChannel();
    	if(isPM && event.getAuthor() != null && !(event.getAuthor() instanceof SelfUser))
    	{
    		System.out.println("poking!");
    		System.out.println(event.getAuthor());
    		event.getAuthor().openPrivateChannel().queue( (c) -> 
    		{
    			Message message = new MessageBuilder().append(event.getMessage()).build();
    			c.sendMessage(event.getMessage()).addFile(new File("test.txt"), AttachmentOption.SPOILER);
    		});
    	}
    	
        /*if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());
        }*/
    }
}
