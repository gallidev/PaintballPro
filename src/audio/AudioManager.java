package audio;

import gui.GUIManager;
import gui.UserSettings;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager implements gui.UserSettingsObserver {

    public SFXResources sfx = new SFXResources();
    public MediaPlayer musicPlayer;
    private float musicVolume = (float) 100.0;
    private float sfxVolume = (float) 100.0;

    private UserSettings s;

    public AudioManager(UserSettings s, GUIManager m) {
        this.s = s;
        updateVolume();
        m.addSettingsObserver(this);
    }

    @Override
    public void settingsChanged() {
        updateVolume();
    }

    private void updateVolume() {
        float musicVol = s.getMusicVolume();
        musicVolume = musicVol / (float) 100.0;
        if (musicPlayer != null) {
            musicPlayer.setVolume(musicVol);
        }

        float sfxVol = s.getSfxVolume();
        sfxVolume = sfxVol / (float) 100.0;
    }

    public void startMusic(Media media) {
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(musicVolume);
        musicPlayer.setOnEndOfMedia(() -> startMusic(media));
        musicPlayer.play();
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public void playSFX(Media media, float distanceVolume) {
        MediaPlayer sfxPlayer = new MediaPlayer(media);
        sfxPlayer.getStatus();
        sfxPlayer.setVolume(sfxVolume * distanceVolume);
        sfxPlayer.play();
    }

}
