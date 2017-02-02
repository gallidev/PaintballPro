package rendering;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Asset extends ImageView
{
	Asset(String material, int x, int y)
	{
		super(new Image("assets/" + material + ".png", 64, 64, true, true));
		setX(x);
		setY(y);
	}
}