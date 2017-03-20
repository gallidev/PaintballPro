package audio;

import javafx.scene.media.Media;
import java.io.File;
import java.util.Random;

/**
 * Class containing music resources for the game
 */
public class MusicResources {
	// Track1 from http://www.freesound.org/people/Headphaze/sounds/177101/ (Creative Commons v3.0)
	public Media track1 = new Media(new File("res/assets/music/track1.mp3").toURI().toString());
	// Track2 from http://www.freesound.org/people/dingo1/sounds/243979/ (Creative Commons v3.0)
	public Media track2 = new Media(new File("res/assets/music/track2.mp3").toURI().toString());

	/**
	 * All tracks available in the game
	 */
	public Media allTracks[] = {track1, track2};

	/**
	 * Method to get a random track to play
	 * @return media object containing the track
	 */
	public Media getRandomTrack() {
		Random r = new Random();
		int i = r.nextInt(allTracks.length);
		return allTracks[i];
	}
}
