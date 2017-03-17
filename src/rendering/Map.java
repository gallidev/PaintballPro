package rendering;

import com.google.gson.Gson;
import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import physics.Flag;
import physics.Powerup;
import physics.PowerupType;

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
	GameMode gameMode;
	transient Flag flag;
	transient Powerup[] powerups = new Powerup[2];
	GameObject[] flagLocations;
	GameObject[] powerupLocations;
	private transient DropShadow propShadow, wallShadow;
	private transient Lighting propLighting, wallLighting;
	private Wall[] walls;
	private Floor[] floors;
	private Prop[] props;
	private Spawn[] spawns;
	transient private Group wallGroup = new Group(), propGroup = new Group();

	/**
	 * Read a map file, extract map information and render all assets onto the scene.
	 *
	 * @param mapName Name of the map file to load
	 * @return Instance of a loaded map
	 */
	static Map load(String mapName)
	{
		Map map = null;
		try
		{
			map = (new Gson()).fromJson(new FileReader("res/maps/" + mapName + ".json"), Map.class);

			int width = 0, height = 0;
			for(Floor floor : map.floors)
			{
				if(floor.x + floor.width > width)
					width = floor.x + floor.width;
				if(floor.y + floor.height > height)
					height = floor.y + floor.height;
			}

			WritableImage tiles = new WritableImage(width * 64, height * 64);

			//load the ground
			for(Floor floor : map.floors)
			{
				Image tile = ImageFactory.getMaterialImage(floor.material);
				for(int i = 0; i < floor.width; i++)
					for(int j = 0; j < floor.height; j++)
						tiles.getPixelWriter().setPixels((floor.x + i) * 64, (floor.y + j) * 64, 64, 64, tile.getPixelReader(), 0, 0);
			}
			ImageView floorGroup = new ImageView(tiles);

			//load spawns
			WritableImage redSpawn = new WritableImage(128, 128), blueSpawn = new WritableImage(128, 128);

			for(int i = 0; i < 2; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					redSpawn.getPixelWriter().setPixels(i * 64, j * 64, 64, 64, ImageFactory.getMaterialImage(map.floors[0].material).getPixelReader(), 0, 0);
					blueSpawn.getPixelWriter().setPixels(i * 64, j * 64, 64, 64, ImageFactory.getMaterialImage(map.floors[1].material).getPixelReader(), 0, 0);
				}
			}
			ImageView redSpawnView = new ImageView(redSpawn), blueSpawnView = new ImageView(blueSpawn);
			redSpawnView.relocate(map.spawns[0].x * 64, map.spawns[0].y * 64);
			redSpawnView.setEffect(new DropShadow(32, 0, 0, Color.RED));
			blueSpawnView.relocate(map.spawns[4].x * 64, map.spawns[4].y * 64);
			blueSpawnView.setEffect(new DropShadow(32, 0, 0, Color.BLUE));

			map.loadProps();
			map.propGroup.setCache(true);

			map.loadWalls();
			map.wallGroup.setCache(true);

			view.getChildren().addAll(floorGroup, redSpawnView, blueSpawnView, map.propGroup, map.wallGroup);

			map.initGameObjects();
			view.getChildren().addAll(map.powerups);

			//define shading
			map.propShadow = new DropShadow(16, 0, 0, Color.BLACK);
			map.propShadow.setSpread(0.5);
			map.propShadow.setHeight(64);

			map.wallShadow = new DropShadow(32, 0, 0, Color.BLACK);
			map.wallShadow.setSpread(0.5);
			map.wallShadow.setHeight(64);

			Light.Distant light = new Light.Distant();
			light.setAzimuth(145.0);
			light.setElevation(40);

			map.propLighting = new Lighting();
			map.propLighting.setLight(light);
			map.propLighting.setSurfaceScale(3.0);

			map.wallLighting = new Lighting();
			map.wallLighting.setLight(light);
			map.wallLighting.setSurfaceScale(5.0);

			map.propShadow.setInput(map.propLighting);
			map.wallShadow.setInput(map.wallLighting);

			//turn on shading if the user has it enabled
			if(GUIManager.getUserSettings().getShading())
				map.toggleShading();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public static Map loadRaw(String mapName)
	{
		Map map = null;
		try
		{
			map = (new Gson()).fromJson(new FileReader("res/maps/" + mapName + ".json"), Map.class);
			map.loadProps();
			map.loadWalls();
			map.initGameObjects();

		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	private void loadProps()
	{
		for(Prop prop : props)
		{
			ImageView image = new ImageView(ImageFactory.getMaterialImage(prop.material));
			image.relocate(prop.x * 64, prop.y * 64);
			propGroup.getChildren().add(image);
		}
	}

	private void loadWalls()
	{
		for(Wall wall : walls)
		{
			for(int i = 0; i < wall.length; i++)
			{
				ImageView block = new ImageView(ImageFactory.getMaterialImage(wall.material));
				block.relocate(wall.orientation ? (i + wall.x) * 64 : wall.x * 64, wall.orientation ? wall.y * 64 : (i + wall.y) * 64);
				wallGroup.getChildren().add(block);
			}
		}
	}

	private ArrayList<Rectangle> getCollisions(Group group)
	{
		ArrayList<Rectangle> rectangles = new ArrayList<>();
		group.getChildren().forEach(node ->
		{
			rectangles.add(new Rectangle(node.getBoundsInParent().getMinX(), node.getBoundsInParent().getMinY(), 64, 64));
		});
		return rectangles;
	}

	private void initGameObjects()
	{
		if(gameMode == GameMode.CAPTURETHEFLAG)
		{
			flag = new Flag(flagLocations);
			view.getChildren().add(flag);
		}

		powerups = new Powerup[] { new Powerup(PowerupType.SHIELD, powerupLocations), new Powerup(PowerupType.SPEED, powerupLocations)
		};
	}

	public ArrayList<Rectangle> getRecWalls()
	{
		return getCollisions(wallGroup);
	}

	public ArrayList<Rectangle> getRecProps()
	{
		return getCollisions(propGroup);
	}

	public Rectangle getRecSpawn(TeamEnum team)
	{
		if(team == TeamEnum.RED)
			return new Rectangle(spawns[0].x * 64, spawns[0].y * 64, 128, 128);
		else
			return new Rectangle(spawns[4].x * 64, spawns[4].y * 64, 128, 128);
	}

	public Floor[] getFloors()
	{
		return floors;
	}

	public Prop[] getProps()
	{
		return props;
	}

	public Wall[] getWalls()
	{
		return walls;
	}

	public Spawn[] getSpawns()
	{
		return spawns;
	}

	public GameMode getGameMode()
	{
		return gameMode;
	}

	public Flag getFlag()
	{
		return flag;
	}

	public GameObject[] getFlagLocations()
	{
		return flagLocations;
	}

	void toggleShading()
	{
		if(propGroup.getEffect() == null)
		{
			propGroup.setEffect(propShadow);
			wallGroup.setEffect(wallShadow);
		}
		else
		{
			propGroup.setEffect(null);
			wallGroup.setEffect(null);
		}
	}
}