package rendering;

/**
 * Container class for game object locations deserialised from a map. Stores <i>x</i> and <i>y</i> grid coordinates of a game object location.
 *
 * @author Artur Komoter
 */
public class GameObject
{
	private int x, y;

	/**
	 * Get the <i>x</i> grid coordinate of a game object location.
 	 * @return <i>x</i> grid coordinate of a game object location
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Get the <i>y</i> grid coordinate of a game object location.
	 * @return <i>y</i> grid coordinate of a game object location
	 */
	public int getY()
	{
		return y;
	}
}
