package rendering;

import com.google.gson.Gson; //add gson-2.8.0.jar to the project libraries!
import enums.TeamEnum;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
	Spawn[] spawns;

	transient private Group wallGroup = new Group(), floorGroup = new Group(), propGroup = new Group(), spawnGroup[] = new Group[2];

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
				material.image = new Image("assets/" + material.name + ".png", 64, 64, true, true);

			map.spawnGroup[0] = new Group();
			map.spawnGroup[1] = new Group();
			for(Floor floor : map.floors)
			{
				for(int i = 0; i < floor.width; i++)
				{
					floorLoop:
					for(int j = 0; j < floor.height; j++)
					{
						ImageView tile = new ImageView(map.getMaterialImage(floor.material));
						tile.setX((i + floor.x) * 64);
						tile.setY((j + floor.y) * 64);
						for(Spawn spawn : map.spawns)
						{
							if(spawn.x == i + floor.x && spawn.y == j + floor.y)
							{
								map.spawnGroup[spawn.team == TeamEnum.RED ? 0 : 1].getChildren().add(tile);
								continue floorLoop;
							}
						}
						map.floorGroup.getChildren().add(tile);
					}
				}
			}
			map.spawnGroup[0].setEffect(new DropShadow(32, 0, 0, Color.RED));
			map.spawnGroup[0].setCache(true);
			map.spawnGroup[1].setEffect(new DropShadow(32, 0, 0, Color.BLUE));
			map.spawnGroup[1].setCache(true);
			map.floorGroup.setCache(true);
			view.getChildren().add(map.floorGroup);
			view.getChildren().add(map.spawnGroup[0]);
			view.getChildren().add(map.spawnGroup[1]);

			Light.Distant light = new Light.Distant();
			light.setAzimuth(145.0);
			light.setElevation(40);

			Lighting propLighting = new Lighting();
			propLighting.setLight(light);
			propLighting.setSurfaceScale(3.0);

			DropShadow propShadow = new DropShadow(16, 0, 0, Color.BLACK);
			propShadow.setSpread(0.5);
			propShadow.setHeight(64);
			propShadow.setInput(propLighting);

			for(Prop prop : map.props)
			{
				ImageView image = new ImageView(new Image("assets/" + prop.material + ".png", 64, 64, true, true));
				image.setX(prop.x * 64);
				image.setY(prop.y * 64);
				map.propGroup.getChildren().add(image);
			}
			map.propGroup.setCache(true);
			map.propGroup.setEffect(propShadow);
			view.getChildren().add(map.propGroup);

			Lighting wallLighting = new Lighting();
			wallLighting.setLight(light);
			wallLighting.setSurfaceScale(5.0);

			DropShadow wallShadow = new DropShadow(32, 0, 0, Color.BLACK);
			wallShadow.setSpread(0.5);
			wallShadow.setHeight(64);
			wallShadow.setInput(wallLighting);

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
			map.wallGroup.setEffect(wallShadow);
			view.getChildren().add(map.wallGroup);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	static Map loadRaw(String url)
	{
		Map map = null;
		try
		{
			map = (new Gson()).fromJson(new FileReader(url), Map.class);

			for(Material material : map.materials)
				material.image = new Image("assets/" + material.name + ".png", 64, 64, true, true);

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

			for(Prop prop : map.props)
			{
				ImageView image = new ImageView(new Image("assets/" + prop.material + ".png", 64, 64, true, true));
				image.setX(prop.x * 64);
				image.setY(prop.y * 64);
				map.propGroup.getChildren().add(image);
			}

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
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	private ArrayList<Rectangle> getCollisions(Group group)
	{
		ArrayList<Rectangle> rectangles = new ArrayList<>();
		for(Node node : group.getChildren())
		{
			Rectangle r = new Rectangle();
			r.setX(node.getBoundsInParent().getMinX());
			r.setY(node.getBoundsInParent().getMinY());
			r.setWidth(node.getBoundsInParent().getWidth());
			r.setHeight(node.getBoundsInParent().getHeight());
			rectangles.add(r);
		}
		return rectangles;
	}

	public ArrayList<Rectangle> getRecWalls()
	{
		return getCollisions(wallGroup);
	}

	public ArrayList<Rectangle> getRecProps()
	{
		return getCollisions(propGroup);
	}

	/**
	 * @return The spawn points of both teams
	 * @author Filippo Galli
	 */
	public Spawn[] getSpawns()
	{
		return spawns;
	}

	private Image getMaterialImage(String material)
	{
		for(Material m : materials)
			if(m.name.equals(material))
				return m.image;
		return null;
	}

}
