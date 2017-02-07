package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class UserSettings {
	// TODO: implement some kind of storage for setting/getting user settings
	private int musicVolume = 100;
	private int sfxVolume = 100;
	private String characterStyle = CharacterStyles.getFirst();
	private String weaponStyle = WeaponStyles.getFirst();
	private String username = "Player";
	
	public int getMusicVolume() {
		return musicVolume;
	}
	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
		UserSettingsManager.saveSettings(this);
	}
	public int getSfxVolume() {
		return sfxVolume;
	}
	public void setSfxVolume(int sfxVolume) {
		this.sfxVolume = sfxVolume;
		UserSettingsManager.saveSettings(this);
	}
	public String getCharacterStyle() {
		return characterStyle;
	}
	public void setCharacterStyle(String characterStyle) {
		this.characterStyle = characterStyle;
		UserSettingsManager.saveSettings(this);
	}
	public String getWeaponStyle() {
		return weaponStyle;
	}
	public void setWeaponStyle(String weaponStyle) {
		this.weaponStyle = weaponStyle;
		UserSettingsManager.saveSettings(this);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
		UserSettingsManager.saveSettings(this);
	}
	
	
}
