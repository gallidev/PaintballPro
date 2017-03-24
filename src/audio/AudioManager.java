package audio;

import gui.GUIManager;
import gui.UserSettings;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class to manage playing of audio resources
 *
 * @author Jack Hughes
 */
public class AudioManager implements gui.UserSettingsObserver {

	MediaPlayer musicPlayer;
	public SFXResources sfx = new SFXResources();
	public MusicResources music = new MusicResources();
	private float musicVolume = (float) 1.0;
	private float sfxVolume = (float) 1.0;
	private UserSettings userSettings;

	/**
	 * Construct a new audio manager
	 *
	 * @param userSettings settings to use for volume choices
	 * @param guiManager   GUIManager to notify for settings changes
	 */
	public AudioManager(UserSettings userSettings, GUIManager guiManager) {
		this.userSettings = userSettings;
		updateVolume();
		guiManager.addSettingsObserver(this);
	}

	/**
	 * Method to update the audio volume when the settings have changed
	 */
	@Override
	public void settingsChanged() {
		updateVolume();
	}

	/**
	 * Method to update the volume from the user's settings
	 */
	private void updateVolume() {
		float musicVol = userSettings.getMusicVolume();
		musicVolume = musicVol / (float) 100.0;
		if (musicPlayer != null) {
			musicPlayer.setVolume(musicVolume);
		}

		float sfxVol = userSettings.getSfxVolume();
		sfxVolume = sfxVol / (float) 100.0;
	}

	/**
	 * Method to start playing a continuous chosen music resource
	 *
	 * @param media resource to play
	 */
	public void startMusic(Media media) {
		musicPlayer = new MediaPlayer(media);
		musicPlayer.setVolume(musicVolume);
		musicPlayer.setOnEndOfMedia(() -> startMusic(media));
		musicPlayer.play();
	}

	/**
	 * Method to stop playing any currently playing music
	 */
	public void stopMusic() {
		if (musicPlayer != null) {
			musicPlayer.stop();
		}
	}

	/**
	 * Method to play a SFX media object
	 *
	 * @param media          sound to play
	 * @param distanceVolume value for the distance away the sound is (1.0 at player, 0.0 at infinity)
	 */
	public void playSFX(Media media, float distanceVolume) {
		(new Thread(() -> {
			MediaPlayer sfxPlayer = new MediaPlayer(media);
			sfxPlayer.getStatus();
			sfxPlayer.setVolume(sfxVolume * distanceVolume);
			sfxPlayer.play();
			sfxPlayer.setOnEndOfMedia(() -> {
				sfxPlayer.stop();
				sfxPlayer.dispose();
			});
		})).start();
	}

}
