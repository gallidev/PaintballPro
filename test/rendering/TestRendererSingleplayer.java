package rendering;

import gui.GUIManager;
import helpers.JavaFXTestHelper;
import javafx.scene.shape.Rectangle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRendererSingleplayer
{
	private GUIManager guiManager;
	private Renderer renderer;

	@Before
	public void setUp()
	{
		JavaFXTestHelper.setupApplication();
		guiManager = new GUIManager();
		renderer = new Renderer("elimination", guiManager);
	}

	@After
	public void tearDown()
	{
		renderer.destroy();
		renderer = null;
	}

	@Test
	public void testRenderer()
	{
		assertNotNull(renderer);
		assertNotNull(renderer.getMap());

		assertNotNull(renderer.getHud());
		assertTrue(Renderer.view.getChildren().contains(renderer.getHud()));

		assertNotNull(renderer.player);
		assertTrue(Renderer.view.getChildren().contains(renderer.player));
	}

	@Test
	public void testPauseMenu()
	{
		renderer.togglePauseMenu();
		assertTrue(renderer.getPauseMenuState());

		renderer.togglePauseMenu();
		assertFalse(renderer.getPauseMenuState());
	}

	@Test
	public void testSettingsMenu()
	{
		renderer.togglePauseMenu();
		renderer.toggleSettingsMenu();
		assertTrue(renderer.getSettingsMenuState());

		renderer.toggleSettingsMenu();
		assertFalse(renderer.getSettingsMenuState());
		assertTrue(renderer.getPauseMenuState());
	}

	@Test
	public void generateSprays()
	{
		Rectangle wall = renderer.getMap().getRecWalls().get(0);
		renderer.generateSpray(wall.getX(), wall.getY(), "red");
		wall = renderer.getMap().getRecWalls().get(1);
		renderer.generateSpray(wall.getX(), wall.getY(), "blue");

		Rectangle prop = renderer.getMap().getRecProps().get(0);
		renderer.generateSpray(prop.getX(), prop.getY(), "red");
		prop = renderer.getMap().getRecProps().get(1);
		renderer.generateSpray(prop.getX(), prop.getY(), "blue");
	}

}