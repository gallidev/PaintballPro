package audio;

import gui.GUIManager;
import gui.UserSettings;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Random;

public class AudioManager implements gui.UserSettingsObserver {
	
	MediaPlayer musicPlayer;
	float musicVolume = (float) 100.0;
	float sfxVolume = (float) 100.0;
	Random r = new Random();
	
	UserSettings s;
	
	public AudioManager(UserSettings s, GUIManager m) {
		this.s = s;
		updateVolume();
		m.addSettingsObserver(this);
	}
	
	@Override
	public void settingsChanged() {
		updateVolume();
	}
	
	public void updateVolume() {
		float musicVol = s.getMusicVolume();
		musicVolume = musicVol / (float)100.0;
		if (musicPlayer != null) {
			musicPlayer.setVolume(musicVol);
		}
		
		float sfxVol = s.getSfxVolume();
		sfxVolume = sfxVol / (float)100.0;
	}
	
	public void startMusic(String fileURL) {
		Media soundMedia = new Media(fileURL);
		musicPlayer = new MediaPlayer(soundMedia);
		musicPlayer.setVolume(musicVolume);
		musicPlayer.play();
	}
	
	public void stopMusic() {
		if (musicPlayer != null) {
			musicPlayer.stop();
		}
	}
	
	public void playSFX(String fileURL) {
		if (fileURL.equals(SFXResources.paintballRandom)) {
			int i = r.nextInt(SFXResources.paintballCollection.length);
			Media soundMedia = new Media(SFXResources.paintballCollection[i]);
			MediaPlayer sfxPlayer = new MediaPlayer(soundMedia);
			sfxPlayer.setVolume(sfxVolume);
			sfxPlayer.play();
		} else {
			Media soundMedia = new Media(fileURL);
			MediaPlayer sfxPlayer = new MediaPlayer(soundMedia);
			sfxPlayer.setVolume(sfxVolume);
			sfxPlayer.play();
		}
	}
	
}
