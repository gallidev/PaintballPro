package gui;
/**
 * Class to store the user's preferences
 */
public class UserSettings {
	// Options that the user can change, with defaults set
	private int musicVolume = 100;
	private int sfxVolume = 100;
	private String username = "Player";
	private boolean shading = true;
	public static String possibleResolutions[] = {"640x360", "960x540", "1024x576", "1280x720", "1600x900", "1920x1080", "2560x1440", "4096x2304"};
	private String resolution = possibleResolutions[2];

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
	
	/**
	 * Get the user's shading option
	 * @return true if shading is on (default)
	 */
	public boolean getShading() {
		return shading;
	}
	
	/**
	 * Set the shading option, and save this to disk
	 * @param shading true if shading is on (default)
	 */
	public void setShading(boolean shading) {
		this.shading = shading;
		UserSettingsManager.saveSettings(this);
	}

	/**
	 * Get the user's resolution option
	 * @return chosen resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * Set the resolution for the user
	 * @param resolution chosen resolution
	 */
	public void setResolution(String resolution) {
		// Check to see if the chosen resolution is a valid option
		for (String res: possibleResolutions) {
			if (resolution.equals(res)) {
				this.resolution = resolution;
				UserSettingsManager.saveSettings(this);
				return;
			}
		}
	}
}