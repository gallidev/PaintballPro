package gui;

import enums.GameLocation;
import enums.Menu;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

/**
 * Class for the game type picker screen
 */
public class GameTypeMenu {
	
	/**
	 * Return a game type menu scene for a given GUI manager
	 * @param m GUI manager to use
	 * @return game type menu scene
	 */
	public static Scene getScene(GUIManager m, GameLocation loc) {

		MenuOption[] empty = {};
		GridPane mainGrid = MenuOptionSet.optionSetToGridPane(empty);

		LoadingPane sp = new LoadingPane(mainGrid);

		// Create a set of button options, with each button's title and event handler
		MenuOption[] set = {new MenuOption("Team Match", true, event -> {
			if (loc == GameLocation.MultiplayerServer) {
					m.transitionTo(Menu.Lobby, "Elimination");
				} else {
				sp.startLoading();
				new Thread(() -> m.transitionTo(Menu.EliminationSingle)).start();
				}
			}), new MenuOption("Capture The Flag", true, event -> {
			if (loc == GameLocation.MultiplayerServer) {
				m.transitionTo(Menu.Lobby, "CTF");
			} else {
				sp.startLoading();
				new Thread(() -> m.transitionTo(Menu.CTFSingle)).start();
			}
		}), new MenuOption("Back", false, event -> {
			if (loc == GameLocation.MultiplayerServer)
				m.exitClient();
			m.transitionTo(Menu.MainMenu);
		})};
		
		// Turn the collection of button options into a GridPane to be displayed
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		mainGrid.add(grid, 0, 0);

		// Create the scene and return it
		m.addButtonHoverSounds(grid);

		Scene s = new Scene(sp, m.width, m.height);
		s.getStylesheets().add("styles/menu.css");
		s.getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
		return s;
	}
}
