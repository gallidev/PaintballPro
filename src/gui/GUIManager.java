package gui;

import enums.GameLocation;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class to manage displaying of views (scenes) in the GUI, and the user's settings
 */
public class GUIManager extends Application {
	
	private Stage s;
	
	// Load the user's settings
	// When set methods are called for this class/object, the class will 
	// automatically save the changed preferences
	private UserSettings user = UserSettingsManager.loadSettings();
	
	// Set the width and height of the stage
	public final int width = 800;
	public final int height = 600;
	
	public static void main(String[] args) {
		// TODO: Remove this method once integrated with Game.java
		launch(args);
	}
	
	/**
	 * Get the user settings object
	 * @return user settings object
	 */
	public UserSettings getUserSettings() {
		return user;
	}

	/**
	 * Method for changing the current scene two switch between views
	 * @param menu the string representation of the menu to switch to
	 * @param o an object to be passed to the target scene (usually null)
	 */
	public void transitionTo(String menu, Object o) {
		switch (menu) {
			case "Main":
				s.setScene(MainMenu.getScene(this));
				break;
			case "Settings":
				s.setScene(SettingsMenu.getScene(this));
				break;
			case "Multiplayer":
				s.setScene(GameTypeMenu.getScene(this, GameLocation.MultiplayerServer));
				break;
			case "Singleplayer":
				s.setScene(GameTypeMenu.getScene(this, GameLocation.SingleplayerLocal));
				break;
		}
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO: Remove this method once integrated with Game.java
		s = primaryStage;
		s.setTitle("Paintball Pro");
		s.setScene(MainMenu.getScene(this));
		s.show();
	}

}
