package rendering;

import com.google.gson.Gson;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static rendering.Renderer.view;

/**
 * A representation of a game map. All information is deserialised into this class from a JSON map file. All assets are placed in a grid format, where each space on a grid is 64x64 pixels. A map does not have a fixed size, therefore it should be treated as being infinite.<br>
 * Each map object stores its name, an array of walls, floor tile groups and props.
 */
@SuppressWarnings("MismatchedReadAndWriteOfArray")
class Map
{
	private String name;
	private Wall[] walls;
	private Floor[] floors;
	private Prop[] props;

	/**
	 * Read a map file, extract map information and render all assets onto the scene.
	 * @param url File location of a map to load
	 * @return Instance of a loaded map
	 */
	static Map load(String url)
	{
		Map map = null;
		try
		{
			map = (new Gson()).fromJson(new FileReader(url), Map.class);

			for(Floor floor : map.floors)
			{
				floor.tiles = new Group();
				for(int i = 0; i < floor.width; i++)
				{
					for(int j = 0; j < floor.height; j++)
					{
						ImageView tile = new ImageView(new Image("assets/" + floor.material + ".png", 64, 64, true, true));
						tile.setX((i + floor.x) * 64);
						tile.setY((j + floor.y) * 64);
						floor.tiles.getChildren().add(tile);
					}
				}
				view.getChildren().add(floor.tiles);
			}

			//Wall orientation: true for horizontal, false for vertical
			for(Wall wall : map.walls)
			{
				wall.blocks = new Group();
				for(int i = 0; i < wall.length; i++)
				{
					ImageView block = new ImageView(new Image("assets/" + wall.material + ".png", 64, 64, true, true));
					block.setX(wall.orientation ? (i + wall.x) * 64 : wall.x * 64);
					block.setY(wall.orientation ? wall.y * 64 : (i + wall.y) * 64);
					wall.blocks.getChildren().add(block);
				}
				view.getChildren().add(wall.blocks);
			}

			for(Prop prop : map.props)
			{
				prop.image = new ImageView(new Image("assets/" + prop.material + ".png", 64, 64, true, true));
				prop.image.setX(prop.x * 64);
				prop.image.setY(prop.y * 64);
				view.getChildren().add(prop.image);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return map;
	}
}
