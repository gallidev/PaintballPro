package rendering;

import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import javafx.scene.shape.Rectangle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMap
{
	private Map singleMap, serverMap;
	private GUIManager guiManager;
	private Renderer renderer;

	@Before
	public void setUp()
	{
		JavaFXTestHelper.setupApplication();
		guiManager = new GUIManager();
		renderer = new Renderer("elimination", guiManager);
		singleMap = renderer.getMap();
		serverMap = Map.loadRaw("ctf");
	}

	@After
	public void tearDown()
	{
		renderer.destroy();
		renderer = null;
	}

	@Test
	public void getRecSpawn()
	{
		Rectangle redSpawn = singleMap.getRecSpawn(TeamEnum.RED);
		Rectangle blueSpawn = singleMap.getRecSpawn(TeamEnum.BLUE);

		assertNotNull(redSpawn);
		assertNotNull(blueSpawn);

		redSpawn = serverMap.getRecSpawn(TeamEnum.RED);
		blueSpawn = serverMap.getRecSpawn(TeamEnum.BLUE);

		assertNotNull(redSpawn);
		assertNotNull(blueSpawn);
	}

	@Test
	public void getFloors()
	{
		assertNotNull(singleMap.getFloors());
		Floor floor = singleMap.getFloors()[0];
		assertTrue(floor.getHeight() > 0);
		assertTrue(floor.getWidth() > 0);
		assertNotNull(floor.getX());
		assertNotNull(floor.getY());

		assertNotNull(serverMap.getFloors());
		floor = serverMap.getFloors()[1];
		assertTrue(floor.getHeight() > 0);
		assertTrue(floor.getWidth() > 0);
		assertNotNull(floor.getX());
		assertNotNull(floor.getY());
	}

	@Test
	public void getProps()
	{
		assertNotNull(singleMap.getProps());
		Prop prop = singleMap.getProps()[0];
		assertNotNull(prop.getX());
		assertNotNull(prop.getY());

		assertNotNull(serverMap.getProps());
		prop = serverMap.getProps()[1];
		assertNotNull(prop.getX());
		assertNotNull(prop.getY());
	}

	@Test
	public void getWalls()
	{
		assertNotNull(singleMap.getWalls());
		Wall wall = singleMap.getWalls()[0];
		assertTrue(wall.getLength() > 0);
		assertNotNull(wall.getX());
		assertNotNull(wall.getY());
		assertNotNull(wall.getOrientation());

		assertNotNull(serverMap.getWalls());
		wall = serverMap.getWalls()[1];
		assertTrue(wall.getLength() > 0);
		assertNotNull(wall.getX());
		assertNotNull(wall.getY());
		assertNotNull(wall.getOrientation());
	}

	@Test
	public void getSpawns()
	{
		assertNotNull(singleMap.getSpawns());
		assertNotNull(serverMap.getSpawns());
	}

	@Test
	public void getGameMode()
	{
		assertTrue(singleMap.getGameMode() == GameMode.ELIMINATION);
		assertTrue(serverMap.getGameMode() == GameMode.CAPTURETHEFLAG);
	}

	@Test
	public void getFlag()
	{
		assertNull(singleMap.getFlag());
		assertNotNull(serverMap.getFlag());
	}

	@Test
	public void getFlagLocations()
	{
		assertNull(singleMap.getFlagLocations());
		assertNotNull(serverMap.getFlagLocations());
	}

	@Test
	public void getPowerups()
	{
		assertNotNull(singleMap.getPowerups());
		assertNotNull(serverMap.getPowerups());
	}

	@Test
	public void getPowerupLocations()
	{
		assertNotNull(singleMap.getPowerupLocations());
		assertNotNull(serverMap.getPowerupLocations());
	}

	@Test
	public void toggleShading()
	{
		//turn shading on/off
		singleMap.toggleShading();
		singleMap.toggleShading();
	}

}