package rendering;

import javafx.scene.Group;

class Wall
{
	String material;
	boolean orientation;
	int length, x, y;
	transient Group blocks;
}
