package rendering;

import com.google.gson.Gson; //add gson-2.8.0.jar to the project libraries!
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static rendering.Renderer.view;

/**
 * A representation of a game map. All information is deserialised into this class from a JSON map file. All assets are placed in a grid format, where each space on a grid is 64x64 pixels. A map does not have a fixed size, therefore it should be treated as being infinite.<br>
 * Each map object stores its name, an array of walls, floor tile groups, props and spawn points for each team.
 */
@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class Map
{
	private String name;
	private Material[] materials;
	private Wall[] walls;
	private Floor[] floors;
	private Prop[] props;
	private Spawn[] spawns;

	transient private Group wallGroup = new Group(), floorGroup = new Group(), propGroup = new Group();

	/**
	 * Read a map file, extract map information and render all assets onto the scene.
	 *
	 * @param url File location of a map to load
	 * @return Instance of a loaded map
	 */
	static Map load(String url)
	{
		Map map = null;
		try
		{
			map = (new Gson()).fromJson(new FileReader(url), Map.class);

			for(Material material : map.materials)
			{
				material.image = new Image("assets/" + material.name + ".png", 64, 64, true, true);
			}

			for(Floor floor : map.floors)
			{
				for(int i = 0; i < floor.width; i++)
				{
					for(int j = 0; j < floor.height; j++)
					{
						ImageView tile = new ImageView(map.getMaterialImage(floor.material));
						tile.setX((i + floor.x) * 64);
						tile.setY((j + floor.y) * 64);
						map.floorGroup.getChildren().add(tile);
					}
				}
			}
			map.floorGroup.setCache(true);

			InnerShadow floorShadow = new InnerShadow(40, Color.BLACK);
			floorShadow.setChoke(0.4);

			map.floorGroup.setEffect(floorShadow);
			view.getChildren().add(map.floorGroup);

			for(Prop prop : map.props)
			{
				ImageView image = new ImageView(new Image("assets/" + prop.material + ".png", 64, 64, true, true));
				image.setX(prop.x * 64);
				image.setY(prop.y * 64);
				map.propGroup.getChildren().add(image);
			}
			view.getChildren().add(map.propGroup);

			Light.Distant light = new Light.Distant();
			light.setAzimuth(-90.0);
			light.setElevation(40);

			Lighting wallLighting = new Lighting();
			wallLighting.setLight(light);
			wallLighting.setSurfaceScale(5.0);

			//Wall orientation: true for horizontal, false for vertical
			for(Wall wall : map.walls)
			{
				for(int i = 0; i < wall.length; i++)
				{
					ImageView block = new ImageView(map.getMaterialImage(wall.material));
					block.setX(wall.orientation ? (i + wall.x) * 64 : wall.x * 64);
					block.setY(wall.orientation ? wall.y * 64 : (i + wall.y) * 64);
					map.wallGroup.getChildren().add(block);
				}
			}
			map.wallGroup.setCache(true);
			map.wallGroup.setEffect(wallLighting);
			view.getChildren().add(map.wallGroup);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Get <code>ImageView</code> of all wall blocks on the map.
	 *
	 * @return <code>ImageView</code> of all wall blocks
	 */
	public ArrayList<ImageView> getWalls()
	{
		ArrayList<ImageView> blocks = new ArrayList<>();
		for(Node node : wallGroup.getChildren())
			blocks.add((ImageView) node);
		return blocks;
	}

	/**
	 * Get <code>ImageView</code> of all props on the map.
	 *
	 * @return <code>ImageView</code> of all props
	 */
	public ArrayList<ImageView> getProps()
	{
		ArrayList<ImageView> props = new ArrayList<>();
		for(Node node : propGroup.getChildren())
			props.add((ImageView) node);
		return props;
	}

	private Image getMaterialImage(String material)
	{
		for(Material m : materials)
			if(m.name.equals(material))
				return m.image;
		return null;
	}
	//added method by Filippo only for bullets collisions, so I know where to respaw
	public Spawn[] getSpawns() {
		return spawns;
	}

}
