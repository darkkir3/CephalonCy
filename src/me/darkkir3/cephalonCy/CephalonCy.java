package me.darkkir3.cephalonCy;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import me.darkkir3.utils.WeaponParser;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class CephalonCy 
{
	public static void main(String[] args)
	{
		//initialize weapon library
		WeaponParser.readWeapons();
		
		JDABuilder builder = new JDABuilder("NjEzMzg5NDYxMzEyNjM0ODk5.XV1m9Q.VcYodgPJE1XVPnpa99EeN97bKM4");
	    
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
