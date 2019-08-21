package me.darkkir3.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.darkkir3.weapons.ParsableWeapon;
import me.darkkir3.weapons.WeaponSlot;

public final class WeaponParser 
{
	
	private static JsonArray primaryWeapons;
	private static JsonArray secondaryWeapons;
	
	private static HashMap<String, JsonObject> weaponList = new HashMap<String, JsonObject>();
	
	public static void readWeapons()
	{
		//directly read the raw weapon data of github
		String primaryJson = readFromURL(
				ConfigReader.readConfigS("primaryURL"));
		
		String secondaryJson = readFromURL(
				ConfigReader.readConfigS("secondaryURL"));
		
		JsonParser jsonParser = new JsonParser();
		
		primaryWeapons = jsonParser.parse(primaryJson).getAsJsonArray();
		secondaryWeapons = jsonParser.parse(secondaryJson).getAsJsonArray();
		
		fetchWeapons(primaryWeapons, WeaponSlot.PRIMARY);
		fetchWeapons(secondaryWeapons, WeaponSlot.SECONDARY);
	}
	
	public static ArrayList<String> fetchWeaponNamesBySlot(WeaponSlot weaponSlot)
	{
		if(weaponSlot == WeaponSlot.NONE)
		{
			return new ArrayList<String>(Arrays.asList(weaponList.keySet().toArray(new String[0])));
		}
		
		ArrayList<String> valuesToReturn = new ArrayList<String>();
		
		JsonArray weaponElements = null;
		switch(weaponSlot)
		{
		case PRIMARY:
			weaponElements = primaryWeapons;
			break;
		case SECONDARY:
			weaponElements = secondaryWeapons;
			break;
		default:
			break;
		}
		
		JsonObject o = null;
		for(int i = 0; i < weaponElements.size(); i++)
		{
			o = weaponElements.get(i).getAsJsonObject();
			valuesToReturn.add(o.get("name").getAsString());
		}
		
		return valuesToReturn;
	}
	
	/**Converts the jsonarray of all weapons into a mapping via the weapon name
	 * @param weaponElements
	 */
	private static void fetchWeapons(JsonArray weaponElements, WeaponSlot weaponSlot)
	{
		JsonObject o = null;
		for(int i = 0; i < weaponElements.size(); i++)
		{
			o = weaponElements.get(i).getAsJsonObject();
			if(!o.has("weaponType"))
			{
				o.addProperty("weaponType", weaponSlot.ordinal());
			}
			weaponList.put(o.get("name").getAsString(), o);
		}
	}
	
	/**Fetch the weapon data of a specific weapon
	 * @param weaponName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ParsableWeapon fetchWeapon(String weaponName)
	{
		Gson gson = new Gson();
		if(weaponList.get(weaponName) != null)
		{
			return gson.fromJson(weaponList.get(weaponName), ParsableWeapon.class);
		}
		
		int lowestDistance = Integer.MAX_VALUE;
		int lowestIndex = -1;
		
		String[] weaponNames = weaponList.keySet().toArray(new String[0]);
		int currentDistance = 0;
		for(int i = 0; i < weaponNames.length; i++)
		{
			currentDistance = StringUtils.getLevenshteinDistance(weaponNames[i], weaponName);
			if(currentDistance < lowestDistance)
			{
				lowestDistance = currentDistance;
				lowestIndex = i;
			}
		}
		
		return gson.fromJson(weaponList.get(weaponNames[lowestIndex]), ParsableWeapon.class);
	}
	
	public static WeaponSlot getWeaponSlotByName(String weaponName)
	{
		JsonObject objectToReturn = weaponList.get(weaponName);
		if(objectToReturn != null && objectToReturn.has("weaponType"))
		{
			return WeaponSlot.values()[objectToReturn.get("weaponType").getAsInt()];
		}
		
		return WeaponSlot.NONE;
	}
	
	private static String readFromURL(String url)
	{
		String result = "";
		try
		{
			InputStream in = new URL(url).openStream();
			result = IOUtils.toString(in, StandardCharsets.UTF_8);
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
