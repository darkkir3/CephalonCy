package me.darkkir3.cephalonCy;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class CephalonCy 
{
	public static void main(String[] args)
	{
		JDABuilder builder = new JDABuilder("NjEzMzg5NDYxMzEyNjM0ODk5.XVwQ7A.qQ1vYUAUj8B3_p6tK_br09PnAaA");
	    
	    // Disable parts of the cache
	    builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE));
	    // Enable the bulk delete event
	    builder.setBulkDeleteSplittingEnabled(false);
	    // Disable compression (not recommended)
	    builder.setCompression(Compression.NONE);
	    // Set activity (like "playing Something")
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
