package rendering;
import javafx.scene.image.Image;

enum AssetType
{
	Wall,
	Floor,
	Objective,
	Prop,
	Powerup;
}

class Asset extends Image
{
	AssetType type;
	int x, y;

	Asset(String url, AssetType type, int x, int y)
	{
		super(url);
		this.type = type;
		this.x = x;
		this.y = y;
	}
}
