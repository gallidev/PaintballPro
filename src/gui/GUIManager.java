package gui;

import java.util.ArrayList;

import audio.AudioManager;
import enums.GameLocation;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	private ObservableList<GameLobbyRow> lobbyData = FXCollections.observableArrayList(new GameLobbyRow("Test", "User"));
	
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
				s.setScene(GameLobbyMenu.getScene(this, lobbyData));
				break;
			case "Elimination":
				s.setScene(new Renderer("elimination", audio));
				break;
		}
	}
	
	public void establishConnection() {
		String nickname = user.getUsername(); // We ask the user what their nickname is.
		int portNumber = 25566; // The server is on a particular port.
		String machName = ""; // The machine has a particular name.

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// This loads up the client code.
				c = new Client(nickname,portNumber,machName);

				// We can then get the client sender and receiver threads.
				ClientSender sender = c.getSender();
				ClientReceiver receiver = c.getReceiver();
			}
		});
		t.start();
	}


	public void setStage(Stage primaryStage) throws Exception {
		// TODO: Remove this method once integrated with Game.java
		s = primaryStage;
		s.setTitle("Paintball Pro");
		s.setScene(NicknameMenu.getScene(this));
		s.show();
	}
	
	public void addSettingsObserver(UserSettingsObserver obs) {
		settingsObservers.add(obs);
	}
	
	public void notifySettingsObservers() {
		// TODO: notify server that username has changed
		for (UserSettingsObserver obs: settingsObservers) {
			obs.settingsChanged();
		}
	}

	public AudioManager getAudioManager() {
		return audio;
	}

	public ObservableList<GameLobbyRow> getLobbyData() {
		return lobbyData;
	}

	public void fetchLobbyUpdates() {
		if (c != null) {
			c.getSender().sendMessage("Get:Red");
			c.getSender().sendMessage("Get:Blue");
		}
	}
}
