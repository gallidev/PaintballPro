package gui;

import enums.Menu;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import rendering.Renderer;

import static org.junit.Assert.fail;

/**
 * Tests for the GUI Manager
 *
 * @author Jack Hughes
 */
public class TestGUIManager {

	/**
	 * Setup the JavaFX application
	 * @throws Exception test failed
	 */
	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		JavaFXTestHelper.waitForPlatform();
	}

	/**
	 * Test the transitionTo method
	 * @throws Exception
	 */
	@Test
	public void transitionTo() throws Exception {
		GUIManager guiManager = new GUIManager();
		Platform.runLater(() -> {
			Stage stage = new Stage();
			guiManager.setStage(stage);

			guiManager.transitionTo(Menu.MAIN_MENU);
			guiManager.transitionTo(Menu.SETTINGS);
			guiManager.transitionTo(Menu.HELP);
			guiManager.transitionTo(Menu.SINGLEPLAYER_GAME_TYPE);
			guiManager.transitionTo(Menu.MULTIPLAYER_GAME_TYPE);
			guiManager.transitionTo(Menu.NICKNAME_SERVER_CONNECTION);
			guiManager.transitionTo(Menu.TEAM_MATCH_SINGLEPLAYER);
			guiManager.transitionTo(Menu.MAIN_MENU);
			guiManager.transitionTo(Menu.CAPTURE_THE_FLAG_SINGLEPLAYER);
			guiManager.transitionTo(Menu.MAIN_MENU);
			guiManager.setRenderer(new Renderer("desert", guiManager));
			guiManager.transitionTo(Menu.END_GAME, "0,0", TeamEnum.BLUE);
			guiManager.transitionTo(Menu.MAIN_MENU);
			try {
				guiManager.transitionTo(null);
				fail("transitionTo null should throw");
			} catch (RuntimeException e) {
				// This will throw
			}
		});
		JavaFXTestHelper.waitForPlatform();
	}

}