package me.darkkir3.cephalonCy;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import me.darkkir3.utils.ObjectParser;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class CephalonCy 
{
	public static void main(String[] args)
	{
		ObjectParser.initialize();
		
		JDABuilder builder = new JDABuilder("");
	    
	    builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE));
	    builder.setBulkDeleteSplittingEnabled(false);
	    builder.setCompression(Compression.NONE);
	    builder.setActivity(Activity.watching("devstream"));
	    
	    builder.addEventListeners(new MessageListener());
	    
	    try 
	    {
			builder.build();
		} 
	    catch (LoginException e) 
	    {
			e.printStackTrace();
		}
	}
}
