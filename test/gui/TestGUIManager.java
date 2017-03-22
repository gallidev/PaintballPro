package gui;

import enums.Menu;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

			guiManager.transitionTo(Menu.MainMenu);
			guiManager.transitionTo(Menu.Settings);
			guiManager.transitionTo(Menu.Help);
			guiManager.transitionTo(Menu.SingleplayerGameType);
			guiManager.transitionTo(Menu.MultiplayerGameType);
			guiManager.transitionTo(Menu.NicknameServerConnection);
			guiManager.transitionTo(Menu.EliminationSingle);
			guiManager.transitionTo(Menu.MainMenu);
			guiManager.transitionTo(Menu.CTFSingle);
			guiManager.transitionTo(Menu.MainMenu);
			guiManager.transitionTo(Menu.EndGame, "0,0", TeamEnum.BLUE);
			guiManager.transitionTo(Menu.MainMenu);
		});
		JavaFXTestHelper.waitForPlatform();
	}

}