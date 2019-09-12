package me.darkkir3.mods;

import com.google.gson.annotations.SerializedName;

public enum ModType 
{
	/**
	 * warframe mods and augments
	 */
	@SerializedName(value = "Warframe", alternate = {"Warframe Mod"})
	WARFRAME(false),
	/**
	 * warframe auras
	 */
	@SerializedName(value = "Aura", alternate = {"Aura Mod"})
	AURA(false),
	/**
	 * rifle (and beam/bow) mods
	 */
	@SerializedName("Rifle")
	RIFLE(true),
	/**
	 * shotgun only mods
	 */
	@SerializedName(value = "Shotgun", alternate = {"Shotgun Mod"})
	SHOTGUN(true),
	/**
	 * secondary mods
	 */
	@SerializedName(value = "Secondary", alternate = {"Secondary Mod"})
	PISTOL(true),
	/**
	 * melee mods
	 */
	@SerializedName(value = "Melee", alternate = {"Melee Mod"})
	MELEE(true),
	/**
	 * companion mods
	 */
	@SerializedName(value = "Companion", alternate = {"Companion Mod"})
	COMPANION(false),
	/**
	 * sentinel only mods
	 */
	@SerializedName(value = "Sentinel", alternate = {"Sentinel Mod"})
	SENTINEL(false),
	/**
	 * kubrow only mods
	 */
	@SerializedName(value = "Kubrow", alternate = {"Kubrow Mod"})
	KUBROW(false),
	/**
	 * archwing (archgun + archwing) mods
	 */
	@SerializedName(value = "Archwing", alternate = {"Archwing Mod"})
	ARCHWING(false),
	/**
	 * unknown mod type
	 */
	UNKNOWN(false);
	
	private boolean isWeaponMod;
	
	/**
	 * @param weaponMod indicates whether we should add this mod to the list of parsed mods
	 */
	ModType(boolean weaponMod)
	{
		this.isWeaponMod = weaponMod;
	}
	
	public boolean isWeaponMod()
	{
		return this.isWeaponMod;
	}
}
