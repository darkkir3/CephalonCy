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

import me.darkkir3.mods.ModType;
import me.darkkir3.mods.ParsableMod;
import me.darkkir3.weapons.ParsableWeapon;
import me.darkkir3.weapons.WeaponSlot;

public final class ObjectParser 
{
	
	private static JsonArray primaryWeapons;
	private static JsonArray secondaryWeapons;
	private static JsonArray mods;
	
	private static HashMap<String, JsonObject> weaponList = new HashMap<String, JsonObject>();
	private static HashMap<String, JsonObject> modList = new HashMap<String, JsonObject>();
	
	public static void initialize()
	{
		readWeapons();
		readMods();
	}
	
	private static void readWeapons()
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
	
	private static void readMods()
	{
		String modJson = readFromURL(
				ConfigReader.readConfigS("modURL"));
		
		JsonParser jsonParser = new JsonParser();
		mods = jsonParser.parse(modJson).getAsJsonArray();
		
		fetchMods(mods);
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
	
	private static void fetchMods(JsonArray modElements)
	{
		Gson gson = new Gson();
		JsonObject o = null;
		for(int i = 0; i < modElements.size(); i++)
		{
			o = modElements.get(i).getAsJsonObject();
			if(o.has("type"))
			{
				ModType type = gson.fromJson(o.get("type"), ModType.class);
				if(type == null || !type.isWeaponMod())
				{
					continue;
				}
			}
			//remove fields that are not needed
			if(o.has("drops"))
			{
				o.remove("drops");
			}
			modList.put(o.get("name").getAsString(), o);
		}
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
			//remove fields that are not needed
			if(o.has("components"))
			{
				o.remove("components");
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
	
	@SuppressWarnings("deprecation")
	public static ParsableMod fetchMod(String modName)
	{
		Gson gson = new Gson();
		if(modList.get(modName) != null)
		{
			return gson.fromJson(modList.get(modName), ParsableMod.class);
		}
		
		int lowestDistance = Integer.MAX_VALUE;
		int lowestIndex = -1;
		
		String[] modNames = modList.keySet().toArray(new String[0]);
		int currentDistance = 0;
		for(int i = 0; i < modNames.length; i++)
		{
			currentDistance = StringUtils.getLevenshteinDistance(modNames[i], modName);
			if(currentDistance < lowestDistance)
			{
				lowestDistance = currentDistance;
				lowestIndex = i;
			}
		}
		
		return gson.fromJson(modList.get(modNames[lowestIndex]), ParsableMod.class);
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
