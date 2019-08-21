package me.darkkir3.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader 
{
	private static Properties configToUse;
	
	private ConfigReader() {}
	
	private static Properties getConfigToUse()
	{
		if(ConfigReader.configToUse == null)
		{
			try 
			{
				InputStream in = new FileInputStream("data" + File.separator + "config.properties");
				ConfigReader.configToUse = new Properties();
				configToUse.load(in);
			} 
			catch (IOException e) 
			{
				System.err.println("Failed to read config file");
				e.printStackTrace();
			}
		}
		
		return ConfigReader.configToUse;
	}
	
	public static String readConfigS(String key)
	{
		return ConfigReader.getConfigToUse().getProperty(key);
	}
	
	public static float readConfigF(String key)
	{
		return Float.valueOf(ConfigReader.readConfigS(key));
	}
	
	public static int readConfigI(String key)
	{
		return Integer.valueOf(ConfigReader.readConfigS(key));
	}
	
	public static Color getNightModeColor()
	{
		return Color.decode("#36393f");
	}
	
	/**Reads the current lang file
	 * @param key
	 * @return
	 */
	public static String readLangFile(String key)
	{
		//TODO: read string values from lang viles
		return key;
	}
}
