package networkingServer;

import java.io.BufferedReader;
import java.io.IOException;

import integrationServer.ClientInputReceiver;
import networkingShared.Message;
import networkingShared.MessageQueue;
import players.ServerBasicPlayer;
import players.ServerPlayer;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

/**
 * Class to get messages from client, process and put appropriate message for a
 * client.
 */
public class ServerMsgReceiver extends Thread {

	private int myClientsID;
	private BufferedReader myClient;
	private ClientTable clientTable;
	private ServerMsgSender sender;
	private LobbyTable gameLobby;
	private MessageQueue myMsgQueue;
	private Lobby lobby;
	
	private ClientInputReceiver inputReceiver;
	
	private boolean debug = true;

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
	 */
	public ServerMsgReceiver(int clientID, BufferedReader reader, ClientTable table, ServerMsgSender sender,
			LobbyTable passedGameLobby) {
		myClientsID = clientID;
		myClient = reader;
		clientTable = table;
		this.sender = sender;
		gameLobby = passedGameLobby;
		myMsgQueue = clientTable.getQueue(myClientsID);
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
				// System.out.println("ServerReceiver got : " + text);

				// If text isn't null and does not read "Exit:Client" do...
				if (text != null && text.compareTo("Exit:Client") != 0) {

					// 	  Protocols
					// ---------------- //

					// In-Game Status'
					// ---------------
					if (text.contains("Scored"))
						newScoreAction(text);

					// UI Client Actions.
					// ------------------
					// When user specifies a game mode to play, add them to a
					// lobby.
					if (text.contains("Play:Mode:"))
						playModeAction(text);

					// When user attempts to switch teams, try to switch.
					if (text.contains("SwitchTeam"))
						gameLobby.switchTeams(clientTable.getPlayer(myClientsID), this);

					// When user tries to change their username.
					if (text.contains("Set:Username:")) 
						setUsernameAction(text);
					

					// UI Client Requests
					// ------------------
					// Team 1 for blue, 2 for red.
					// Get usernames of people currently in red team of lobby.
					if (text.contains("Get:Red"))
						getRedTeamAction(text);
					

					// Get usernames of people currently in blue team of lobby.
					if (text.contains("Get:Blue")) 
						getBlueTeamAction(text);
					

					// Get the client's currently set username.
					if (text.contains("Get:Username")) 
						getUsernameAction(text);
					

					// Reset the client when they exit the game.
					if (text.contains("Exit:Game"))
						exitGame();

					// Server Actions
					// ---------------
					// Send a message to all clients in the game.
					// Includes : sending moves, bullets
					if (text.contains("SendToAll:"))
						sendToAll(text);
					
					
					//*===================== !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!========================================
					//							NEW INTEGRATION BELOW
					if(text.contains("Action:")){
						String[] action = text.split(":");
						
						switch(action[2]){
							case "Up"    : inputReceiver.moveUpAction();
							case "Down"  : inputReceiver.moveDownAction();
							case "Left"  : inputReceiver.moveLeftAction(); 
							case "Right" : inputReceiver.moveRightAction();
							case "Mouse" : inputReceiver.mouseMovedAction();
						}
					}

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

	public void getUsernameAction(String text) {
		String clientUsername = clientTable.getPlayer(myClientsID).getUsername();
		myMsgQueue.offer(new Message("Ret:Username:" + clientUsername));
	}

	private void getBlueTeamAction(String text) {
		Lobby lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		String blueMems = lobby.getTeam(1);
		myMsgQueue.offer(new Message("Ret:Blue:" + blueMems));
	}

	private void getRedTeamAction(String text) {
		Lobby lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		String redMems = lobby.getTeam(2);
		myMsgQueue.offer(new Message("Ret:Red:" + redMems));
	}

	private void playModeAction(String text) {
		int gameMode = Integer.parseInt(text.substring(10));
		gameLobby.addPlayerToLobby(clientTable.getPlayer(myClientsID), gameMode, this);
		lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		int curTotal = lobby.getCurrPlayerTotal();
		// lobby.timerStart(this);
		if (curTotal == 2) {
			lobby.switchGameStatus();
			lobby.timerStart(this);
		}
	}

	public void setUsernameAction(String text) {
		String username = text.substring(13, text.length());
		clientTable.getPlayer(myClientsID).setUsername(username);
	}

	/**
	 * Updates a team's score based on the information got from a client. Helps
	 * the server keep track of each team's score(the teams are stored in the
	 * Lobby).
	 * 
	 * @param text
	 *            The protocol message for updating a team's score.
	 * 
	 * @author Alexandra Paduraru
	 */
	public void newScoreAction(String text) {
		// Protocol : "Scored:<Team>"
		String teamColour = text.split(":")[1];

		if (teamColour.equals("Red"))
			lobby.getRedTeam().incrementScore(1);
		else
			lobby.getBlueTeam().incrementScore(1);

		// debugging code
		if(debug) System.out.println("Red team score: " + lobby.getRedTeam().getScore());
		if(debug) System.out.println("Blue team score: " + lobby.getBlueTeam().getScore());
	}

	/* Methods to communicate between client and sender */

	public void sendToAll(String text) {
		// System.out.println("Sending to all: " + text);
		
		//extract the lobby which contains only the players that should receive the message
		Lobby currentLobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		
		ServerBasicPlayer[] gamePlayers = currentLobby.getPlayers();
		for (ServerBasicPlayer player : gamePlayers) {
			MessageQueue queue = clientTable.getQueue(player.getID());
			queue.offer(new Message(text));
		}
		
		//Given a move, the server player's location needs to be updated
		
		if (text.contains("Move")){
			//extract the id of the server player with a new location
			String[] parsedMsg = text.split(":");
			int id = Integer.parseInt(parsedMsg[2]);
			double x = Double.parseDouble(parsedMsg[3]);
			double y = Double.parseDouble(parsedMsg[4]);
			double angle = Double.parseDouble(parsedMsg[5]);

			//get that server player from the lobby
			ServerPlayer currentPlayer = null;
			for(ServerPlayer p : lobby.getRedTeam().getMembers())
				if( id == p.getPlayerId())
					currentPlayer = p;
			
			if (currentPlayer == null){
				for(ServerPlayer p : lobby.getBlueTeam().getMembers())
					if( id == p.getPlayerId())
						currentPlayer = p;
			}
			//update its location
			currentPlayer.setX(x);
			currentPlayer.setY(y);
			currentPlayer.setAngle(angle);
		}
	}

	public void sendToSpec(int id, String text) {
		MessageQueue queue = clientTable.getQueue(id);
		queue.offer(new Message(text));
	}

	/* System methods */

	private void exitGame() {
		ServerBasicPlayer myPlayer = clientTable.getPlayer(myClientsID);
		gameLobby.removePlayer(myPlayer);
		myPlayer.setAllocatedLobby(-1);
	}

	private void exitSystem() {
		// Send exit message to the client.
		myMsgQueue.offer(new Message("Exit:Client"));

		// Remove client from any game lobbies.
		ServerBasicPlayer myPlayer = clientTable.getPlayer(myClientsID);
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
