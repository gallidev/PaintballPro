package networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import enums.MenuEnum;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.application.Platform;
import networking.game.UDPClientReceiver;
import networking.shared.Message;
import networking.shared.MessageQueue;
import physics.CollisionsHandler;
import players.ClientLocalPlayer;
import players.GeneralPlayer;
import players.PhysicsClientPlayer;
import rendering.Map;

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
	private PhysicsClientPlayer cPlayer;
	private ArrayList<ClientLocalPlayer> myTeam;
	private ArrayList<ClientLocalPlayer> enemies;
	private UDPClientReceiver udpReceiver;
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
	public ClientReceiver(int Cid, BufferedReader reader, ClientSender sender, GUIManager m, UDPClientReceiver udpReceiver, TeamTable teams) {
		this.m = m;
		clientID = Cid;
		fromServer = reader;
		this.sender = sender;
		myTeam = new ArrayList<>();
		enemies = new ArrayList<>();
		this.udpReceiver = udpReceiver;
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
								m.transitionTo(MenuEnum.EndGame, someScore);
							}

						});
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
	public void startGameAction(String text) {
		// get all the relevant data from the message : StartGame:2:Red:1:Red:
		String[] data = text.split(":");

		clientID = Integer.parseInt(data[1]);
		String clientTeam = data[2];
		Map map = Map.loadRaw("elimination");

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		// add myself to my team
		// create my client
		if (clientTeam.equals("Red"))
			cPlayer = new PhysicsClientPlayer(map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64,
					clientID, false, map, m.getAudioManager(), TeamEnum.RED, udpReceiver, collisionsHandler);
		else
			cPlayer = new PhysicsClientPlayer(map.getSpawns()[clientID + 3].x * 64, map.getSpawns()[clientID + 3].y * 64,
					clientID, false, map, m.getAudioManager(), TeamEnum.BLUE, udpReceiver,collisionsHandler);

		ArrayList<GeneralPlayer> allplayers = new ArrayList<GeneralPlayer>();
		// extract the other members
		for (int i = 3; i < data.length - 1; i = i + 2) {
			int id = Integer.parseInt(data[i]);
			if (data[i + 1].equals(clientTeam)) {
				if (clientTeam.equals("Red"))
					myTeam.add(new ClientLocalPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id,
							TeamEnum.RED));
				else
					myTeam.add(new ClientLocalPlayer(map.getSpawns()[id + 3].x * 64, map.getSpawns()[id + 3].y * 64, id,
							TeamEnum.BLUE));
			} else {
				if (clientTeam.equals("Red"))
					enemies.add(new ClientLocalPlayer(map.getSpawns()[id + 3].x * 64, map.getSpawns()[id + 3].y * 64, id,
							TeamEnum.BLUE));
				else
					enemies.add(new ClientLocalPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id,
							TeamEnum.RED));
			}
		}

		allplayers.addAll(enemies);
		allplayers.addAll(myTeam);
		collisionsHandler.setPlayers(allplayers);
		cPlayer.setClientEnemies(enemies);

		teams.setEnemies(enemies);
		teams.setMyTeam(myTeam);
		
		// for debugging
		if(debug) System.out.println("game has started for player with ID " + clientID);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				m.transitionTo(MenuEnum.EliminationMulti, null);
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
	public ArrayList<ClientLocalPlayer> getMyTeam() {
		return myTeam;
	}

	/**
	 * Return all the players that are not in this Player's team.
	 *
	 * @return All opponent players.
	 *
	 * @author Alexandra Paduraru
	 */
	public ArrayList<ClientLocalPlayer> getEnemies() {
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
	public PhysicsClientPlayer getClientPlayer() {
		return cPlayer;
	}
}