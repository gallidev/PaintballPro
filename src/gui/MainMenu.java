package gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

/**
 * Main Menu scene class
 */
public class MainMenu {

	/**
	 * Return a main menu scene for a given GUI manager
	 * @param m GUI manager to use
	 * @return main menu scene
	 */
	public static Scene getScene(GUIManager m) {
		
		// Create a set of button options, with each button's title and event handler
		MenuOption[] set = {new MenuOption("Single player", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo("Singleplayer", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Multiplayer", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.establishConnection();
		    	m.transitionTo("Multiplayer", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Settings", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo("Settings", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		})};
		
		// Turn the collection of button options into a GridPane to be displayed
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		
		// Create the scene and return it
		return new Scene(grid, m.width, m.height);
	}
}
