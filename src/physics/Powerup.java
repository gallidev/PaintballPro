package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import rendering.GameObject;
import rendering.ImageFactory;

import java.util.Random;

import integration.server.CollisionHandlerListener;

public class Powerup extends ImageView
{
	private boolean taken = false;
	private PowerupType type;
	private int duration = 15000; //Respawn 15 seconds after being taken
	private GameObject[] locations;
	private CollisionHandlerListener listener;
	private int indexLocation;
	private Powerup otherPowerUp;

	public Powerup(PowerupType type, GameObject[] locations)
	{
		super(ImageFactory.getPowerupImage(type));
		setEffect(new DropShadow(8, type == PowerupType.SHIELD ? Color.GREEN : Color.YELLOW));
		this.type = type;
		this.locations = locations;
		resetPosition();
	}

	public boolean isTaken()
	{
		return taken;
	}

	public void setTaken(boolean b)
	{
		taken = b;
	}

	public PowerupType getType()
	{
		return type;
	}

	public void setType(PowerupType type)
	{
		this.type = type;
	}

	public void setListener(CollisionHandlerListener listener)
	{
		this.listener = listener;
	}

	private void resetPosition()
	{
		indexLocation = (new Random()).nextInt(locations.length);
		if(otherPowerUp != null){
			while(indexLocation == otherPowerUp.getIndexLocation()){
				indexLocation = (new Random()).nextInt(locations.length);
			}
		}

		relocate(locations[indexLocation].getX() * 64 + 16, locations[indexLocation].getY() * 64 + 16);
		if(listener != null)
			listener.onPowerupRespawn(type, indexLocation);
	}

	public void resetPosition(int index)
	{
		relocate(locations[index].getX() * 64 + 16, locations[index].getY() * 64 + 16);
	}

	void took() {
		setVisible(false);
		new java.util.Timer().schedule(
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	setVisible(true);
		            	setTaken(false);
		                resetPosition();
		            }
		        },
		        duration
		);

	}

	public int getIndexLocation(){
		return this.indexLocation;
	}

	public void setOtherPowerUp( Powerup otherPowerUp){
		this.otherPowerUp = otherPowerUp;
		resetPosition();
	}
}
