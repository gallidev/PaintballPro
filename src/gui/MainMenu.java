package gui;

import enums.Menu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Class containing the Main Menu scene
 *
 * @author Jack Hughes
 */
public class MainMenu {

	/**
	 * Return a main menu scene for a given GUI manager
	 *
	 * @param guiManager GUI manager to use
	 * @return main menu scene
	 */
	public static Scene getScene(GUIManager guiManager) {
		Image i = new Image("assets/paintballlogo.png");
		ImageView iv = new ImageView(i);
		iv.setId("logo");
		iv.setPreserveRatio(true);
		iv.setFitWidth(400);

		// Create a set of button options, with each button's title and event handler
		MenuOption[] set = {new MenuOption("Single Player", true, (event) -> guiManager.transitionTo(Menu.SINGLEPLAYER_GAME_TYPE)),
				new MenuOption("Multiplayer", true, (event) -> guiManager.transitionTo(Menu.NICKNAME_SERVER_CONNECTION)),
				new MenuOption("Settings", false, (event) -> guiManager.transitionTo(Menu.SETTINGS)),
				new MenuOption("Help", false, (event) -> guiManager.transitionTo(Menu.HELP))};

		// Turn the collection of button options into a GridPane to be displayed
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		GridPane view = new GridPane();

		view.setAlignment(Pos.CENTER);
		view.setHgap(10);
		view.setVgap(10);
		view.setPadding(MenuControls.scaleByResolution(25));

		view.add(iv, 0, 0);
		view.add(grid, 0, 1);

		// Create the scene and return it
		return guiManager.createScene(view);
	}
}
