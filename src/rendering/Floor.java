package rendering;

/**
 * A group of floor tiles that the player can walk on. Each group consists of a material, can be <i>i</i> wide and <i>j</i> tall, and starts at coordinates (<i>x</i>,<i>y</i>).
 * @author Artur Komoter
 */
public class Floor
{
	String material;
	int width, height, x, y;

	/**
	 * Get the height of a group of floor tiles.
	 * @return The height of the group of floor tiles
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Get the width of a group of floor tiles.
	 * @return The width of the group of floor tiles
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Get the <i>x</i> grid coordinate of the top-left tile of a group of floor tiles.
	 * @return <i>x</i> grid coordinate of the top-left tile of the floor tiles
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Get the <i>y</i> grid coordinate of the top-left tile of a group of floor tiles.
	 * @return <i>y</i> grid coordinate of the top-left tile of the floor tiles
	 */
	public int getY()
	{
		return y;
	}
}