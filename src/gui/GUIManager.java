package gui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import audio.AudioManager;
import audio.MusicResources;
import audio.SFXResources;
import enums.GameLocation;
import enums.MenuEnum;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import networkingClient.Client;
import networkingClient.ClientReceiver;
import networkingClient.ClientSender;
import networkingDiscovery.ServerAnnouncer;
import networkingServer.Server;
import rendering.Renderer;

/**
 * Class to manage displaying of views (scenes) in the GUI, and the user's settings
 */
public class GUIManager {

    private Stage s;
    private Client c;
    private MenuEnum currentScene = MenuEnum.MainMenu;
    private String ipAddress = "";

    private ObservableList<GameLobbyRow> lobbyData = FXCollections.observableArrayList();
    private boolean timerStarted = false;
    private int timeLeft = 10;

    // Load the user's settings
    // When set methods are called for this class/object, the class will
    // automatically save the changed preferences
    private static UserSettings user = UserSettingsManager.loadSettings();
    private ArrayList<UserSettingsObserver> settingsObservers = new ArrayList<>();

    private AudioManager audio;

    // Set the width and height of the stage
    public final int width = 800;
    public final int height = 600;

    public static final Image redPlayerImage = new Image("assets/characters/player_red.png", 30, 64, true, true);
    public static final Image bluePlayerImage = new Image("assets/characters/player_blue.png", 30, 64, true, true);

    public GUIManager() {
        audio = new AudioManager(user, this);
    }

    /**
     * Get the user settings object
     *
     * @return user settings object
     */
    public static UserSettings getUserSettings() {
        return user;
    }

    /**
     * Method for changing the current scene two switch between views
     *
     * @param menu the string representation of the menu to switch to
     * @param o    an object to be passed to the target scene (usually null)
     */
    public void transitionTo(MenuEnum menu, Object o) {
        audio.stopMusic();
        if (!menu.equals(currentScene)) {
            currentScene = menu;
            switch (menu) {
                case MainMenu:
                    s.setScene(MainMenu.getScene(this));
                    break;
                case NicknameServerConnection:
                    s.setScene(NicknameServerSelectMenu.getScene(this));
                    break;
                case Settings:
                    s.setScene(SettingsMenu.getScene(this));
                    break;
                case MultiplayerGameType:
                    s.setScene(GameTypeMenu.getScene(this, GameLocation.MultiplayerServer));
                    break;
                case SingleplayerGameType:
                    s.setScene(GameTypeMenu.getScene(this, GameLocation.SingleplayerLocal));
                    break;
                case Lobby:
                    timerStarted = false;
                    if (o instanceof String) {
                        if (o.equals("CTF")) {
                            c.getSender().sendMessage("Play:Mode:2");
                        } else {
                            c.getSender().sendMessage("Play:Mode:1");
                        }
                    } else {
                        c.getSender().sendMessage("Play:Mode:1");
                    }
                    s.setScene(GameLobbyMenu.getScene(this, lobbyData));
                    break;
                case EliminationSingle:
                    establishLocalSingleServerConnection();
                    audio.startMusic(MusicResources.track1);
                    s.setScene(new Renderer("elimination", audio));
                    break;
                case EliminationMulti:
                    audio.startMusic(MusicResources.track1);
                    s.setScene(new Renderer("elimination", c.getReceiver()));
                    break;
                case CTFSingle:
                    establishLocalSingleServerConnection();
                    audio.startMusic(MusicResources.track1);
                    s.setScene(new Renderer("ctf", audio));
                    break;
                case CTFMulti:
                    audio.startMusic(MusicResources.track1);
                    s.setScene(new Renderer("ctf", c.getReceiver()));
                    break;
                case EndGame:
                    s.setScene(EndGameMenu.getScene(this));
                    break;
                default:
                    throw new RuntimeException("Menu '" + menu + "' is not a valid transition");
            }
        }
    }

    private void establishLocalSingleServerConnection() {
        ipAddress = "0.0.0.0";
        (new Thread(new Runnable() {
            @Override
            public void run() {
                int portNo = 25566;
                String[] serverArgs = {portNo + "", ipAddress};
                Server.main(serverArgs);
            }
        })).start();
        establishConnection();
    }

    public void establishConnection() {
        if (c == null) {
            String nickname = user.getUsername(); // We ask the user what their nickname is.

		    String serverLocation = ipAddress + ":25566";

            int portNumber = Integer.parseInt(serverLocation.split(":")[1]); // The server is on a particular port.
            String machName = serverLocation.split(":")[0]; // The machine has a particular name.

            // This loads up the client code.
            c = new Client(nickname, portNumber, machName, this);

            // We can then get the client sender and receiver threads.
            ClientSender sender = c.getSender();
            ClientReceiver receiver = c.getReceiver();
        }
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
        // TODO: notify server that username has changed
        for (UserSettingsObserver obs : settingsObservers) {
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

    public void updateRedLobby(String[] redPlayers) {
        // Update all rows
        for (int i = 0; i < lobbyData.size(); i++) {
            String redName = "";
            if (i < redPlayers.length) {
                redName = redPlayers[i];
            }
            GameLobbyRow row = new GameLobbyRow(redName, lobbyData.get(i).getBlueName());
            lobbyData.set(i, row);
        }

        // Add in any new rows
        for (int i = lobbyData.size(); i < redPlayers.length; i++) {
            GameLobbyRow row = new GameLobbyRow(redPlayers[i], "");
            lobbyData.add(row);
        }
    }

    public void updateBlueLobby(String[] bluePlayers) {
        // Update all rows
        for (int i = 0; i < lobbyData.size(); i++) {
            String blueName = "";
            if (i < bluePlayers.length) {
                blueName = bluePlayers[i];
            }
            GameLobbyRow row = new GameLobbyRow(lobbyData.get(i).getRedName(), blueName);
            lobbyData.set(i, row);
        }

        // Add in any new rows
        for (int i = lobbyData.size(); i < bluePlayers.length; i++) {
            GameLobbyRow row = new GameLobbyRow("", bluePlayers[i]);
            lobbyData.add(row);
        }
    }

    public Client getClient() {
        return c;
    }

    public void setTimerStarted() {
        timerStarted = true;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public MenuEnum getCurrentScene() {
        return currentScene;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void addButtonHoverSounds(Node n) {
        if (n instanceof Pane) {
            for (Node p : ((Pane) n).getChildren()) {
                addButtonHoverSounds(p);
            }
        }
        if (n instanceof Button) {
            n.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    audio.playSFX(new SFXResources().clickSound, (float)1.0);
                }
            });
            n.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    audio.playSFX(new SFXResources().getRandomPaintball(), (float)1.0);
                }
            });
        }
    }
}
