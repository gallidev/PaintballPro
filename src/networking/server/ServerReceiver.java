package networking.server;

import java.io.BufferedReader;
import java.io.IOException;

import networking.game.UDPServer;
import networking.shared.Message;
import networking.shared.MessageQueue;

import players.ServerBasicPlayer;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.
/**
 * Class to get messages from client, process and put appropriate message for a
 * client.
 *
 * @author Matthew Walters
 */
public class ServerReceiver extends Thread {

	private boolean singlePlayer;
	private boolean debug = false;
	private BufferedReader myClient;
	private ClientTable clientTable;
	private int myClientsID;
	private Lobby lobby;
	private LobbyTable gameLobby;
	private MessageQueue myMsgQueue;
	private ServerSender sender;
	private UDPServer udpReceiver;

	/**
	 * Construct the class, setting passed variables to local objects.
	 *
	 * @param clientID
	 *            The ID of the client.
	 * @param reader
	 *            Input stream reader for data.
	 * @param table
	 *            Table storing client information.
	 * @param sender
	 *            Sender class for sending messages to the client.
	 * @param passedGameLobby
	 *            Table storing all lobby information.
	 * @param udpReceiver
	 *            UDP Server sender/receiver.
	 * @param single
	 *            Are we playing in Single Player mode?
	 */
	public ServerReceiver(int clientID, BufferedReader reader, ClientTable table, ServerSender sender,
			LobbyTable passedGameLobby, UDPServer udpReceiver, boolean single) {
		myClientsID = clientID;
		myClient = reader;
		clientTable = table;
		this.sender = sender;
		gameLobby = passedGameLobby;
		myMsgQueue = clientTable.getQueue(myClientsID);
		this.udpReceiver = udpReceiver;
		this.singlePlayer = single;
	}

	/**
	 * The main method running in this class, runs when the class is started
	 * after initialisation.
	 */
	public void run() {
		try {
			while (true) {
				// Get input from the client read stream.
				String text = myClient.readLine();

				// for debugging
				if (debug)
					System.out.println("ServerReceiver got : " + text);

				// If text isn't null and does not read "Exit:Client" do...
				if (text != null && text.compareTo("Exit:Client") != 0) {

					// Protocols
					// ---------------- //

					// UI Client Actions.
					// ------------------
					// When user specifies a game mode to play, add them to a
					// lobby.
					if (debug)
						System.out.println("Server single player = " + singlePlayer);

					if (text.contains("Play:Mode:"))
						playModeAction(text);

					// When user attempts to switch teams, try to switch.
					else if (text.contains("SwitchTeam"))
						switchTeams();

					// UI Client Requests
					// ------------------
					// Team 1 for blue, 2 for red.
					// Get usernames of people currently in red team of lobby.
					else if (text.contains("Get:Red"))
						getRedTeamAction();

					// Get usernames of people currently in blue team of lobby.
					else if (text.contains("Get:Blue"))
						getBlueTeamAction();

					// Reset the client when they exit the game.
					else if (text.contains("Exit:Game") || text.contains("QuitLobby"))
						exitGame();

					// Server Actions
					// ---------------
					// Send a message to all clients in the game.
					// Includes : sending moves, bullets
					else if (text.contains("SendToAll:"))
						sendToAll(text);

				} else // if the client wants to exit the system.
				{
					exitSystem();
					return;
				}
			}
		} catch (IOException e) {
			// If there is something wrong... exit cleanly.
			exitSystem();
			return;
		}
	}

	/*
	 * Different actions performed depending on the messages received from
	 * clients.
	 */
	
	private void switchTeams() {
		gameLobby.switchTeams(clientTable.getPlayer(myClientsID), this);
	}

	/**
	 * Retrieve the username for a particular client.
	 */
	public void getUsernameAction() {
		String clientUsername = clientTable.getPlayer(myClientsID).getUsername();
		myMsgQueue.offer(new Message("Ret:Username:" + clientUsername));
	}

	/**
	 * Retrieve members of Blue team.
	 */
	private void getBlueTeamAction() {
		Lobby lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		String blueMems = lobby.getTeam(1);
		myMsgQueue.offer(new Message("Ret:Blue:" + blueMems));
	}

	/**
	 * Retrieve members of Red team.
	 * 
	 * @param text
	 */
	private void getRedTeamAction() {
		Lobby lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		String redMems = lobby.getTeam(2);
		myMsgQueue.offer(new Message("Ret:Red:" + redMems));
	}

	/**
	 * Initialise playing a game, setting game status and starting a timer in
	 * the lobby.
	 * 
	 * @param text
	 *            Text passed to server, parsed for input.
	 */
	private void playModeAction(String text) {
		int gameMode;
		int curTotal;
		gameMode = Integer.parseInt(text.substring(10));
		gameLobby.addPlayerToLobby(clientTable.getPlayer(myClientsID), gameMode, this, udpReceiver);
		lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		curTotal = lobby.getCurrPlayerTotal();
		// lobby.timerStart(this);
		lobby.timerStart(this, udpReceiver, gameMode);
		if (curTotal == lobby.getMaxPlayers()) {
			lobby.switchGameStatus();
		}
	}

	/* Methods to communicate between client and sender */

	/**
	 * Send a message to all clients in a given game.
	 * 
	 * @param text
	 *            Message to send to all clients.
	 */
	public void sendToAll(String text) {
		// System.out.println("Sending to all: " + text);

		// extract the lobby which contains only the players that should receive
		// the message
		ServerBasicPlayer myClientID = clientTable.getPlayer(myClientsID);
		if (myClientID != null) {
			int allocatedLobby = myClientID.getAllocatedLobby();
			Lobby currentLobby = gameLobby.getLobby(allocatedLobby);

			if (currentLobby != null) {
				ServerBasicPlayer[] gamePlayers = currentLobby.getPlayers();

				for (ServerBasicPlayer player : gamePlayers) {
					MessageQueue queue = clientTable.getQueue(player.getID());
					queue.offer(new Message(text));
				}
			}
		}

	}

	/**
	 * Send a message to a particular client.
	 * 
	 * @param id
	 *            ID of client to send message to.
	 * @param text
	 *            Message to send to client.
	 */
	public void sendToSpec(int id, String text) {
		if (debug)
			System.out.println("Sending msg to id: " + id);
		MessageQueue queue = clientTable.getQueue(id);
		queue.offer(new Message(text));
	}

	/* System methods */
	/**
	 * Method to exit game smoothly.
	 */
	private void exitGame() {
		ServerBasicPlayer myPlayer = clientTable.getPlayer(myClientsID);
		gameLobby.removePlayer(myPlayer);
		myPlayer.setAllocatedLobby(-1);
	}

	/**
	 * Method to exit system smoothly.
	 */
	private void exitSystem() {

		// Remove client from any game lobbies.
		ServerBasicPlayer myPlayer = clientTable.getPlayer(myClientsID);

		// Send exit message to the client.
		myMsgQueue.offer(new Message("Exit:Client"));
		
		if (myPlayer.getAllocatedLobby() != -1)
			gameLobby.removePlayer(myPlayer);

		// Remove client from client table data as they are exiting the system.
		clientTable.removeClient(myClientsID);

		// Close client stream.
		try {
			myClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close server sender thread from the client.
		sender.stopThread();
	}
}