package me.darkkir3.utils;

import java.io.File;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public final class MessageUtils 
{
	public static void sendErrorMessage(MessageChannel channel, String title, String header, String description) 
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(description);
		
		File exclamationFile = new File("data" + File.separator + "icons" + File.separator +  "exclamation.png");
		builder.setAuthor(title, null, "attachment://exclamation.png");
		builder.setTitle(header);
		builder.setColor(ConfigReader.readWarningColor());
		
		MessageEmbed embed = builder.build();
		channel.sendMessage(embed).addFile(exclamationFile).queue();
	}
	
	public static void sendInfoMessage(MessageChannel channel, String title, String header, String description) 
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(description);
		
		File exclamationFile = new File("data" + File.separator + "icons" + File.separator +  "information.png");
		builder.setAuthor(title, null, "attachment://information.png");
		builder.setTitle(header);
		builder.setColor(ConfigReader.readInfoColor());
		
		MessageEmbed embed = builder.build();
		channel.sendMessage(embed).addFile(exclamationFile).queue();
	}
}
