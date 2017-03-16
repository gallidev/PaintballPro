package gui;

import audio.AudioManager;
import audio.SFXResources;
import enums.GameLocation;
import enums.Menu;
import enums.TeamEnum;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import networking.client.Client;
import networking.client.ClientReceiver;
import networking.client.ClientSender;
import networking.server.Server;
import rendering.Renderer;

import java.util.ArrayList;

/**
 * Class to manage displaying of views (scenes) in the GUI, and the user's settings
 */
public class GUIManager {

    // Local server code toggle
    private static boolean localServerCode = false;

    // Renderer
    public static Renderer renderer;
    private ArrayList<GameObserver> gameObservers = new ArrayList<>();

    // Settings
    private static UserSettings user = UserSettingsManager.loadSettings();
    private ArrayList<UserSettingsObserver> settingsObservers = new ArrayList<>();

    // Scene
    public final double width;
    public final double height;
    private Stage s;

    // Networking
    public int udpPortNumber = 0;
    private int tcpPortNumber = 25566;
    private String ipAddress = "";
    private Client c;
    private Thread localServer;

    // GUI
    private Menu currentScene = Menu.MainMenu;
    private ObservableList<GameLobbyRow> lobbyData = FXCollections.observableArrayList();
    private boolean lobbyTimerStarted = false;
    private int lobbyTimeLeft = 10;

    // Audio
    private AudioManager audio;

    /**
     * Constructor for GUIManager
     */
    public GUIManager() {
        audio = new AudioManager(user, this);
        String[] resolution = GUIManager.getUserSettings().getResolution().split("x");
        width = Double.parseDouble(resolution[0]);
        height = Double.parseDouble(resolution[1]);
    }

    /**
     * Get the user settings object
     *
     * @return user settings
     */
    public static UserSettings getUserSettings() {
        return user;
    }

    /**
     * Method for changing the current scene two switch between views
     *
     * @param menu the string representation of the menu to switch to
     * @param o    objects to be passed to the target scene
     */
    public void transitionTo(Menu menu, Object... o) {
        audio.stopMusic();
        if (!menu.equals(currentScene)) {
            currentScene = menu;
            switch (menu) {
                case MainMenu:
                    renderer.destroy();
                    renderer = null;
                    if (localServer != null) {
                        localServer.interrupt();
                        localServer = null;
                    }
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
                    //if (localServerCode) establishLocalServerConnection();
                    s.setScene(GameTypeMenu.getScene(this, GameLocation.SingleplayerLocal));
                    break;
                case Lobby:
                    lobbyTimerStarted = false;
                    if (o[0] instanceof String) {
                        if (o[0].equals("CTF")) {
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
                    if (localServerCode) {
                    	//establishLocalServerConnection();

                        c.getSender().sendMessage("Play:Mode:1");
                        audio.startMusic(audio.music.getRandomTrack());
                        renderer = new Renderer("elimination", c.getReceiver(), this, null);
                        s.setScene(renderer);
                    } else {
                        audio.startMusic(audio.music.getRandomTrack());
                        renderer = new Renderer("elimination", this);
                        s.setScene(renderer);
                    }
                    break;
                case EliminationMulti:
                    audio.startMusic(audio.music.getRandomTrack());
                    //renderer = new Renderer("elimination", c.getReceiver(), this);
                    s.setScene(renderer);
                    break;
                case CTFSingle:
                    if (localServerCode) {
                    	establishLocalServerConnection();
                        c.getSender().sendMessage("Play:Mode:2");
                        audio.startMusic(audio.music.getRandomTrack());
                        renderer = new Renderer("ctf", c.getReceiver(), this, null);
                        s.setScene(renderer);
                    } else {
                        audio.startMusic(audio.music.getRandomTrack());
                        renderer = new Renderer("ctf", this);
                        Platform.runLater(() -> {
                            s.setScene(renderer);
                        });
                    }
                    break;
                case CTFMulti:
                    audio.startMusic(audio.music.getRandomTrack());
                    //renderer = new Renderer("ctf", c.getReceiver(), this);
                    s.setScene(renderer);
                    break;
                case EndGame:
                    s.setScene(EndGameMenu.getScene(this, (String)o[0], (TeamEnum)o[1]));
                    break;
                default:
                    throw new RuntimeException("Menu '" + menu + "' is not a valid transition");
            }
        }
    }

    /**
     * Start a local server and connect to it (for single player)
     * @return true if the connection was established
     */
    private boolean establishLocalServerConnection() {
        if (localServerCode) {
            ipAddress = "127.0.0.1";
            Server local = new Server(tcpPortNumber,ipAddress,new ServerConsole(), 0);

            local.setSinglePlayer(true);

            localServer = new Thread(new Runnable() {
                @Override
                public void run() {
                    local.start();
                }
            });
            localServer.start();
            try {
                Thread.sleep(1000);
                boolean b = establishConnection();
                Thread.sleep(1000);
                return b;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Connect to a remote server
     * @return true if the connection was established
     */
    public boolean establishConnection() {
            String nickname = user.getUsername();
            System.out.println("Connecting to: " + ipAddress + ":" + tcpPortNumber);
            try {
                c = new Client(nickname, tcpPortNumber, ipAddress, this, udpPortNumber, false);
                return true;
            } catch (Exception e) {
                return false;
            }
    }

    /**
     * Get the stage for the GUI
     * @return stage for the GUI
     */
    public Stage getStage()
    {
        return s;
    }

    /**
     * Set the stage for the GUI
     * @param primaryStage stage to use for displaying the GUI
     */
    public void setStage(Stage primaryStage) {
        s = primaryStage;
        s.setTitle("Paintball Pro");
        s.setScene(MainMenu.getScene(this));
        s.show();
    }

    /**
     * Add an observer for the settings, to be notified when the settings change
     * @param obs observer
     */
    public void addSettingsObserver(UserSettingsObserver obs) {
        settingsObservers.add(obs);
    }

    /**
     * Notify observers that the settings have changed
     */
    public void notifySettingsObservers() {
        for (UserSettingsObserver obs : settingsObservers) {
            obs.settingsChanged();
        }
    }

    /**
     * Get the audio manager
     * @return audio manager
     */
    public AudioManager getAudioManager() {
        return audio;
    }

    /**
     * Request an updated lobby from the server
     */
    void fetchLobbyUpdates() {
        if (c != null) {
            c.getSender().sendMessage("Get:Red");
            c.getSender().sendMessage("Get:Blue");
        }
    }

    /**
     * Update the display of red players in the lobby
     * @param redPlayers array of player names (strings)
     */
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

    /**
     * Update the display of blue players in the lobby
     * @param bluePlayers array of player names (strings)
     */
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

    /**
     * Get the client
     * @return client
     */
    public Client getClient() {
        return c;
    }

    /**
     * Start the timer for the lobby
     */
    public void setTimerStarted() {
        lobbyTimerStarted = true;
    }

    /**
     * Is the lobby timer running
     * @return lobby timer started
     */
    public boolean isTimerStarted() {
        return lobbyTimerStarted;
    }

    /**
     * Get the time left for the lobby
     * @return lobby
     */
    public int getTimeLeft() {
        return lobbyTimeLeft;
    }

    /**
     * Set the time left for the lobby
     * @param timeLeft time left in seconds
     */
    public void setTimeLeft(int timeLeft) {
        this.lobbyTimeLeft = timeLeft;
    }

    /**
     * Set the IP address to use for connections
     * @param ipAddress IP address for connections
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Add hover and click buttons to all buttons in a node
     * @param n parent node to start from
     */
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
                    audio.playSFX(new SFXResources().clickSound, (float)0.5);
                }
            });
            n.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    audio.playSFX(new SFXResources().getRandomPaintball(), (float)0.5);
                }
            });
        }
    }

    /**
     * Set the renderer to use for the games
     * @param r renderer for the game
     */
    public void setRenderer(Renderer r){
    	renderer = r;
    }

    /**
     * Send a message to the server about the client disconnecting
     */
    public void exitClient() {
        if (this.getClient() != null && this.getClient().getSender() != null) {
            this.getClient().getSender().sendMessage("Exit:Client");
        }
    }

}
