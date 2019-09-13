package me.darkkir3.cephalonCy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import me.darkkir3.status.StatusTypes;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.DamageUtils;
import me.darkkir3.weapons.ParsableWeapon;

public class BuildOverviewGenerator 
{
	private static final int marginX = 10, marginY = 10;
	private static final int offsetY = 5;
	
	public static File createOverview()
	{
		//use temporary files for builds
		File fileToReturn = null;
		try 
		{
			fileToReturn = File.createTempFile("build", "");
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		BufferedImage image = new BufferedImage(ConfigReader.readConfigI("overviewWidth"), ConfigReader.readConfigI("overviewHeight"), ConfigReader.readConfigI("overviewImageType"));
		
		Graphics2D g2d = image.createGraphics();
		g2d.setFont(new Font("SansSerif.plain", Font.PLAIN, 20));
		
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setColor(ConfigReader.getEmbedColor());
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        int currentPosY = 2 * marginY;
        int fontHeight = g2d.getFontMetrics().getHeight();
        
        g2d.setColor(Color.WHITE);
        
        
        
		try 
		{
			ImageIO.write(image, "PNG", fileToReturn);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		fileToReturn.deleteOnExit();
		return fileToReturn;
	}
}
