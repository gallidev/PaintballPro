package audio;

import javafx.scene.media.Media;

import java.io.File;
import java.util.Random;

public class MusicResources {
	// Track1 from http://www.freesound.org/people/Headphaze/sounds/177101/ (Creative Commons v3.0)
	public Media track1 = new Media(new File("res/assets/music/track1.wav").toURI().toString());
	// Track2 from http://www.freesound.org/people/dingo1/sounds/243979/ (Creative Commons v3.0)
	public Media track2 = new Media(new File("res/assets/music/track2.wav").toURI().toString());

	public Media allTracks[] = {track1, track2};

	public Media getRandomTrack() {
		Random r = new Random();
		int i = r.nextInt(allTracks.length);
		return allTracks[i];
	}
}
