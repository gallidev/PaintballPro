package gui;

import enums.GameLocation;
import enums.MenuEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
		
		// Create a set of button options, with each button's title and event handler
		MenuOption[] set = {new MenuOption("Elimination", true, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	if (loc == GameLocation.MultiplayerServer) {
					m.transitionTo(MenuEnum.Lobby, "Elimination");
				} else {
					m.transitionTo(MenuEnum.EliminationSingle, null);
				}

		        System.out.println("ActionEvent: (Elimination) " + event);
		    }     
		}), new MenuOption("Capture The Flag", true, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				if (loc == GameLocation.MultiplayerServer) {
					m.transitionTo(MenuEnum.Lobby, "CTF");
				} else {
					m.transitionTo(MenuEnum.CTFSingle, null);
				}

				System.out.println("ActionEvent: (CTF) " + event);
			}
		}), new MenuOption("Back", false, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo(MenuEnum.MainMenu, null);
		        System.out.println("ActionEvent: " + event);
		    }     
		})};
		
		// Turn the collection of button options into a GridPane to be displayed
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		
		// Create the scene and return it
		m.addButtonHoverSounds(grid);
		Scene s = new Scene(grid, m.width, m.height);
		s.getStylesheets().add("styles/menu.css");
		return s;
	}
}
