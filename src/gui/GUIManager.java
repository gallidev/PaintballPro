package gui;

import audio.AudioManager;
import audio.SFXResources;
import enums.GameLocation;
import enums.MenuEnum;
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

    public static boolean localServerCode = false;
    // Load the user's settings
    // When set methods are called for this class/object, the class will
    // automatically save the changed preferences
    private static UserSettings user = UserSettingsManager.loadSettings();
    // Set the width and height of the stage
    public final int width = 1024;
    public final int height = 576;
    private Stage s;
    private Client c;
    private Thread localServer;
    private MenuEnum currentScene = MenuEnum.MainMenu;
    private String ipAddress = "";
    private Renderer r = null;
    private ObservableList<GameLobbyRow> lobbyData = FXCollections.observableArrayList();
    private boolean lobbyTimerStarted = false;
    private int lobbyTimeLeft = 10;
    private ArrayList<UserSettingsObserver> settingsObservers = new ArrayList<>();
    private ArrayList<GameObserver> gameObservers = new ArrayList<>();
    private int gameTimeLeft = 0;
    private int redScore = 0;
    private int blueScore = 0;
    private AudioManager audio;

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
                    r = null;
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
                    if (localServerCode) establishLocalServerConnection();
                    s.setScene(GameTypeMenu.getScene(this, GameLocation.SingleplayerLocal));
                    break;
                case Lobby:
                    lobbyTimerStarted = false;
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
                    if (localServerCode) {
                        c.getSender().sendMessage("Play:Mode:1");
                        audio.startMusic(audio.music.track1);
                        r = new Renderer("elimination", c.getReceiver(), this);
                        s.setScene(r);
                    } else {
                        audio.startMusic(audio.music.track1);
                        r = new Renderer("elimination", this);
                        s.setScene(r);
                    }
                    break;
                case EliminationMulti:
                    audio.startMusic(audio.music.track1);
                    r = new Renderer("elimination", c.getReceiver(), this);
                    s.setScene(r);
                    break;
                case CTFSingle:
                    if (localServerCode) {
                        c.getSender().sendMessage("Play:Mode:2");
                        audio.startMusic(audio.music.track1);
                        r = new Renderer("ctf", c.getReceiver(), this);
                        s.setScene(r);
                    } else {
                        audio.startMusic(audio.music.track1);
                        r = new Renderer("ctf", this);
                        s.setScene(r);
                    }
                    break;
                case CTFMulti:
                    audio.startMusic(audio.music.track1);
                    r = new Renderer("ctf", c.getReceiver(), this);
                    s.setScene(r);
                    break;
                case EndGame:
                    s.setScene(EndGameMenu.getScene(this));
                    break;
                default:
                    throw new RuntimeException("Menu '" + menu + "' is not a valid transition");
            }
        }
    }

    private boolean establishLocalServerConnection() {
        if (localServerCode) {
            ipAddress = "0.0.0.0";
            localServer = new Thread(new Runnable() {
                @Override
                public void run() {
                    int portNo = 25566;
                    String[] serverArgs = {portNo + "", ipAddress};
                    Server.main(serverArgs, new ServerConsole());
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

    public boolean establishConnection() {
            String nickname = user.getUsername(); // We ask the user what their nickname is.

		    String serverLocation = ipAddress + ":25566";

            int portNumber = Integer.parseInt(serverLocation.split(":")[1]); // The server is on a particular port.
            String machName = serverLocation.split(":")[0]; // The machine has a particular name.

            // This loads up the client code.
            try {
                c = new Client(nickname, portNumber, machName, this);

                // We can then get the client sender and receiver threads.
                ClientSender sender = c.getSender();
                ClientReceiver receiver = c.getReceiver();
                return true;
            } catch (Exception e) {
                return false;
            }
    }

    public Stage getStage()
    {
        return s;
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
        lobbyTimerStarted = true;
    }

    public boolean isTimerStarted() {
        return lobbyTimerStarted;
    }

    public MenuEnum getCurrentScene() {
        return currentScene;
    }

    public int getTimeLeft() {
        return lobbyTimeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.lobbyTimeLeft = timeLeft;
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

    public void addGameObserver(GameObserver obs) {
        this.gameObservers.add(obs);
    }

    private void notifyGameChanged() {
        this.gameObservers.forEach(obs -> obs.gameUpdated());
    }

    public void setGameTimeLeft(int gameTimeLeft) {
        this.gameTimeLeft = gameTimeLeft;
        notifyGameChanged();
    }

    public void setRedScore(int redScore) {
        this.redScore = redScore;
        notifyGameChanged();
    }

    public void setBlueScore(int blueScore) {
        this.blueScore = blueScore;
        notifyGameChanged();
    }
}
