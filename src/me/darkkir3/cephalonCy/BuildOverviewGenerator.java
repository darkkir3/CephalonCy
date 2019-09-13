package me.darkkir3.cephalonCy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.darkkir3.mods.ParsableMod;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.ImageCache;
import me.darkkir3.utils.ObjectParser;

public class BuildOverviewGenerator 
{
	private static final int marginX = 10, marginY = 10;
	private static Font plainFont = new Font("SansSerif", Font.PLAIN, 20);
	private static Font boldFont = new Font("SansSerif", Font.BOLD, 20);
	
	public static File createOverview()
	{
		//use temporary files for builds
		File fileToReturn = null;
		try 
		{
			fileToReturn = File.createTempFile("build", ".png");
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		BufferedImage image = new BufferedImage(ConfigReader.readConfigI("buildWidth"), ConfigReader.readConfigI("buildHeight"), ConfigReader.readConfigI("overviewImageType"));
		
		Graphics2D g2d = image.createGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setColor(ConfigReader.getEmbedColor());
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        int imageWidth = image.getWidth();
        //only use a part the screen
        int imageHeight = (int)(image.getHeight() * 0.3125f);
        
        int cardWidth = imageWidth / 4 - (2 * marginX);
        int cardHeight = imageHeight / 2 - (2 * marginY) / 2;
        
        int cardOffsetX = cardWidth + (2 * marginX);
        int cardOffsetY = cardHeight + (2 * marginY);
        
        int startX = 0;
        int startY = 0;
        
        String[] modsToDisplay = new String[] {
        		"Serration", 
        		"Split chamber", 
        		"Point strike", 
        		"Vital sense", 
        		"Vigilante armaments", 
        		"Malignant force",
        		"High Voltage",
        		"Primed Shred"};
        
        for(int i = 0; i < 8; i++)
        {
        	ParsableMod modToDisplay = ObjectParser.fetchMod(modsToDisplay[i]);
        	if(i > 0 && i % 4 == 0)
        	{
        		startY += cardOffsetY;
        		startX = 0;
        	}
        	
        	drawModCard(modToDisplay, g2d, startX, startY, cardWidth, cardHeight);
        	startX += cardOffsetX;
        }
        
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
	
	private static void drawEmptyModCard(Graphics2D g2d, int posX, int posY, int cardWidth, int cardHeight)
	{
		int cardStartX = posX + marginX;
		int cardStartY = posY + marginY;
		
		g2d.setColor(Color.darkGray.darker());
		g2d.fillRect(cardStartX, cardStartY, cardWidth, cardHeight);
		
		g2d.setColor(Color.GRAY);
		g2d.draw3DRect(cardStartX, cardStartY, cardWidth, cardHeight, true);
		
		
	}
	
	private static void drawModCard(ParsableMod mod, Graphics2D g2d, int posX, int posY, int cardWidth, int cardHeight)
	{
		drawEmptyModCard(g2d, posX, posY, cardWidth, cardHeight);
		
		g2d.setFont(boldFont);
		
		BufferedImage polarityIcon = ImageCache.getImageFromFile("data" + File.separator + "icons" + File.separator + mod.polarity.name().toLowerCase() + "_pol.png");
		
		int markerX = posX + marginX + cardWidth - polarityIcon.getWidth();
		int markerY = posY + (2 * marginY);
		
		String fusionCount = String.valueOf(mod.fusionLimit + mod.baseDrain);
		int fusionWidth = g2d.getFontMetrics().stringWidth(fusionCount);
		markerX -= fusionWidth;
		
		int markerRight = posX + marginX + cardWidth;
		int markerBottom = markerY + polarityIcon.getHeight();
		
		int[] polyX = new int[] {markerX - marginX, markerX, markerRight, markerRight, markerX};
		int[] polyY = new int[] {markerY + (polarityIcon.getHeight() / 2), markerY, markerY, markerBottom, markerBottom};
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillPolygon(polyX, polyY, 5);
		
		g2d.setColor(Color.GRAY.darker());
		g2d.drawPolygon(polyX, polyY, 5);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(fusionCount, markerX, markerY + (g2d.getFontMetrics().getHeight() / 2) + (polarityIcon.getHeight() / 4));
		g2d.setFont(plainFont);
		
		markerX += fusionWidth;
		g2d.drawImage(polarityIcon, markerX, markerY, 20, 20, null);
		
		g2d.setColor(Color.GRAY);
		
		g2d.setColor(mod.getRarityColor());
		g2d.drawString(
				mod.name, 
				posX + marginX + (cardWidth / 2) - (g2d.getFontMetrics().stringWidth(mod.name) / 2), 
				posY + polarityIcon.getHeight() + (cardHeight / 2) + (g2d.getFontMetrics().getHeight() / 2));
	}
}
