package rendering;

import gui.GUIManager;
import helpers.JavaFXTestHelper;
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
		renderer = new Renderer("desert", guiManager);
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
		assertTrue(Renderer.VIEW.getChildren().contains(renderer.getHud()));

		assertNotNull(renderer.player);
		assertTrue(Renderer.VIEW.getChildren().contains(renderer.player));
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
}