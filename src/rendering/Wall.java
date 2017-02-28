package rendering;

import javafx.scene.Group;

/**
 * A group of blocks that form a wall which players cannot walk through. Each wall is <i>l</i> blocks long, spans at a certain <code>orientation</code>:
 * <ul>
 *     <li><code>true</code> - horizontal</li>
 *     <li><code>false</code> - vertical</li>
 * </ul>
 * in a certain <code>direction</code>:
 * <ul>
 *     <li><code>true</code> - left to right/top to bottom</li>
 *     <li><code>false</code> - right to left/bottom to top</li>
 * </ul>
 * and starts at (<i>x</i>,<i>y</i>).<br>
 */
public class Wall
{
	String material;
	boolean orientation;
	int length, x, y;

	public int getLength() {
		return length;
	}

	public int getY() {
		return y;
	}

	public int getX() { return x; }

	public boolean getOrientation() { return orientation; }
}
