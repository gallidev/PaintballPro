package physics;

import integrationServer.CollisionsHandlerListener;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import rendering.GameObject;
import rendering.ImageFactory;

import java.util.ArrayList;
import java.util.Random;

public class Powerup extends ImageView
{
	private static final int duration = 15000; //Respawn 15 seconds after being taken
	private PowerupType type;
	private ArrayList<Powerup> alternatePowerups = new ArrayList<>();
	private GameObject[] locations;
	private CollisionsHandlerListener listener;
	private int index;

	public Powerup(PowerupType type, GameObject[] locations)
	{
		super(ImageFactory.getPowerupImage(type));
		setEffect(new DropShadow(8, type == PowerupType.SHIELD ? Color.GREEN : Color.YELLOW));
		this.type = type;
		this.locations = locations;
	}

	public PowerupType getType()
	{
		return type;
	}

	public void setListener(CollisionsHandlerListener listener)
	{
		this.listener = listener;
	}

	private void resetPosition()
	{
		int randomLocation = (new Random()).nextInt(locations.length);

		for(Powerup alternatePowerup : alternatePowerups)
			if(randomLocation == alternatePowerup.getIndex())
			{
				resetPosition();
				return;
			}

		double x = locations[randomLocation].getX() * 64 + 16, y = locations[randomLocation].getY() * 64 + 16;
		relocate(x, y);
		index = randomLocation;
		if(listener != null)
			listener.onPowerupRespawn(type, index);
	}

	public void resetPosition(int index)
	{
		this.index = index;
		relocate(locations[index].getX() * 64 + 16, locations[index].getY() * 64 + 16);
	}

	public void addAlternatePowerup(Powerup alternatePowerup)
	{
		alternatePowerups.add(alternatePowerup);
		resetPosition();
	}

	void take() {
		setVisible(false);
		new java.util.Timer().schedule(
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	setVisible(true);
			            resetPosition();
		            }
		        },
		        duration
		);

	}

	public int getIndex()
	{
		return index;
	}
}
