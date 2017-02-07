package gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class MainMenu {
	// TODO: implement main menu scene


	public static Scene getScene(GUIManager m) {
		// TODO Auto-generated method stub
		System.out.println("Setting");
		
		MenuOption[] set = {new MenuOption("Single player", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Multiplayer", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Settings", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo("Settings", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		})};
		
		GridPane grid = MenuOptionSet.optionSetToGridPane(set);
		
		return new Scene(grid, m.width, m.height);
	}
}
