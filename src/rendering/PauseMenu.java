package rendering;

import enums.Menu;
import gui.GUIManager;
import gui.MenuControls;
import gui.MenuOption;
import gui.MenuOptionSet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Class containing the game pause menu
 *
 * @author Jack Hughes
 */
class PauseMenu extends SubScene {

	static GridPane gridPane = new GridPane();
	boolean opened = false;

	/**
	 * Create a new pause menu
	 *
	 * @param guiManager GUIManager for the game
	 */
	PauseMenu(GUIManager guiManager) {
		super(gridPane, guiManager.width, guiManager.height);
		gridPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);");
		setScaleX(1024 / guiManager.width);
		setScaleY(576 / guiManager.height);

		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(25, 25, 25, 25));

		Image i = new Image("assets/paintballlogo.png");
		ImageView iv = new ImageView(i);
		iv.setId("logo");
		iv.setPreserveRatio(true);
		iv.setFitWidth(150);
		gridPane.add(MenuControls.centreInPane(iv), 0, 0);

		Label title = new Label("Paused");
		title.setStyle("-fx-text-alignment: center; -fx-font-size: 32px");
		gridPane.add(MenuControls.centreInPane(title), 0, 1);

		MenuOption[] set = {new MenuOption("Resume", true, (event) -> {
			System.out.println("ActionEvent: " + event);
			GUIManager.renderer.togglePauseMenu();
		}), new MenuOption("Settings", true, (event) -> {
			System.out.println("ActionEvent: " + event);
			GUIManager.renderer.toggleSettingsMenu();
		}), new MenuOption("Back to Main Menu", false, (event) -> {
			guiManager.exitClient();
			guiManager.transitionTo(Menu.MainMenu);
			System.out.println("ActionEvent: " + event);
		})};

		GridPane buttonSet = MenuOptionSet.optionSetToGridPane(set);
		gridPane.add(buttonSet, 0, 2);

		gridPane.getStylesheets().add("styles/menu.css");
	}
}
