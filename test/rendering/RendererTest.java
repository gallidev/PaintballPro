package rendering;

import gui.GUIManager;
import helpers.JavaFXTestHelper;
import javafx.scene.shape.Rectangle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RendererTest
{
	private GUIManager guiManager;
	private Renderer rendererSingle;
	private Renderer rendererMultiplayer;

	@Before
	public void setUp()
	{
		JavaFXTestHelper.setupApplication();
		guiManager = new GUIManager();
		rendererSingle = new Renderer("elimination", guiManager);

		//rendererMultiplayer = new Renderer("ctf", guiManager.getClient().getReceiver(), guiManager, null);
	}

	@After
	public void tearDown()
	{
		rendererSingle.destroy();
		rendererSingle = null;

//		rendererMultiplayer.destroy();
//		rendererMultiplayer = null;
	}

	@Test
	public void testSinglePlayer()
	{
		assertNotNull(rendererSingle);
		assertNotNull(rendererSingle.getMap());

		assertNotNull(rendererSingle.getHud());
		assertTrue(Renderer.view.getChildren().contains(rendererSingle.getHud()));

		assertNotNull(rendererSingle.player);
		assertTrue(Renderer.view.getChildren().contains(rendererSingle.player));
	}

	@Test
	public void testPauseMenu()
	{
		rendererSingle.togglePauseMenu();
		assertTrue(rendererSingle.getPauseMenuState());

		rendererSingle.togglePauseMenu();
		assertFalse(rendererSingle.getPauseMenuState());
	}

	@Test
	public void testSettingsMenu()
	{
		rendererSingle.togglePauseMenu();
		rendererSingle.toggleSettingsMenu();
		assertTrue(rendererSingle.getSettingsMenuState());

		rendererSingle.toggleSettingsMenu();
		assertFalse(rendererSingle.getSettingsMenuState());
		assertTrue(rendererSingle.getPauseMenuState());
	}

	@Test
	public void generateSprays()
	{
		Rectangle wall = rendererSingle.getMap().getRecWalls().get(0);
		rendererSingle.generateSpray(wall.getX(), wall.getY(), "red");
		wall = rendererSingle.getMap().getRecWalls().get(1);
		rendererSingle.generateSpray(wall.getX(), wall.getY(), "blue");

		Rectangle prop = rendererSingle.getMap().getRecProps().get(0);
		rendererSingle.generateSpray(prop.getX(), prop.getY(), "red");
		prop = rendererSingle.getMap().getRecProps().get(1);
		rendererSingle.generateSpray(prop.getX(), prop.getY(), "blue");
	}

}