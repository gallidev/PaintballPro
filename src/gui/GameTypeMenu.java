package gui;

import enums.GameLocation;
import enums.Menu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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

		StackPane sp = new StackPane();
		GridPane loadingGrid = new GridPane();
		loadingGrid.setAlignment(Pos.CENTER);
		loadingGrid.setHgap(10);
		loadingGrid.setVgap(10);
		loadingGrid.setPadding(new Insets(25, 25, 25, 25));
		ProgressIndicator spinner = new ProgressIndicator();
		spinner.setProgress(-1);
		loadingGrid.add(MenuControls.centreInPane(spinner), 0, 0);

		MenuOption[] empty = {};
		GridPane mainGrid = MenuOptionSet.optionSetToGridPane(empty);


		// Create a set of button options, with each button's title and event handler
		MenuOption[] set = {new MenuOption("Elimination", true, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	if (loc == GameLocation.MultiplayerServer) {
					m.transitionTo(Menu.Lobby, "Elimination");
				} else {
					m.transitionTo(Menu.EliminationSingle);
				}
			}
		}), new MenuOption("Capture The Flag", true, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				if (loc == GameLocation.MultiplayerServer) {
					m.transitionTo(Menu.Lobby, "CTF");
				} else {

					sp.getChildren().remove(mainGrid);
					sp.getChildren().add(loadingGrid);
					System.out.println(sp.getChildren().toString());

					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {

							m.transitionTo(Menu.CTFSingle);

						}
					});
					t.start();

				}

			}
		}), new MenuOption("Back", false, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	if (loc == GameLocation.MultiplayerServer)
		    		m.exitClient();
		    	m.transitionTo(Menu.MainMenu);
		    }
		})};
		
		// Turn the collection of button options into a GridPane to be displayed
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		mainGrid.add(grid, 0, 0);

		// Create the scene and return it
		m.addButtonHoverSounds(grid);
		sp.getChildren().add(mainGrid);

		Scene s = new Scene(sp, m.width, m.height);
		s.getStylesheets().add("styles/menu.css");
		s.getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
		return s;
	}
}
