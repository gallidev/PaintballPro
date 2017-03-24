package audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;
import java.util.Random;

/**
 * Class containing sound effects for the game
 *
 * @author Jack Hughes
 */
public class SFXResources {
	// Paintball and click sounds from http://www.freesfx.co.uk
	public AudioClip paintball1 = new AudioClip(new File("res/assets/sfx/paintball-01.wav").toURI().toString());
	public AudioClip paintball2 = new AudioClip(new File("res/assets/sfx/paintball-02.wav").toURI().toString());
	public AudioClip paintball3 = new AudioClip(new File("res/assets/sfx/paintball-03.wav").toURI().toString());
	public AudioClip clickSound = new AudioClip(new File("res/assets/sfx/click.wav").toURI().toString());
	// Splat from http://www.freesound.org/people/gprosser/sounds/360942/
	public AudioClip splat = new AudioClip(new File("res/assets/sfx/splat.wav").toURI().toString());
	// Adapted from http://www.freesound.org/people/InspectorJ/sounds/345560/ (Creative Commons 3.0)
	public AudioClip footsteps = new AudioClip(new File("res/assets/sfx/footsteps.wav").toURI().toString());
	// Adapted from http://www.freesound.org/people/DrMinky/sounds/166184/ (Creative Commons 3.0)
	public AudioClip pickup = new AudioClip(new File("res/assets/sfx/pickup.wav").toURI().toString());
	// Adapted from http://www.freesound.org/people/DrMinky/sounds/166184/ (Creative Commons 3.0)
	public AudioClip flagcollect = new AudioClip(new File("res/assets/sfx/flagcollect.mp3").toURI().toString());

	/**
	 * All paintball sounds available in the game
	 */
	public AudioClip[] paintballCollection = {paintball1, paintball2, paintball3};

	/**
	 * Method to get a random paintball sound
	 *
	 * @return media object containing the paintball sound
	 */
	public AudioClip getRandomPaintball() {
		Random r = new Random();
		int i = r.nextInt(paintballCollection.length);
		return paintballCollection[i];
	}
}
