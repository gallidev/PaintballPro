package gui;
import enums.MenuEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import javafx.scene.image.ImageView;

import javax.swing.*;
import java.util.Set;

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

//		JOptionPane.showMessageDialog(null, "Cannot find any LAN servers running.", "No LAN server.", JOptionPane.ERROR_MESSAGE);

		Image i = new Image("assets/paintballlogo.png");
		ImageView iv = new ImageView(i);
		iv.setId("logo");
		iv.setPreserveRatio(true);
		iv.setFitWidth(400);
		
		// Create a set of button options, with each button's title and event handler
		MenuOption[] set = {new MenuOption("Single player", true, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo(MenuEnum.SingleplayerGameType, null);
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Multiplayer", true, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo(MenuEnum.NicknameServerConnection, null);
//		    	m.transitionTo("Multiplayer", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Settings", false, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo(MenuEnum.Settings, null);
		        System.out.println("ActionEvent: " + event);
		    }     
		})};
		
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
		m.addButtonHoverSounds(view);
		Scene s = new Scene(view, m.width, m.height);
		s.getStylesheets().add("styles/menu.css");
		return s;
	}
}
