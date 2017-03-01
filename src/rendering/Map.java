package rendering;
import com.google.gson.Gson; //add gson-2.8.0.jar to the project libraries!
import enums.TeamEnum;
import gui.GUIManager;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.*;
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
			//load materials
			for(Material material : map.materials)
				material.image = new Image("assets/materials/" + material.name + ".png", 64, 64, true, false);
			//load the ground
			for(Floor floor : map.floors)
			{
				WritableImage tiles = new WritableImage(floor.width * 64, floor.height * 64);
				Image tile = map.getMaterialImage(floor.material);
				for(int i = 0; i < floor.width; i++)
				{
					for(int j = 0; j < floor.height; j++)
					{
						tiles.getPixelWriter().setPixels(i * 64, j * 64, 64, 64, tile.getPixelReader(), 0, 0);
					}
				}
				ImageView tilesView = new ImageView(tiles);
				tilesView.relocate(floor.x * 64, floor.y * 64);
				map.floorGroup.getChildren().add(tilesView);
			}
			map.floorGroup.setCache(true);
			map.floorGroup.setCacheHint(CacheHint.SCALE);
			//load spawns
			WritableImage redSpawn = new WritableImage(128, 128), blueSpawn = new WritableImage(128, 128);
			Image spawnTile = map.getMaterialImage(map.floors[0].material);
			for(int i = 0; i < 2; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					redSpawn.getPixelWriter().setPixels(i * 64, j * 64, 64, 64, spawnTile.getPixelReader(), 0, 0);
					blueSpawn.getPixelWriter().setPixels(i * 64, j * 64, 64, 64, spawnTile.getPixelReader(), 0, 0);
				}
			}
			ImageView redSpawnView = new ImageView(redSpawn), blueSpawnView = new ImageView(blueSpawn);
			redSpawnView.relocate(map.spawns[0].x * 64, map.spawns[0].y * 64);
			redSpawnView.setEffect(new DropShadow(32, 0, 0, Color.RED));
			blueSpawnView.relocate(map.spawns[4].x * 64, map.spawns[4].y * 64);
			blueSpawnView.setEffect(new DropShadow(32, 0, 0, Color.BLUE));
			//load props
			map.loadProps();
			map.propGroup.setCache(true);
			map.propGroup.setCacheHint(CacheHint.SCALE);
			//load walls
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
//			for(Wall wall : map.walls)
//			{
//				WritableImage blocks = new WritableImage(wall.orientation ? wall.length * 64 : 64, wall.orientation ? 64 : wall.length * 64);
//				Image block = map.getMaterialImage(wall.material);
//				for(int i = 0; i < wall.length; i++)
//					blocks.getPixelWriter().setPixels(wall.orientation ? i * 64 : 0, wall.orientation ? 0 : i * 64, 64, 64, block.getPixelReader(), 0, 0);
//				ImageView blocksView = new ImageView(blocks);
//				blocksView.setX(wall.x * 64);
//				blocksView.setY(wall.y * 64);
//				map.wallGroup.getChildren().add(blocksView);
//			}
			map.wallGroup.setCache(true);
			map.wallGroup.setCacheHint(CacheHint.SCALE);
			view.getChildren().addAll(map.floorGroup, redSpawnView, blueSpawnView, map.propGroup, map.wallGroup);
			//turn on shading if the user has it enabled
			if(GUIManager.getUserSettings().getShading())
			{
				DropShadow propShadow = new DropShadow(16, 0, 0, Color.BLACK);
				propShadow.setSpread(0.5);
				propShadow.setHeight(64);
				DropShadow wallShadow = new DropShadow(32, 0, 0, Color.BLACK);
				wallShadow.setSpread(0.5);
				wallShadow.setHeight(64);
				Light.Distant light = new Light.Distant();
				light.setAzimuth(145.0);
				light.setElevation(40);
				Lighting propLighting = new Lighting();
				propLighting.setLight(light);
				propLighting.setSurfaceScale(3.0);
				Lighting wallLighting = new Lighting();
				wallLighting.setLight(light);
				wallLighting.setSurfaceScale(5.0);
				propShadow.setInput(propLighting);
				wallShadow.setInput(wallLighting);
				map.propGroup.setEffect(propShadow);
				map.wallGroup.setEffect(wallShadow);
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return map;
	}
	public static Map loadRaw(String url)
	{
		Map map = null;
		try
		{
			map = (new Gson()).fromJson(new FileReader("res/maps/" + url + ".json"), Map.class);
			for(Material material : map.materials)
				material.image = new Image("assets/materials/" + material.name + ".png", 64, 64, true, true);
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
			map.loadProps();
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
	public Floor[] getFloors()
	{
		return floors;
	}
	public Prop[] getProps()
	{
		return props;
	}
	public Wall[] getWalls() { return walls; }
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
	private void loadProps()
	{
		for(Prop prop : props)
		{
			ImageView image = new ImageView(new Image("assets/materials/" + prop.material + ".png", 64, 64, true, true));
			image.setX(prop.x * 64);
			image.setY(prop.y * 64);
			propGroup.getChildren().add(image);
		}
	}
}