package networkingClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import enums.TeamEnum;
import gui.GUIManager;
import javafx.application.Platform;
import networkingShared.Message;
import networkingShared.MessageQueue;
import physics.Bullet;
import players.ClientLocalPlayer;
import players.PhysicsClientPlayer;
import rendering.Map;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.
/**
 * Class to get messages from client, process and put appropriate message for a
 * client.
 */
public class ClientReceiver extends Thread {
	private int clientID;
	private BufferedReader fromServer;
	private ClientSender sender;
	private MessageQueue myMsgQueue;
	private Message msg;
	private GUIManager m;
	private PhysicsClientPlayer cPlayer;
	private ArrayList<ClientLocalPlayer> myTeam;
	private ArrayList<ClientLocalPlayer> enemies;
	
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
	public ClientReceiver(int Cid, BufferedReader reader, ClientSender sender, MessageQueue msgQueue, GUIManager m) {
		this.m = m;
		clientID = Cid;
		fromServer = reader;
		this.sender = sender;
		myMsgQueue = msgQueue;
		myTeam = new ArrayList<>();
		enemies = new ArrayList<>();
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

					// In-game messages
					if (text.contains("Move"))
						moveAction(text);

					if (text.contains("Bullet"))
						bulletAction(text);

					// UI Requests
					if (text.contains("Ret:Red:")) {
						if(debug) System.out.println("Got red");
						String[] red = text.substring(8).split("-");
						m.updateRedLobby(red);
					} else if (text.contains("Ret:Blue:")) {
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
					else if (text.contains("StartGame")) {
						startGameAction(text);
					} else if (text.contains("EndGame")) {
						if(debug) System.out.println("Game has ended for player with ID " + clientID);
						// Get data about scores, and pass into transition
						// method
						int someScore = 0;
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								m.transitionTo("EndGame", someScore);
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
	 * Action starting when a player fires a bullet. It renders the bullet and
	 * also detects when a player has been eliminated.
	 * 
	 * @param text
	 *            The protocol text containing information about the coordinates
	 *            and angle of the bullet, as well as the player id which shot
	 *            it.
	 *
	 * @author Alexandra Paduraru
	 */
	private void bulletAction(String text) {
		// Protocol message: SendToAll:Bullet:id:team:x:y:angle:
		String[] data = text.split(":");

		int id = Integer.parseInt(data[2]);
		String t = data[3];

		ClientLocalPlayer p = getPlayerWithID(id);

		if (p != null) // the player is not us
		{
			ArrayList<Bullet> firedBullets = new ArrayList<>();
			for (int i = 4; i < data.length - 2; i = i + 3) {

				double x = Double.parseDouble(data[i]);
				double y = Double.parseDouble(data[i + 1]);
				double angle = Double.parseDouble(data[i + 2]);

				firedBullets.add(new Bullet(x, y, angle, p.getTeam()));
			}
			p.tickBullets(firedBullets);
		}

		if(debug)
		{
			 System.out.print("my Team players: " );
			 for(ClientLocalPlayer pq : myTeam)
			 System.out.print(pq.getPlayerId() + " ");
			 System.out.println();
			 System.out.print("my enemy players: " );
		}
	}

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

		// add myself to my team
		// create my client
		if (clientTeam.equals("Red"))
			cPlayer = new PhysicsClientPlayer(map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64,
					clientID, false, map, m.getAudioManager(), TeamEnum.RED, this);
		else
			cPlayer = new PhysicsClientPlayer(map.getSpawns()[clientID + 3].x * 64, map.getSpawns()[clientID + 3].y * 64,
					clientID, false, map, m.getAudioManager(), TeamEnum.BLUE, this);

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
		cPlayer.setClientEnemies(enemies);

		// for debugging
		if(debug) System.out.println("game has started for player with ID " + clientID);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				m.transitionTo("Elimination", null);
			}
		});
	}

	/**
	 * Gets a move signal from the server about a specific player. The method
	 * finds that player and updates the player's position on the map
	 * accordingly.
	 * 
	 * @param text
	 *            The protocol message containing the new x and y coordinates,
	 *            as well as the angle of the player.
	 *
	 * @author Alexandra Paduraru
	 */
	public void moveAction(String text) {
		String[] msg = text.split(":");
		// System.out.println("Text move action: " + Arrays.toString(msg));

		int id = Integer.parseInt(msg[2]);
		double x = Double.parseDouble(msg[3]);
		double y = Double.parseDouble(msg[4]);
		double angle = Double.parseDouble(msg[5]);

		if(debug)
		{
			for(ClientLocalPlayer p : myTeam)
				System.out.println(p.getPlayerId());
		}


		if (id != clientID) {
			// find the player that need to be updated
			ClientLocalPlayer p = getPlayerWithID(id);
			p.tick(x, y, angle);
		}
	}

	/* Getters and setters */
	/**
	 * Retrieves a player with a specific id from the current game.
	 * 
	 * @param id
	 *            The player's id.
	 * @return The player with the given id.
	 *
	 * @author Alexandra Paduraru
	 */
	private ClientLocalPlayer getPlayerWithID(int id) {
		// Check if the Player is in my team
		for (ClientLocalPlayer p : myTeam)
			if (p.getPlayerId() == id)
				return p;

		// otherwise, player is in the enemy team
		for (ClientLocalPlayer p : enemies)
			if (p.getPlayerId() == id)
				return p;

		return null;
	}

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

	public ClientSender getSender() {
		return sender;
	}

	public PhysicsClientPlayer getClientPlayer() {
		return cPlayer;
	}
}