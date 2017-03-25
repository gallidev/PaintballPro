package networking.client;

import enums.Menu;
import enums.TeamEnum;
import gui.GUIManager;
import integration.client.ClientGameStateReceiver;
import javafx.application.Platform;
import networking.game.UDPClient;
import physics.CollisionsHandler;
import physics.Flag;
import physics.Powerup;
import physics.PowerupType;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.GhostPlayer;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.
/**
 * Class to get messages from client, process and put appropriate message for a
 * client.
 *
 * @author Matthew Walters
 */
public class ClientReceiver extends Thread {

	private ArrayList<EssentialPlayer> myTeam;
	private ArrayList<EssentialPlayer> enemies;
	private boolean debug = false;
	private boolean singlePlayer;
	private BufferedReader fromServer;
	private ClientGameStateReceiver clientGameStateReceiver;
	private ClientPlayer cPlayer;
	private ClientSender sender;
	private GUIManager guiManager;
	private int clientID;
	private TeamTable teams;
	private UDPClient udpClient;

	/**
	 * Construct the class, setting passed variables to local objects.
	 *
	 * @param clientId
	 *            The ID of the client.
	 * @param reader
	 *            Input stream reader for data.
	 * @param sender
	 *            Sender class for sending messages to the client.
	 * @param guiManager
	 *            GUI manager to pop-up Alert Boxes to the Client.
	 * @param udpClient
	 *            UDP Game Client to transmit messages to UDP Server.
	 * @param teams
	 *            Friendly and Opposing Teams stored in an object.
	 */
	public ClientReceiver(int clientId, BufferedReader reader, ClientSender sender, GUIManager guiManager, UDPClient udpClient,
			TeamTable teams) {
		this.guiManager = guiManager;
		clientID = clientId;
		fromServer = reader;
		this.sender = sender;
		myTeam = new ArrayList<>();
		enemies = new ArrayList<>();
		this.udpClient = udpClient;
		this.teams = teams;
	}

	/**
	 * The main method running in this class, runs when the class is started
	 * after initialisation.
	 */
	public void run() {
		try {
			while (true) {
				// Get input from the client read stream.
				String text = fromServer.readLine();
				// If text isn't null and does not read "Exit:Client" do...
				if (text != null && text.compareTo("Exit:Client") != 0) {
					if (debug)
						System.out.println("Client receiver got: " + text);

					// UI Requests
					if (text.contains("Ret:Red:")) {
						String[] red = text.substring(8).split("-");
						guiManager.updateRedLobby(red);
					} else if (text.contains("Ret:Blue:")) {
						String[] blue = text.substring(9).split("-");
						guiManager.updateBlueLobby(blue);
					}

					// Lobby status
					else if (text.contains("TimerStart")) {
						if (debug)
							System.out.println("Timer Started");
						guiManager.setTimerStarted();
					} else if (text.contains("LTime:")) {
						String remTime = text.split(":")[1];
						int time = Integer.parseInt(remTime);
						guiManager.setTimeLeft(time);
						guiManager.setTimerStarted();
						if (debug)
							System.out.println("Lobby has " + time + " left");
					} else if (text.contains("Single")) {
						singlePlayer = true;
						if (debug)
							System.out.println("Single player: " + singlePlayer);
					}

					// Game actions
					switch (text.charAt(0)) {

					case '2':
						startGameAction(text);
						break;
					default:
						break;
					}

				} else // if the client wants to exit the system.
				{
					if (debug)
						System.out.println("Exiting now.");
					return;
				}
			}
		} catch (IOException e) {
			// If there is something wrong... exit cleanly.
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Method which creates everything on the client-side required to start a
	 * new game. It does this based on a string received from the server which
	 * contains the information about all the other players.
	 *
	 * This method also starts the renderer and updates the GUI manager.
	 *
	 * @param text
	 *            The protocol message containing all the start game
	 *            information.
	 *
	 * @author Alexandra Paduraru
	 * @author Filippo Galli
	 */
	public void startGameAction(String text) {
		// get all the relevant data from the message :
		// 2:<gameMode>:2:Red:1:Red:

		// Declarations
		String[] data = text.split(":");
		int gameMode = Integer.parseInt(data[1]);
		int powerUpIndex1 = Integer.parseInt(data[2]);
		int powerUpIndex2 = Integer.parseInt(data[3]);
		int flagIndex = Integer.parseInt(data[4]);

		String myNickname = data[6];
		String clientTeam = data[6];
		Map map = null;
		clientID = Integer.parseInt(data[5]);

		if (gameMode == 1)
			map = Map.loadRaw("elimination");
		else
			map = Map.loadRaw("ctf");

		CollisionsHandler collisionHandler = new CollisionsHandler(map);
		ArrayList<EssentialPlayer> players = new ArrayList<EssentialPlayer>(myTeam);
		Flag flag = new Flag();
		Powerup[] powerups = new Powerup[2];

		if (debug)
			System.out.println("Start game info : ");
		if (debug)
			System.out.println(Arrays.toString(data));

		// add myself to my team
		// create my client
		if (clientTeam.equals("Red"))
			cPlayer = new ClientPlayer(map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64,
					clientID, map.getSpawns(), TeamEnum.RED, guiManager, collisionHandler, null,
					ImageFactory.getPlayerImage(TeamEnum.RED), null, Renderer.TARGET_FPS);
		else
			cPlayer = new ClientPlayer(map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64,
					clientID, map.getSpawns(), TeamEnum.BLUE, guiManager, collisionHandler, null,
					ImageFactory.getPlayerImage(TeamEnum.BLUE), null, Renderer.TARGET_FPS);

		cPlayer.setNickname(myNickname);

		// extract the other members
		for (int i = 8; i < data.length - 2; i = i + 3) {

			int id = Integer.parseInt(data[i]);
			String nickname = data[i + 2];

			if (data[i + 1].equals(clientTeam)) {
				if (clientTeam.equals("Red")) {
					GhostPlayer p = new GhostPlayer(map.getSpawns()[myTeam.size()].x * 64,
							map.getSpawns()[myTeam.size()].y * 64, id, map.getSpawns(), TeamEnum.RED, collisionHandler,
							null, Renderer.TARGET_FPS);
					p.setNickname(nickname);
					myTeam.add(p);

					if (debug)
						System.out.println("Created player with nickname " + p.getNickname());
				} else {
					GhostPlayer p = new GhostPlayer(map.getSpawns()[myTeam.size()].x * 64,
							map.getSpawns()[myTeam.size()].y * 64, id, map.getSpawns(), TeamEnum.BLUE, collisionHandler,
							null, Renderer.TARGET_FPS);
					p.setNickname(nickname);
					myTeam.add(p);

					if (debug)
						System.out.println("Created player with nickname " + p.getNickname());
				}

			} else {
				if (clientTeam.equals("Red")) {
					GhostPlayer p = new GhostPlayer(map.getSpawns()[myTeam.size()].x * 64,
							map.getSpawns()[myTeam.size()].y * 64, id, map.getSpawns(), TeamEnum.BLUE, collisionHandler,
							null, Renderer.TARGET_FPS);
					p.setNickname(nickname);
					enemies.add(p);

					if (debug)
						System.out.println("Created player with nickname " + p.getNickname());

				} else {
					GhostPlayer p = new GhostPlayer(map.getSpawns()[myTeam.size()].x * 64,
							map.getSpawns()[myTeam.size()].y * 64, id, map.getSpawns(), TeamEnum.RED, collisionHandler,
							null, Renderer.TARGET_FPS);
					p.setNickname(nickname);
					enemies.add(p);

					if(debug)
						System.out.println("Created player with nickname " + p.getNickname());
				}
			}
		}

		players.addAll(enemies);
		players.add(cPlayer);
		collisionHandler.setPlayers(players);

		teams.setEnemies(enemies);
		teams.setMyTeam(myTeam);

		powerups[0] = new Powerup(PowerupType.SHIELD, map.getPowerupLocations());
		powerups[0].resetPosition(powerUpIndex1);
		powerups[1] = new Powerup(PowerupType.SPEED, map.getPowerupLocations());
		powerups[1].resetPosition(powerUpIndex2);

		if (gameMode == 1) {
			clientGameStateReceiver = new ClientGameStateReceiver(getAllPlayers(), cPlayer, powerups, guiManager.getAudioManager());
		} else {
			flag.setLocations(map.getFlagLocations());
			flag.resetPosition(flagIndex);
			clientGameStateReceiver = new ClientGameStateReceiver(getAllPlayers(), cPlayer, flag, powerups, guiManager.getAudioManager());
		}

		udpClient.setGameStateReceiver(clientGameStateReceiver);

		// for debugging
		if (debug)
		{
			System.out.println("game has started for player with ID " + clientID);
			System.out.println("game mode is " + gameMode);
			System.out.println("single player = " + singlePlayer);
		}

		// changing the scene
		if(guiManager.getClient() != null)
			Platform.runLater(() -> {
				if (!singlePlayer) {
					if (gameMode == 1) {
						guiManager.transitionTo(Menu.TEAM_MATCH_MULTIPLAYER);
					} else {
						guiManager.transitionTo(Menu.CAPTURE_THE_FLAG_MULTIPLAYER, flag);
					}
				} else {
					if (gameMode == 1) {
						guiManager.transitionTo(Menu.TEAM_MATCH_SINGLEPLAYER);
					} else {
						guiManager.transitionTo(Menu.CAPTURE_THE_FLAG_SINGLEPLAYER);
					}
				}
			});
	}

	/* Getters and setters */
	/**
	 * Returns the players that are in this Player's team.
	 *
	 * @return All the other players in the user's team, except himself.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<EssentialPlayer> getMyTeam() {
		return myTeam;
	}

	/**
	 * Return all the players that are not in this Player's team.
	 *
	 * @return All opponent players.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<EssentialPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * Return client sender object.
	 *
	 * @return Sender thread.
	 */
	public ClientSender getSender() {
		return sender;
	}

	/**
	 * Return client player instantiation of player.
	 * @return Client Player object.
	 */
	public ClientPlayer getClientPlayer() {
		return cPlayer;
	}

	/**
	 * Retrieve the UDP Client object from this class.
	 * @return UDP Client object.
	 */
	public UDPClient getUdpClient() {
		return udpClient;
	}

	/**
	 * Retreive the ClientGameStateReceiver object from this class.
	 * @return ClientGameStateReceiver object.
	 */
	public ClientGameStateReceiver getClientGameStateReceiver() {
		return clientGameStateReceiver;
	}

	/**
	 * Return all the players that are not in this Player's team.
	 *
	 * @return All opponent players.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<EssentialPlayer> getAllPlayers() {
		ArrayList<EssentialPlayer> allplayers = new ArrayList<EssentialPlayer>();
		allplayers.addAll(enemies);
		allplayers.addAll(myTeam);
		allplayers.add(cPlayer);
		return allplayers;
	}

	public void resetGame() {
		myTeam = new ArrayList<>();
		enemies = new ArrayList<>();
		teams = new TeamTable();
	}

}