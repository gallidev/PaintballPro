package gui;

/**
 * Class to store the user's preferences
 */
public class UserSettings {
	// Options that the user can change, with defaults set
	private int musicVolume = 100;
	private int sfxVolume = 100;
	private String characterStyle = CharacterStyles.getFirst();
	private String weaponStyle = WeaponStyles.getFirst();
	private String username = "Player";
	
	/**
	 * Get the music volume level
	 * @return music volume level
	 */
	public int getMusicVolume() {
		return musicVolume;
	}
	
	/**
	 * Set the music volume level, and save this to disk
	 * @param musicVolume new music volume level
	 */
	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
		UserSettingsManager.saveSettings(this);
	}
	
	/**
	 * Get the sound FX volume level
	 * @return sound FX volume level
	 */
	public int getSfxVolume() {
		return sfxVolume;
	}
	
	/**
	 * Set the sound FX volume level, and save this to disk
	 * @param sfxVolume new sound FX volume level
	 */
	public void setSfxVolume(int sfxVolume) {
		this.sfxVolume = sfxVolume;
		UserSettingsManager.saveSettings(this);
	}
	
	/**
	 * Get the character style
	 * @return character style
	 */
	public String getCharacterStyle() {
		return characterStyle;
	}
	
	/**
	 * Set the character style, and save this to disk
	 * @param characterStyle new character style
	 */
	public void setCharacterStyle(String characterStyle) {
		this.characterStyle = characterStyle;
		UserSettingsManager.saveSettings(this);
	}
	
	/**
	 * Get the weapon style
	 * @return weapon style
	 */
	public String getWeaponStyle() {
		return weaponStyle;
	}
	
	/**
	 * Set the weapon style, and save this to disk
	 * @param weaponStyle new weapon style
	 */
	public void setWeaponStyle(String weaponStyle) {
		this.weaponStyle = weaponStyle;
		UserSettingsManager.saveSettings(this);
	}
	
	/**
	 * Get the user name
	 * @return user name
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Set the user name, and save this to disk
	 * @param username new user name
	 */
	public void setUsername(String username) {
		this.username = username;
		UserSettingsManager.saveSettings(this);
	}	
	
}
