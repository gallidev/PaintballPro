
package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import networking.game.GameUpdateListener;
import rendering.GameObject;
import rendering.ImageFactory;

import java.util.ArrayList;
import java.util.Random;

import enums.PowerupType;


/**
 * This class represents a power up.
 *
 * @author Filippo Galli
 * @author Sivarjuen Ravichandran
 * @author Artur Komoter
 */
public class Powerup extends ImageView
{

	/** The Constant time of respawn after the power up has been taken. */
	private static final int duration = 15000; //Respawn 15 seconds after being taken

	/** The type. */
	private PowerupType type;

	/** The other power ups in the map. */
	private ArrayList<Powerup> alternatePowerups = new ArrayList<>();

	/** The possible locations. */
	private GameObject[] locations;

	/** The listener for sending game updates. */
	private GameUpdateListener listener;

	/** The current index location. */
	private int index;

	/**
	 * Instantiates a new power up.
	 *
	 * @param type the type of it
	 * @param locations the possible locations of it
	 */
	public Powerup(PowerupType type, GameObject[] locations)
	{
		super(ImageFactory.getPowerupImage(type));
		setEffect(new DropShadow(8, type == PowerupType.SHIELD ? Color.GREEN : Color.YELLOW));
		this.type = type;
		this.locations = locations;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public PowerupType getType()
	{
		return type;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(GameUpdateListener listener)
	{
		this.listener = listener;
	}

	/**
	 * Reset the position of the power up based on the positions of the others.
	 */
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

	/**
	 * Change the position of the power up to the specified index location
	 *
	 * @param index the index
	 */
	public void resetPosition(int index)
	{
		this.index = index;
		relocate(locations[index].getX() * 64 + 16, locations[index].getY() * 64 + 16);
	}

	/**
	 * Adds the other power up.
	 *
	 * @param alternatePowerup the other power up
	 */
	public void addAlternatePowerup(Powerup alternatePowerup)
	{
		alternatePowerups.add(alternatePowerup);
		resetPosition();
	}

	/**
	 * Take the power up, set it invisible and wait a some times before respawing a new one
	 */
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

	/**
	 * Gets the position index.
	 *
	 * @return the position index
	 */
	public int getIndex()
	{
		return index;
	}
}
