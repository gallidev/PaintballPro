package audio;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gui.GUIManager;
import gui.UserSettings;
import helpers.JavaFXTestHelper;
import javafx.scene.media.MediaPlayer;

/**
 * Tests for AudioManager
 *
 * @author Jack Hughes
 */
public class TestAudioManager {

    private AudioManager audio;
    private UserSettings userSettings;
    private GUIManager m;

    /**
     * Setup the test method
     * @throws Exception setup failed
     */
    @Before
    public void setUp() throws Exception {
        // Setup JavaFX
        JavaFXTestHelper.setupApplication();

        // Setup the GUI Manager and User Settings
        m = new GUIManager();
        userSettings = GUIManager.getUserSettings();
        audio = m.getAudioManager();
        // Start and stop the music, so that a musicPlayer
        // is created in the audio manager
        audio.startMusic(audio.music.getRandomTrack());
        audio.stopMusic();
        Thread.sleep(100);
    }

    /**
     * Tear down the test cases
     * @throws Exception tear down failed
     */
    @After
    public void tearDown() throws Exception {
        audio = null;
        userSettings = null;
        m = null;
    }

    /**
     * Check that the AudioManager updates when notified that
     * the settings have been updated
     * @throws Exception test failed
     */
    @Test
    public void settingsChanged() throws Exception {
        // Check that the settings update correctly, by
        // changing the volume, notifying observers, and then
        // checking that the volume has changed
        audio.startMusic(audio.music.track1);
        audio.musicPlayer.setVolume((float)100.0);
        userSettings.setMusicVolume(50);
        m.notifySettingsObservers();
        assertTrue(audio.musicPlayer.getVolume() == (float) 0.5);
        audio.stopMusic();
    }

    /**
     * Check that the music starts correctly
     * @throws Exception test failed
     */
    @Test
    public void startMusic() throws Exception {
        // Check that the music starts correctly
        assertTrue(audio.musicPlayer.getStatus() != MediaPlayer.Status.PLAYING);
        audio.startMusic(audio.music.track1);
        audio.musicPlayer.setVolume((float)1.0);
        Thread.sleep(1000);
        assertTrue(audio.musicPlayer.getStatus() == MediaPlayer.Status.PLAYING);
        audio.musicPlayer.stop();
    }

    /**
     * Check that the music stops correctly
     * @throws Exception test failed
     */
    @Test
    public void stopMusic() throws Exception {
        audio.startMusic(audio.music.track1);
        audio.musicPlayer.setVolume((float)0.0);
        Thread.sleep(1000);
        assertTrue(audio.musicPlayer.getStatus() == MediaPlayer.Status.PLAYING);
        audio.stopMusic();
        Thread.sleep(1000);
        assertTrue(audio.musicPlayer.getStatus() != MediaPlayer.Status.PLAYING);
    }

    /**
     * Check that a SFX item can play correctly
     * @throws Exception test failed
     */
    @Test
    public void playSFX() throws Exception {
        SFXResources sfx = new SFXResources();
        audio.playSFX(sfx.getRandomPaintball(), (float)100.0);
    }

}