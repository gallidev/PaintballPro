package networking.client;

import enums.Menu;
import enums.TeamEnum;
import gui.GUIManager;
import integrationClient.ClientGameStateReceiver;
import integrationClient.ClientInputSender;
import javafx.application.Platform;
import networking.game.UDPClient;
import physics.CollisionsHandler;
import players.ClientLocalPlayer;
import players.GeneralPlayer;
import players.GhostPlayer;
import players.PhysicsClientPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.
/**
 * Class to get messages from client, process and put appropriate message for a
 * client.
 *
 * @author MattW
 */
public class ClientReceiver extends Thread {
	private int clientID;
	private BufferedReader fromServer;
	private ClientSender sender;
	private GUIManager m;
	private GhostPlayer cPlayer;
	private ArrayList<GhostPlayer> myTeam;
	private ArrayList<GhostPlayer> enemies;
	private ClientGameStateReceiver actionReceiver;
	private UDPClient udpClient;
	private TeamTable teams;

	private boolean debug = false;

	/**
	 * Construct the class, setting passed variables to local objects.
	 *
	 * @param Cid
	 *            The ID of the client.
	 * @param reader
	 *            Input stream reader for data.
	 * @param sender
	 *            Sender class for sending messages to the client.
	 */
	public ClientReceiver(int Cid, BufferedReader reader, ClientSender sender, GUIManager m, UDPClient udpClient, TeamTable teams) {
		this.m = m;
		clientID = Cid;
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
					if(debug) System.out.println("Received: " + text);

					// UI Requests
					if (text.contains("Ret:Red:")) {
						if(debug) System.out.println("Got red");
						String[] red = text.substring(8).split("-");
						m.updateRedLobby(red);
					}
					else if (text.contains("Ret:Blue:")) {
						if(debug) System.out.println("Got blue");
						String[] blue = text.substring(9).split("-");
						m.updateBlueLobby(blue);
					}
					else if (text.contains("Ret:Username:")) {
						// do nothing.
					}

					// Lobby status
					else if (text.contains("TimerStart")) {
						if(debug) System.out.println("Timer Started");
						// Do stuff here, we have 10 secs till game start
						// message sent.
						m.setTimerStarted();
					}

					else if (text.contains("LTime:")) {
						String remTime = text.split(":")[1];
						int time = Integer.parseInt(remTime);
						m.setTimeLeft(time);
						m.setTimerStarted();
						if(debug) System.out.println("Lobby has " + time + " left");
					}

					// Game status
					else if (text.contains("StartGame"))
						startGameAction(text);

					else if (text.contains("EndGame")) {
						if(debug) System.out.println("Game has ended for player with ID " + clientID);
						// Get data about scores, and pass into transition
						// method
						int someScore = 0;
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								m.transitionTo(Menu.EndGame, someScore);
							}

						});
					}

					//*===================== !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!========================================
					//							NEW INTEGRATION BELOW

					//initialize the ClientActionReceiver!!!!
					//do stuff here according to new protocols for actions that update the client-sided player

					switch(text.charAt(0)){
						case '2' : startGameAction(text);
								   break;
					}

				} else // if the client wants to exit the system.
				{
					sender.stopThread();
					return;
				}
			}
		} catch (IOException e) {
			// If there is something wrong... exit cleanly.
			sender.stopThread();
			return;
		}
	}

	//*===================== !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!========================================
	//							NEW INTEGRATION BELOW

	public void startGameAction(String text) {
		// get all the relevant data from the message : StartGame:2:Red:1:Red:
		String[] data = text.split(":");

		clientID = Integer.parseInt(data[1]);
		String clientTeam = data[2];
		Map map = Map.loadRaw("elimination");

		// add myself to my team
		// create my client
		if (clientTeam.equals("Red"))
			cPlayer = new GhostPlayer( map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64, clientID, ImageFactory.getPlayerImage(TeamEnum.RED),null);
		else
			cPlayer = new GhostPlayer( map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64, clientID, ImageFactory.getPlayerImage(TeamEnum.BLUE),null);
		// extract the other members
		for (int i = 3; i < data.length - 1; i = i + 2) {
			int id = Integer.parseInt(data[i]);
			if (data[i + 1].equals(clientTeam)) {
				if (clientTeam.equals("Red"))
					myTeam.add(new GhostPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id,
							ImageFactory.getPlayerImage(TeamEnum.RED), null));
				else
					myTeam.add(new GhostPlayer(map.getSpawns()[id + 3].x * 64, map.getSpawns()[id + 3].y * 64, id,
							ImageFactory.getPlayerImage(TeamEnum.BLUE), null));
			} else {
				if (clientTeam.equals("Red"))
					enemies.add(new GhostPlayer(map.getSpawns()[id + 3].x * 64, map.getSpawns()[id + 3].y * 64, id,
							ImageFactory.getPlayerImage(TeamEnum.BLUE), null));
				else
					enemies.add(new GhostPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id,
							ImageFactory.getPlayerImage(TeamEnum.RED), null));
			}
		}

		teams.setEnemies(enemies);
		teams.setMyTeam(myTeam);

		ClientGameStateReceiver gameStateReceiver = new ClientGameStateReceiver(getAllPlayers());
		udpClient.setGameStateReceiver(gameStateReceiver);
		System.out.println("initialised GameStateReceiver");

		// for debugging
		if(debug) System.out.println("game has started for player with ID " + clientID);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				m.transitionTo(Menu.EliminationMulti, null);
			}
		});
	}







	// Different actions to handle the server messages
	/**
	 * Contains everything that needs to be done when a player receives the
	 * start signal: take the client's id and team, then form the team and the
	 * enemy team. This information is then used by the renderer.
	 *
	 * @param text
	 *            The text received from the server.
	 *
	 * @author Alexandra Paduraru
	 */
//	public void startGameAction(String text) {
//		// get all the relevant data from the message : StartGame:2:Red:1:Red:
//		String[] data = text.split(":");
//
//		clientID = Integer.parseInt(data[1]);
//		String clientTeam = data[2];
//		Map map = Map.loadRaw("elimination");
//
//		// add myself to my team
//		// create my client
//		if (clientTeam.equals("Red"))
//			cPlayer = new GhostPlayer( map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64, clientID, ImageFactory.getPlayerImage(TeamEnum.RED),null);
//		else
//			cPlayer = new GhostPlayer( map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64, clientID, ImageFactory.getPlayerImage(TeamEnum.BLUE),null);
//
//		// extract the other members
//		for (int i = 3; i < data.length - 1; i = i + 2) {
//			int id = Integer.parseInt(data[i]);
//			if (data[i + 1].equals(clientTeam)) {
//				if (clientTeam.equals("Red"))
//					myTeam.add(new GhostPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id,
//							ImageFactory.getPlayerImage(TeamEnum.RED), null));
//				else
//					myTeam.add(new GhostPlayer(map.getSpawns()[id + 3].x * 64, map.getSpawns()[id + 3].y * 64, id,
//							ImageFactory.getPlayerImage(TeamEnum.BLUE), null));
//			} else {
//				if (clientTeam.equals("Red"))
//					enemies.add(new GhostPlayer(map.getSpawns()[id + 3].x * 64, map.getSpawns()[id + 3].y * 64, id,
//							ImageFactory.getPlayerImage(TeamEnum.BLUE), null));
//				else
//					enemies.add(new GhostPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id,
//							ImageFactory.getPlayerImage(TeamEnum.RED), null));
//			}
//		}
//
//		teams.setEnemies(enemies);
//		teams.setMyTeam(myTeam);
//
//
//		actionReceiver = new ClientActionReceiver(getAllPlayers());
//
//
//		// for debugging
//		if(debug) System.out.println("game has started for player with ID " + clientID);
//
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				m.transitionTo(MenuEnum.EliminationMulti, null);
//			}
//		});
//	}

	/* Getters and setters */
	/**
	 * Returns the players that are in this Player's team.
	 *
	 * @return All the other players in the user's team, except himself.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<GhostPlayer> getMyTeam() {
		return myTeam;
	}

	/**
	 * Return all the players that are not in this Player's team.
	 *
	 * @return All opponent players.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<GhostPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * Return client sender object.
	 * @return Sender thread.
	 */
	public ClientSender getSender() {
		return sender;
	}

	/**
	 * Return client player instantiation of player.
	 * @return Client Player object.
	 */
	public GhostPlayer getClientPlayer() {
		return cPlayer;
	}

	public UDPClient getUdpClient(){
		return udpClient;
	}
	/**
	 * Return all the players that are not in this Player's team.
	 *
	 * @return All opponent players.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<GhostPlayer> getAllPlayers() {
		ArrayList<GhostPlayer> allplayers = new ArrayList<GhostPlayer>();
		allplayers.addAll(enemies);
		allplayers.addAll(myTeam);
		allplayers.add(cPlayer);
		return allplayers;
	}


}