package me.darkkir3.cephalonCy;

import java.io.File;

import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.WeaponParser;
import me.darkkir3.weapons.ParsableWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

    	User author = event.getAuthor();
    	if(!author.isBot() && !(author instanceof SelfUser))
    	{
    		String textMessage = event.getMessage().getContentRaw();
    		if(!textMessage.startsWith("!"))
    		{
    			return;
    		}
    		textMessage = textMessage.substring(1);
    		ParsableWeapon weapon = WeaponParser.fetchWeapon(textMessage);
    		
    		if(weapon != null)
    		{
	    		File fileToSend = OverviewGenerator.createOverview(weapon, false);
	    		MessageChannel channel = event.getChannel();
	    		
	    		EmbedBuilder builder = new EmbedBuilder();
	    		builder.setTitle(weapon.name);
	    		builder.setDescription(weapon.description);
	    		builder.setThumbnail(weapon.getImageURL());	
	    		channel.sendMessage(builder.build()).submit();
	    		
	    		builder = new EmbedBuilder();
	    		builder.setTitle(ConfigReader.readLangFile("PRIMARY_MODE"));
	    		builder.setImage("attachment://weapon_primary.png");
	    		
	    		channel.sendFile(fileToSend, "weapon_primary.png").embed(builder.build()).queue();
	    		if(weapon.hasSecondaryStats())
	    		{
	    			ParsableWeapon secondaryWeapon = weapon.displaySecondaryStats();
	    			
	    			builder = new EmbedBuilder();
		    		builder.setTitle(ConfigReader.readLangFile("SECONDARY_MODE"));
		    		builder.setImage("attachment://weapon_secondary.png");
	    			
		    		channel.sendFile(OverviewGenerator.createOverview(secondaryWeapon, true), "weapon_secondary.png")
		    		.embed(builder.build()).queue();
	    		}
    		}
    	}
    }
}
