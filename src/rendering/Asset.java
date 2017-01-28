package rendering;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Asset extends ImageView
{
	final AssetType type;
	final int x, y;

	Asset(String url, AssetType type, int x, int y)
	{
		super(new Image(url, 64, 64, true, true));
		this.type = type;
		this.x = x;
		this.y = y;
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
}
