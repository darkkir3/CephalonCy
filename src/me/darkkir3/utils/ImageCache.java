package me.darkkir3.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public final class ImageCache 
{
	private static HashMap<String, BufferedImage> imagesFromFile = new HashMap<>();
	private static HashMap<String, BufferedImage> imagesFromURL = new HashMap<>();
	
	public static BufferedImage getImageFromFile(String path)
	{
		if(!imagesFromFile.containsKey(path))
		{
			try 
			{
				imagesFromFile.put(path, ImageIO.read(new File(path)));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return imagesFromFile.get(path);
	}
	
	public static BufferedImage getImageFromURL(String path)
	{
		if(!imagesFromURL.containsKey(path))
		{
			try 
			{
				imagesFromURL.put(path, readFromURL(path));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return imagesFromURL.get(path);
	}
	
	private static BufferedImage readFromURL(String path) throws IOException
	{
		final URL url = new URL(path);
    	final HttpURLConnection connection = (HttpURLConnection) url
    	        .openConnection();
    	connection.setRequestProperty(
    	    "User-Agent",
    	    //custom user agent since the default java vm gets blocked
    	    "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
    	return ImageIO.read(connection.getInputStream());
	}
}
