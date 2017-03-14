package physics;

import javafx.scene.image.ImageView;
import logic.GameObject;
import rendering.ImageFactory;
import rendering.ObjectiveType;

public class Flag extends ImageView implements GameObject
{
	private boolean captured = false;

	public Flag(int x, int y)
	{
		super(ImageFactory.getObjectiveImage(ObjectiveType.FLAG));
		relocate(x * 64, y * 64);
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
