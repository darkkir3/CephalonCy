package me.darkkir3.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		String value = ConfigReader.readConfigS(key);
		return Float.valueOf(value == null ? "0" : value);
	}
	
	public static int readConfigI(String key)
	{
		String value = ConfigReader.readConfigS(key);
		return Integer.valueOf(value == null ? "0" : value);
	}
	
	public static void setConfig(String key, String value)
	{
		ConfigReader.configToUse.setProperty(key, value);
		System.out.println("Setting config: " + key + " | " + value);
		
		OutputStream out;
		try 
		{
			out = new FileOutputStream("data" + File.separator + "config.properties");
			ConfigReader.configToUse.store(out, "");
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
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
		return ConfigReader.getLangToUse().getProperty(key, key)
				.replace("%commandPrefix%", ConfigReader.readConfigS("commandPrefix"));
	}
}
