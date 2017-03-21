package gui;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
		MenuOption[] set = {new MenuOption("Single Player", true, (event) -> guiManager.transitionTo(Menu.SingleplayerGameType)),
				new MenuOption("Multiplayer", true, (event) -> guiManager.transitionTo(Menu.NicknameServerConnection)),
				new MenuOption("Settings", false, (event) -> guiManager.transitionTo(Menu.Settings)),
				new MenuOption("Help", false, (event) -> guiManager.transitionTo(Menu.Help))};

		// Turn the collection of button options into a GridPane to be displayed
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		GridPane view = new GridPane();

		view.setAlignment(Pos.CENTER);
		view.setHgap(10);
		view.setVgap(10);
		view.setPadding(new Insets(25, 25, 25, 25));

		view.add(iv, 0, 0);
		view.add(grid, 0, 1);

		// Create the scene and return it
		return guiManager.createScene(view);
	}
}
