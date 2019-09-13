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
	private static Properties langToUse;
	
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
				in.close();
			} 
			catch (IOException e) 
			{
				System.err.println("Failed to read config/lang file");
				e.printStackTrace();
			}
		}
		
		return ConfigReader.configToUse;
	}
	
	private static Properties getLangToUse()
	{
		if(ConfigReader.langToUse == null)
		{
			try 
			{
				InputStream in = new FileInputStream("data" + File.separator + "lang" + File.separator + readConfigS("defaultLang") + ".properties");
				ConfigReader.langToUse = new Properties();
				langToUse.load(in);
				in.close();
			} 
			catch (IOException e) 
			{
				System.err.println("Failed to read lang file");
				e.printStackTrace();
			}
		}
		
		return ConfigReader.langToUse;
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
	
	public static Color readColor(String key)
	{
		return new Color(readConfigI(key));
	}
	
	public static Color readInfoColor()
	{
		return readColor("infoColor");
	}
	
	public static Color readWarningColor()
	{
		return readColor("warningColor");
	}
	
	public static Color readDetailColor()
	{
		return readColor("detailColor");
	}
	
	public static Color getNightModeColor()
	{
		return Color.decode("#36393f");
	}
	
	public static Color getEmbedColor()
	{
		return Color.decode("#34363c");
	}
	
	/**Reads the current lang file
	 * @param key
	 * @return
	 */
	public static String readLangFile(String key)
	{
		return ConfigReader.getLangToUse().getProperty(key, key);
	}
}
