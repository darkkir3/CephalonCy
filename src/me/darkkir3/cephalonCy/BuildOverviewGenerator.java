package me.darkkir3.cephalonCy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

import javax.imageio.ImageIO;

import me.darkkir3.mods.ModPolarity;
import me.darkkir3.mods.ModType;
import me.darkkir3.mods.ParsableMod;
import me.darkkir3.utils.ConfigReader;
import me.darkkir3.utils.ImageCache;

public class BuildOverviewGenerator 
{
	private static final int marginX = 10, marginY = 10;
	private static Font plainFont = new Font("SansSerif", Font.PLAIN, 20);
	private static Font detailFont = new Font("SansSerif", Font.BOLD, 16);
	private static Font boldFont = new Font("SansSerif", Font.BOLD, 20);
	
	public static File createOverview(ArrayList<ParsableMod> modsToDisplay)
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
        
        HashMap<ParsableMod, Boolean> polarizationSuggestion = BuildOverviewGenerator.calculatePolaritySuggestion(modsToDisplay);
        int totalCapacity = BuildOverviewGenerator.getMaxCapacity(modsToDisplay.get(0).getModType());
        int capacityUsed = 0;
        
        for(int i = 0; i < 8; i++)
        {
        	if(i > 0 && i % 4 == 0)
        	{
        		startY += cardOffsetY;
        		startX = 0;
        	}
        	
        	ParsableMod modToDraw = (modsToDisplay.size() - 1) >= i ? modsToDisplay.get(i) : null;
        	boolean isPolarized = false;
        	if(modToDraw != null)
        	{
        		isPolarized = polarizationSuggestion.getOrDefault(modToDraw, false);
        		capacityUsed += isPolarized ? Math.ceil(modToDraw.getTotalDrain() / 2f) : modToDraw.getTotalDrain();
        	}
        	
        	drawModCard(modToDraw, isPolarized, g2d, startX, startY, cardWidth, cardHeight);
        	startX += cardOffsetX;
        }
        
        startY += cardOffsetY + marginY;
		startX = 0;
		
		drawCapacity(g2d, cardWidth, startX, startY, totalCapacity, capacityUsed);
		
		startY += g2d.getFontMetrics().getHeight() + 2 * marginY;
		
		ArrayList<String> buildEffects = modsToDisplay.get(0).mergeModEffects(modsToDisplay);
		
		int effectPosX = startX + marginX;
		int effectPosY = startY;
		
		g2d.setFont(BuildOverviewGenerator.detailFont);
		g2d.setColor(Color.WHITE);
		
		for(String value : buildEffects)
		{	
			BuildOverviewGenerator.formatModEffects(g2d, value, effectPosX, effectPosY);
			effectPosY += g2d.getFontMetrics().getHeight();
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

	private static void drawCapacity(Graphics2D g2d, int cardWidth, int startX, int startY, int totalCapacity,
			int capacityUsed) {
		g2d.setFont(boldFont);
		
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(marginX + startX, g2d.getFontMetrics().getHeight() + startY - marginY, marginX + startX + cardWidth, g2d.getFontMetrics().getHeight() + startY - marginY);
		
        g2d.setColor(Color.WHITE);
		String capacityString = ConfigReader.readLangFile("CAPACITY");
		g2d.drawString(capacityString, marginX + startX, g2d.getFontMetrics().getHeight() / 2 + startY);
		String capacityValueString = (totalCapacity - capacityUsed) + " / " + totalCapacity;
        g2d.drawString(capacityValueString, 
        		marginX + startX + g2d.getFontMetrics().stringWidth(capacityString) + (cardWidth / 2) - (g2d.getFontMetrics().stringWidth(capacityValueString) / 2),
        		g2d.getFontMetrics().getHeight() / 2 + startY);
	}
	
	private static void drawEmptyModCard(Graphics2D g2d, boolean isPolarized, int posX, int posY, int cardWidth, int cardHeight)
	{
		int cardStartX = posX + marginX;
		int cardStartY = posY + marginY;
		
		g2d.setColor(Color.darkGray.darker());
		g2d.fillRect(cardStartX, cardStartY, cardWidth, cardHeight);
		
		g2d.setColor(Color.GRAY);
		g2d.draw3DRect(cardStartX, cardStartY, cardWidth, cardHeight, true);
		
		
	}
	
	private static void drawModCard(ParsableMod mod, boolean isPolarized, Graphics2D g2d, int posX, int posY, int cardWidth, int cardHeight)
	{
		drawEmptyModCard(g2d, isPolarized, posX, posY, cardWidth, cardHeight);
		
		if(mod == null)
		{
			return;
		}
		
		g2d.setFont(boldFont);
		
		BufferedImage polarityIcon = ImageCache.getImageFromFile("data" + File.separator + "icons" + File.separator + mod.polarity.name().toLowerCase() + "_pol.png");
		
		int markerX = posX + marginX + cardWidth - polarityIcon.getWidth();
		int markerY = posY + (2 * marginY);
		
		String totalDrain = String.valueOf(isPolarized ? ((int)Math.ceil(mod.getTotalDrain() / 2f)) : mod.getTotalDrain());
		int fusionWidth = g2d.getFontMetrics().stringWidth(totalDrain);
		markerX -= fusionWidth;
		
		int markerRight = posX + marginX + cardWidth;
		int markerBottom = markerY + polarityIcon.getHeight();
		
		int[] polyX = new int[] {markerX - marginX, markerX, markerRight, markerRight, markerX};
		int[] polyY = new int[] {markerY + (polarityIcon.getHeight() / 2), markerY, markerY, markerBottom, markerBottom};
		
		g2d.setColor(isPolarized ? Color.DARK_GRAY : Color.LIGHT_GRAY);
		g2d.fillPolygon(polyX, polyY, 5);
		
		g2d.setColor(isPolarized ? Color.GRAY.brighter() : Color.GRAY.darker());
		g2d.drawPolygon(polyX, polyY, 5);
		
		Color formaColor = ConfigReader.readFormaColor();
		
		g2d.setColor(isPolarized ? formaColor : Color.BLACK);
		g2d.drawString(totalDrain, markerX, markerY + (g2d.getFontMetrics().getHeight() / 2) + (polarityIcon.getHeight() / 4));
		g2d.setFont(plainFont);
		
		markerX += fusionWidth;
		if(isPolarized)
		{
			BufferedImage polarityCopy = new BufferedImage(polarityIcon.getWidth(), polarityIcon.getHeight(), BufferedImage.TRANSLUCENT);
			Graphics2D graphicsCopy = polarityCopy.createGraphics();
			graphicsCopy.drawImage(polarityIcon, 0, 0, null);
			
			 for (int i = 0; i < polarityCopy.getWidth(); i++)
			    {
			      for (int j = 0; j < polarityCopy.getHeight(); j++)
			      {
			        int ax = polarityCopy.getColorModel().getAlpha(polarityCopy.getRaster().
			                getDataElements(i, j, null));
			        int rx = 255, gx = 255, bx = 255;
			        rx *= (float)formaColor.getRed() / 255f;
			        gx *= (float)formaColor.getGreen() / 255f;
			        bx *= (float)formaColor.getBlue() / 255f;
			        polarityCopy.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx));
			      }
			    }
			 g2d.drawImage(polarityCopy, markerX, markerY, 20, 20, null);
			 graphicsCopy.dispose();
		}
		else
		{
			g2d.drawImage(polarityIcon, markerX, markerY, 20, 20, null);
		}
		
		g2d.setColor(Color.GRAY);
		
		g2d.setColor(mod.getRarityColor());
		g2d.drawString(
				mod.name, 
				posX + marginX + (cardWidth / 2) - (g2d.getFontMetrics().stringWidth(mod.name) / 2), 
				posY + polarityIcon.getHeight() + (cardHeight / 2) + (g2d.getFontMetrics().getHeight() / 2));
	}
	
	private static HashMap<ParsableMod, Boolean> calculatePolaritySuggestion(ArrayList<ParsableMod> modList)
	{
		HashMap<ParsableMod, Boolean> polarizedMap = new HashMap<ParsableMod, Boolean>();
		
		Optional<ParsableMod> firstNonNullMod = modList.stream().filter(T -> T != null).findFirst();
		ModType buildType = null;
		if(firstNonNullMod.isPresent())
		{
			buildType = firstNonNullMod.get().getModType();
		}
		
		if(buildType != null)
		{
			Integer maxCapacity = getMaxCapacity(buildType);
			if(maxCapacity > 0)
			{
				ArrayList<ParsableMod> modListCopy = new ArrayList<ParsableMod>(modList);
				modListCopy.sort(new Comparator<ParsableMod>()
						{
							@Override
							public int compare(ParsableMod o1, ParsableMod o2) 
							{
								return o2.getTotalDrain() - o1.getTotalDrain();
							}
						});
				Integer availableCapacity = maxCapacity;
				for(ParsableMod mod : modList)
				{
					availableCapacity -= mod.getTotalDrain();
				}
				
				while(availableCapacity < 0 && modListCopy.size() > 0)
				{
					int i = 0;
					ParsableMod highestDrainMod = null;
					do
					{
						highestDrainMod = modListCopy.get(i++);
					}
					while(highestDrainMod.polarity == ModPolarity.UMBRA && i < modListCopy.size());
					
					//we save half of the floored value by polarizing a slot
					availableCapacity += highestDrainMod.getTotalDrain() / 2;
					polarizedMap.put(highestDrainMod, true);
					modListCopy.remove(highestDrainMod);
				}
				
				for(ParsableMod unpolarizedMod : modListCopy)
				{
					polarizedMap.put(unpolarizedMod, false);
				}
			}
		}
		
		return polarizedMap;
	}

	private static Integer getMaxCapacity(ModType buildType) 
	{
		return ConfigReader.readConfigI("modType." + buildType.name().toLowerCase() + ".maxCapacity");
	}
	
	/**adds tabs and formats the specified effect
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 */
	private static void formatModEffects(Graphics2D g, String text, int x, int y)
	{
		if(text.startsWith("+") || text.startsWith("-"))
		{
			text = text.replace("+", "+\t").replace("-", "-\t");
			for(String value : text.split("\t", 3))
			{
				g.drawString(value, x, y);
				x += g.getFontMetrics().stringWidth("_") + g.getFontMetrics().getHeight();
			}
		}
		else
		{
			g.drawString(text, x, y);
		}
	}
}
