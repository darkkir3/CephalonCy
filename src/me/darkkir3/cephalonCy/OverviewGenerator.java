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

import javax.imageio.ImageIO;

import me.darkkir3.utils.ConfigReader;
import me.darkkir3.weapons.ParsableWeapon;

public class OverviewGenerator 
{
	private static final int marginX = 10, marginY = 10;
	private static final int offsetY = 5;
	
	public static File createOverview(ParsableWeapon weaponToDraw)
	{
		File fileToReturn = new File("generated" + File.separator + "overview" + File.separator + weaponToDraw.name + ".png");
		new File("generated" + File.separator + "overview").mkdirs();
		
		BufferedImage image = new BufferedImage(ConfigReader.readConfigI("overviewWidth"), ConfigReader.readConfigI("overviewHeight"), ConfigReader.readConfigI("overviewImageType"));
		
		Graphics2D g2d = image.createGraphics();
		g2d.setFont(new Font("SansSerif.plain", Font.PLAIN, 18));
		
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setColor(ConfigReader.getNightModeColor());
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        int currentPosY = 2 * marginY;
        int fontHeight = g2d.getFontMetrics().getHeight();
        
        g2d.setColor(Color.WHITE);
        
        /*accuracy does not matter for modding at the moment
        drawStatLine(g2d, marginX, currentPosY, image.getWidth(), ConfigReader.readLangFile("accuracy"), weaponToDraw.accuracy, "");
        currentPosY += fontHeight + offsetY;*/
        drawStatLine(g2d, marginX, currentPosY, (image.getWidth() - marginX) / 2, ConfigReader.readLangFile("critical_chance"), weaponToDraw.criticalChance * 100f, "%");
        currentPosY += fontHeight + 2 * offsetY;
        drawStatLine(g2d, marginX, currentPosY, (image.getWidth() - marginX) / 2, ConfigReader.readLangFile("critical_multiplier"), weaponToDraw.criticalMultiplier, "x");
        currentPosY += fontHeight + 2 * offsetY;
        drawStatLine(g2d, marginX, currentPosY, (image.getWidth() - marginX) / 2, ConfigReader.readLangFile("fire_rate"), weaponToDraw.fireRate, "");
        currentPosY += fontHeight + 2 * offsetY;
        drawStatLine(g2d, marginX, currentPosY, (image.getWidth() - marginX) / 2, ConfigReader.readLangFile("magazine"), weaponToDraw.magazineSize, "");
        currentPosY += fontHeight + 2 * offsetY;
        drawStatLine(g2d, marginX, currentPosY, (image.getWidth() - marginX) / 2, ConfigReader.readLangFile("reload"), weaponToDraw.reloadTime, "");
        currentPosY += fontHeight + 2 * offsetY;
        drawStatLine(g2d, marginX, currentPosY, (image.getWidth() - marginX) / 2, ConfigReader.readLangFile("status"), weaponToDraw.procChance * 100f, "%");
        currentPosY += fontHeight + 2 * offsetY;
        
        currentPosY += fontHeight;
        
        drawStatBars(weaponToDraw, image, g2d, currentPosY, 2.4f, ConfigReader.readLangFile("CRITICAL"), true);        
        currentPosY += (2 * marginY) + offsetY + fontHeight;
        drawStatBars(weaponToDraw, image, g2d, currentPosY, 0.2f, ConfigReader.readLangFile("STATUS"), false);
        currentPosY += (2 * marginY) + offsetY + fontHeight;
        drawStatBars(weaponToDraw, image, g2d, currentPosY, -1.7f, ConfigReader.readLangFile("SUSTAINED"), false);
        
		try 
		{
			ImageIO.write(image, "PNG", fileToReturn);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return fileToReturn;
	}

	private static void drawStatBars(ParsableWeapon weapon, BufferedImage image, Graphics2D g2d, int currentPosY, float value, String barName, boolean drawBarDescription) 
	{
        
        float valueToDraw = value / 3f;
        
        int posX = marginX;
        int posY = currentPosY;
        int width = image.getWidth() - (2 * marginX);
        int height = 2 * marginY;
        
        g2d.setColor(Color.GRAY);
        g2d.fillRect(posX, posY, width, height);
        
        Color startColor = Color.getHSBColor(0.3f, 1f, 0.75f);
        Color endColor = Color.getHSBColor(0.6f, 1f, 0.75f);
        
        GradientPaint gradient = new GradientPaint(0, 0, startColor, width, height, endColor);
        g2d.setPaint(gradient);
        
        float valueOnAxis = (valueToDraw + 1f) / 2f;
        int startX = (int)(posX + width * valueOnAxis);
        int endX = posX + (width / 2);
        
        boolean isNegative = valueToDraw < 0;
        
        int barStart = isNegative ? startX : endX;
        
        g2d.fillRect(barStart, posY, Math.abs(endX - startX), height);
        
        g2d.setColor(Color.WHITE);
        
        for(int x = posX; x < width; x += marginX)
        {
        	g2d.draw3DRect(x, posY, marginX, height, true);
        }
        
        int fontHeight = g2d.getFontMetrics().getHeight();
        
        if(drawBarDescription)
        {
        	g2d.setColor(Color.LIGHT_GRAY);
	        g2d.drawString(ConfigReader.readLangFile("NOT_INCLINED_AT_ALL"), posX, posY - offsetY);
	        
	        String extremelyInclined = ConfigReader.readLangFile("EXTREMELY_INCLINED");
	        int extremelyInclinedLength = g2d.getFontMetrics().stringWidth(extremelyInclined);
	        
	        g2d.drawString(extremelyInclined, posX + width - extremelyInclinedLength, posY - offsetY);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.draw3DRect(marginX, currentPosY, image.getWidth() - (2 * marginX), 2 * marginY, true);
        
        g2d.setPaint(gradient);
        g2d.draw3DRect(barStart, posY, Math.abs(endX - startX), height, false);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(barName, posX + (width / 2) - g2d.getFontMetrics().stringWidth(barName) / 2, posY + height + fontHeight);
	}
	
	private static void drawStatLine(Graphics2D g2d, int startX, int startY, int width, String valueName, Object value, String append)
    {
    	String valueToDraw = formatValueToDraw(value);
    	
    	g2d.setColor(Color.LIGHT_GRAY);
    	g2d.drawString(valueName, startX, startY);
    	g2d.drawString(valueToDraw + append, width - startX - g2d.getFontMetrics().stringWidth(valueToDraw + append), startY);
    	
    	int baseLineY = startY + g2d.getFontMetrics().getDescent();
    	
    	g2d.setStroke(new BasicStroke(1f));
    	g2d.setColor(Color.GRAY);
    	g2d.fillPolygon(
    			new int[] {startX, width - marginX, width + marginX, startX + 2 * marginX}, 
    			new int[] {baseLineY, baseLineY, baseLineY + offsetY, baseLineY + offsetY}, 4);
    	g2d.setColor(Color.WHITE);
    	g2d.drawPolygon(
    			new int[] {startX, width - marginX, width + marginX, startX + 2 * marginX}, 
    			new int[] {baseLineY, baseLineY, baseLineY + offsetY, baseLineY + offsetY}, 4);
    	g2d.setStroke(new BasicStroke(1f));
    	//g2d.drawLine(startX, startY + g2d.getFontMetrics().getDescent(), width - marginX, startY + g2d.getFontMetrics().getDescent());
    	//g2d.drawLine(startX + marginX, startY + g2d.getFontMetrics().getDescent() + 1, width, startY + g2d.getFontMetrics().getDescent() + 1);
    	
    }

	private static String formatValueToDraw(Object value) 
	{
		String valueToDraw = null;
    	if(value instanceof String || value instanceof Integer)
    	{
    		valueToDraw = value.toString();
    	}
    	else if(value instanceof Number)
    	{
    		valueToDraw = String.valueOf(Math.round(((Number)value).floatValue() * 100.0) / 100.0);
    	}
    	
		return valueToDraw;
	}
}
