package gui;

import java.util.ArrayList;

import audio.AudioManager;
import enums.GameLocation;
import javafx.application.Application;
import javafx.stage.Stage;
import networkingClient.Client;
import networkingClient.ClientReceiver;
import networkingClient.ClientSender;
import rendering.Renderer;

/**
 * Class to manage displaying of views (scenes) in the GUI, and the user's settings
 */
public class GUIManager {
	
	private Stage s;
	private Client c;
	
	// Load the user's settings
	// When set methods are called for this class/object, the class will 
	// automatically save the changed preferences
	private UserSettings user = UserSettingsManager.loadSettings();
	private ArrayList<UserSettingsObserver> settingsObservers = new ArrayList<>();
	
	private AudioManager audio;
	
	// Set the width and height of the stage
	public final int width = 800;
	public final int height = 600;
	
	public GUIManager() {
		audio = new AudioManager(user, this);
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
			case "Lobby":
				establishConnection();
				s.setScene(GameLobbyMenu.getScene(this));
				break;
			case "Elimination":
				s.setScene(new Renderer("elimination"));
				break;
		}
	}
	
	public void establishConnection() {
		String nickname = user.getUsername(); // We ask the user what their nickname is.
		int portNumber = 25566; // The server is on a particular port.
		String machName = ""; // The machine has a particular name.
		
		// This loads up the client code.
		c = new Client(nickname,portNumber,machName);
		
		// We can then get the client sender and receiver threads.
		ClientSender sender = c.getSender();
		ClientReceiver receiver = c.getReceiver();
	}


	public void setStage(Stage primaryStage) throws Exception {
		// TODO: Remove this method once integrated with Game.java
		s = primaryStage;
		s.setTitle("Paintball Pro");
		s.setScene(MainMenu.getScene(this));
		s.show();
	}
	
	public void addSettingsObserver(UserSettingsObserver obs) {
		settingsObservers.add(obs);
	}
	
	public void notifySettingsObservers() {
		for (UserSettingsObserver obs: settingsObservers) {
			obs.settingsChanged();
		}
	}

}
