package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import logic.GameObject;
import rendering.ImageFactory;
import rendering.Objective;
import rendering.ObjectiveType;

import java.util.Random;

public class Flag extends ImageView implements GameObject
{
	private boolean captured = false;
	private Objective[] locations;


	public Flag(){
		super(ImageFactory.getObjectiveImage(ObjectiveType.FLAG));
		setEffect(new DropShadow(16, Color.BLACK));
	}

	public Flag(Objective[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectiveType.FLAG));
		this.locations = locations;
		int randomLocation = (new Random()).nextInt(this.locations.length);
		relocate(locations[randomLocation].getX() * 64, this.locations[randomLocation].getY() * 64);
		setEffect(new DropShadow(16, Color.BLACK));
	}

	public boolean isCaptured()
	{
		return captured;
	}

	public void setCaptured(boolean b)
	{
		captured = b;
	}

	public void resetPosition(){
		int randomLocation = (new Random()).nextInt(locations.length);
		relocate(locations[randomLocation].getX() * 64, locations[randomLocation].getY() * 64);
		setEffect(new DropShadow(16, Color.BLACK));
	}


	@Override
	public void tick()
	{

	}

	@Override
	public void interact()
	{

	}
}
