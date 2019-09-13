package me.darkkir3.commands;

import java.io.File;

import me.darkkir3.cephalonCy.WeaponOverviewGenerator;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.ObjectParser;
import me.darkkir3.weapons.ParsableWeapon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WeaponOverviewCommand implements IBotCommand
{
	@Override
	public String getCommandUsage()
	{
		return ConfigReader.readConfigS("commandPrefix") + this.getCommandName() + " [" + ConfigReader.readLangFile("WEAPON") + "]";
	}

	@Override
	public String getCommandDescription() 
	{
		return ConfigReader.readLangFile("WEAPON_OVERVIEW");
	}

	@Override
	public String getCommandName() 
	{
		return "weapon";
	}
	
	@Override
	public String getUserFriendlyCommandName() 
	{
		return ConfigReader.readLangFile("WEAPON_FRIENDLY");
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
		
		ParsableWeapon weapon = ObjectParser.fetchWeapon(textMessage);
		
		if(weapon != null)
		{
			File fileToSend = WeaponOverviewGenerator.createOverview(weapon, false);
			MessageChannel channel = event.getChannel();
			
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle(weapon.name);
			builder.setDescription(weapon.description);
			builder.setThumbnail(weapon.getImageURL());	
			builder.setColor(ConfigReader.readInfoColor());
			channel.sendMessage(builder.build()).submit();
			
			builder = new EmbedBuilder();
			builder.setTitle(ConfigReader.readLangFile("PRIMARY_MODE"));
			builder.setImage("attachment://weapon_primary.png");
			builder.setColor(ConfigReader.readColor("detailColor"));
			
			channel.sendFile(fileToSend, "weapon_primary.png").embed(builder.build()).queue();
			if(weapon.hasSecondaryStats())
			{
				ParsableWeapon secondaryWeapon = weapon.displaySecondaryStats();
				
				builder = new EmbedBuilder();
				builder.setTitle(ConfigReader.readLangFile("SECONDARY_MODE"));
				builder.setImage("attachment://weapon_secondary.png");
				builder.setColor(ConfigReader.readColor("detailColor"));
				
				channel.sendFile(WeaponOverviewGenerator.createOverview(secondaryWeapon, true), "weapon_secondary.png")
				.embed(builder.build()).queue();
			}
		}
		
		return true;
	}

}
