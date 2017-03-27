package rendering;

import com.google.gson.Gson;
import enums.GameMode;
import enums.PowerupType;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static rendering.Renderer.VIEW;

/**
 * A representation of a game map. All information is deserialised into this class from a JSON map file. All assets are placed in a grid format, where each space on a grid is 64x64 pixels. A map does not have a fixed size, therefore it should be treated as being infinite.<br><br>
 * Each map stores its gamemode type, powerup locations, flag locations (for a Capture the Flag map), walls, floors, props and spawn locations.<br><br>
 *
 * @author Artur Komoter
 */
@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class Map {
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
	static Map load(String mapName) {
		Map map = null;
		try {
			map = (new Gson()).fromJson(new FileReader("res/maps/" + mapName + ".json"), Map.class);

			int width = 0, height = 0;
			for(Floor floor : map.floors) {
				if(floor.x + floor.width > width)
					width = floor.x + floor.width;
				if(floor.y + floor.height > height)
					height = floor.y + floor.height;
			}

			WritableImage tiles = new WritableImage(width * 64, height * 64);

			//load the ground
			for(Floor floor : map.floors) {
				Image tile = ImageFactory.getMaterialImage(floor.material);
				for(int i = 0; i < floor.width; i++)
					for(int j = 0; j < floor.height; j++)
						tiles.getPixelWriter().setPixels((floor.x + i) * 64, (floor.y + j) * 64, 64, 64, tile.getPixelReader(), 0, 0);
			}
			ImageView floorGroup = new ImageView(tiles);

			//load spawns
			WritableImage redSpawn = new WritableImage(128, 128), blueSpawn = new WritableImage(128, 128);

			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < 2; j++) {
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

			VIEW.getChildren().addAll(floorGroup, redSpawnView, blueSpawnView, map.propGroup, map.wallGroup);

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
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Load a map file without floors and displaying the map assets. Used by the client receiver and the server.
	 *
	 * @param mapName Name of the map file to load
	 * @return Instance of a loaded map
	 */
	public static Map loadRaw(String mapName) {
		Map map = null;
		try {
			map = (new Gson()).fromJson(new FileReader("res/maps/" + mapName + ".json"), Map.class);
			map.loadProps();
			map.loadWalls();

			if(map.gameMode == GameMode.CAPTURE_THE_FLAG)
				map.flag = new Flag(map.flagLocations);

			map.powerups = new Powerup[]{new Powerup(PowerupType.SHIELD, map.powerupLocations), new Powerup(PowerupType.SPEED, map.powerupLocations)
			};
			map.powerups[0].addAlternatePowerup(map.powerups[1]);
			map.powerups[1].addAlternatePowerup(map.powerups[0]);

		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Display all props on the map.
	 */
	private void loadProps() {
		for(Prop prop : props) {
			ImageView image = new ImageView(ImageFactory.getMaterialImage(prop.material));
			image.relocate(prop.x * 64, prop.y * 64);
			propGroup.getChildren().add(image);
		}
	}

	/**
	 * Display all walls on the map.
	 */
	private void loadWalls() {
		for(Wall wall : walls) {
			for(int i = 0; i < wall.length; i++) {
				ImageView block = new ImageView(ImageFactory.getMaterialImage(wall.material));
				block.relocate(wall.orientation ? (i + wall.x) * 64 : wall.x * 64, wall.orientation ? wall.y * 64 : (i + wall.y) * 64);
				wallGroup.getChildren().add(block);
			}
		}
	}

	/**
	 * Get collision bounds for a given <code>Group</code> of map assets.
	 *
	 * @param group The group from which to generate collision bounds
	 * @return An <code>ArrayList</code> of collision bounds
	 */
	private ArrayList<Rectangle> getCollisions(Group group) {
		ArrayList<Rectangle> rectangles = new ArrayList<>();
		group.getChildren().forEach(node ->
		{
			rectangles.add(new Rectangle(node.getBoundsInParent().getMinX(), node.getBoundsInParent().getMinY(), 64, 64));
		});
		return rectangles;
	}

	/**
	 * Get collision bounds for all walls on a map.
	 *
	 * @return An <code>ArrayList</code> of wall collision bounds
	 */
	public ArrayList<Rectangle> getWallCollisionBounds() {
		return getCollisions(wallGroup);
	}

	/**
	 * Get collision bounds for all props on a map.
	 *
	 * @return An <code>ArrayList</code> of prop collision bounds
	 */
	public ArrayList<Rectangle> getPropCollisionBounds() {
		return getCollisions(propGroup);
	}

	/**
	 * Get collision bounds for a given team spawn area.
	 *
	 * @return A collision bound for a given team spawn area
	 */
	public Rectangle getSpawnCollisionBound(TeamEnum team) {
		if(team == TeamEnum.RED)
			return new Rectangle(spawns[0].x * 64, spawns[0].y * 64, 128, 128);
		else
			return new Rectangle(spawns[4].x * 64, spawns[4].y * 64, 128, 128);
	}

	/**
	 * Get all floors on a map.
	 *
	 * @return An array of <code>Floor</code> objects
	 */
	public Floor[] getFloors() {
		return floors;
	}

	/**
	 * Get all props on a map.
	 *
	 * @return An array of <code>Prop</code> objects
	 */
	public Prop[] getProps() {
		return props;
	}

	/**
	 * Get all walls on a map.
	 *
	 * @return An array of <code>Wall</code> objects
	 */
	public Wall[] getWalls() {
		return walls;
	}

	/**
	 * Get all spawn tiles on a map.
	 *
	 * @return An array of <code>Spawn</code> objects
	 */
	public Spawn[] getSpawns() {
		return spawns;
	}

	/**
	 * Get the gamemode of a map.
	 *
	 * @return The gamemode of a map
	 */
	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * For Capture the Flag, get the <code>Flag</code> on a map.
	 *
	 * @return The <code>Flag</code> object
	 */
	public Flag getFlag() {
		return flag;
	}

	/**
	 * For Capture the Flag, get all possible locations a flag could appear in on a map.
	 *
	 * @return An array of <code>GameObject</code> locations
	 */
	public GameObject[] getFlagLocations() {
		return flagLocations;
	}

	/**
	 * Get all powerups that appear on a map.
	 *
	 * @return An array of <code>Powerup</code> objects
	 */
	public Powerup[] getPowerups() {
		return powerups;
	}

	/**
	 * Get all possible locations a powerup could appear in on a map.
	 *
	 * @return An array of <code>GameObject</code> locations
	 */
	public GameObject[] getPowerupLocations() {
		return powerupLocations;
	}

	/**
	 * Toggle the shading of map assets. When toggled off, increases performance on older computers and at higher resolutions.
	 */
	void toggleShading() {
		if(propGroup.getEffect() == null) {
			propGroup.setEffect(propShadow);
			wallGroup.setEffect(wallShadow);
		} else {
			propGroup.setEffect(null);
			wallGroup.setEffect(null);
		}
	}
}