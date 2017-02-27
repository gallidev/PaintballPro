package audio;

import javafx.scene.media.Media;

import java.io.File;
import java.util.Random;

public class SFXResources {
	// Paintball and click sounds from http://www.freesfx.co.uk
	public Media paintball1 = new Media(new File("res/assets/sfx/paintball-01.wav").toURI().toString());
	public Media paintball2 = new Media(new File("res/assets/sfx/paintball-02.wav").toURI().toString());
	public Media paintball3 = new Media(new File("res/assets/sfx/paintball-03.wav").toURI().toString());
	public Media clickSound = new Media(new File("res/assets/sfx/click.wav").toURI().toString());
	// Splat from http://www.freesound.org/people/gprosser/sounds/360942/
	public Media splat = new Media(new File("res/assets/sfx/splat.wav").toURI().toString());


	public Media[] paintballCollection = {paintball1, paintball2, paintball3};
	public Media getRandomPaintball() {
		Random r = new Random();
		int i = r.nextInt(paintballCollection.length);
		return paintballCollection[i];
	}
}
