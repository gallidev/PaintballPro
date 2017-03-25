package rendering;

/**
 * A prop acts just like a <code>Wall</code>, except each prop is on its own and acts more like a decoration to the map.
 * @see Wall
 * @author Artur Komoter
 */
public class Prop
{
	String material;
	int x, y;
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}