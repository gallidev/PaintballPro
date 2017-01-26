package rendering;
import javafx.scene.image.Image;

import java.io.InputStream;

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
	private AssetType type;

	Asset(InputStream is, AssetType type)
	{
		super(is);
		this.type = type;
	}

	public AssetType getType()
	{
		return type;
	}
}
