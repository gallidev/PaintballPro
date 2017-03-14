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

	public Flag(Objective[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectiveType.FLAG));
		int randomLocation = (new Random()).nextInt(locations.length);
		relocate(locations[randomLocation].getX() * 64, locations[randomLocation].getY() * 64);
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

	@Override
	public void tick()
	{

	}

	@Override
	public void interact()
	{

	}
}
