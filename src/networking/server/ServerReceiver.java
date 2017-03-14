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

	private int myClientsID;
	private BufferedReader myClient;
	private ClientTable clientTable;
	private ServerSender sender;
	private LobbyTable gameLobby;
	private MessageQueue myMsgQueue;
	private Lobby lobby;
	private UDPServer udpReceiver;
	private boolean singlePlayer;
	private boolean debug = false;

	/**
	 * Construct the class, setting passed variables to local objects.
	 *
	 * @param clientID The ID of the client.
	 * @param reader Input stream reader for data.
	 * @param table Table storing client information.
	 * @param sender Sender class for sending messages to the client.
	 * @param passedGameLobby Table storing all lobby information.
	 * @param udpReceiver UDP Server sender/receiver.
	 * @param single Are we playing in Single Player mode?
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
				if (debug) System.out.println("ServerReceiver got : " + text);

				// If text isn't null and does not read "Exit:Client" do...
				if (text != null && text.compareTo("Exit:Client") != 0) {

					// 	  Protocols
					// ---------------- //

					// UI Client Actions.
					// ------------------
					// When user specifies a game mode to play, add them to a
					// lobby.
					if (debug) System.out.println("Server single player = " + singlePlayer);
					
					if (text.contains("Play:Mode:"))
						if (singlePlayer){
							playModeSingleAction(text);
							sendToAll("Single");
						}
						else
							playModeAction(text);

					// When user attempts to switch teams, try to switch.
					else if (text.contains("SwitchTeam"))
						gameLobby.switchTeams(clientTable.getPlayer(myClientsID), this);

					// When user tries to change their username.
					else if (text.contains("Set:Username:"))
						setUsernameAction(text);


					// UI Client Requests
					// ------------------
					// Team 1 for blue, 2 for red.
					// Get usernames of people currently in red team of lobby.
					else if (text.contains("Get:Red"))
						getRedTeamAction();


					// Get usernames of people currently in blue team of lobby.
					else if (text.contains("Get:Blue"))
						getBlueTeamAction();


					// Get the client's currently set username.
					else if (text.contains("Get:Username"))
						getUsernameAction();


					// Reset the client when they exit the game.
					else if (text.contains("Exit:Game") || text.contains("QuitLobby"))
						exitGame();

					// Server Actions
					// ---------------
					// Send a message to all clients in the game.
					// Includes : sending moves, bullets
					else if (text.contains("SendToAll:"))
						sendToAll(text);


					//*===================== !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!========================================
					//							NEW INTEGRATION BELOW
//					switch(text.charAt(0)){
//					case '0' : playerInputChanged(text);
//					}

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



	//*===================== !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!========================================
	//							NEW INTEGRATION BELOW
	
	private void playModeSingleAction(String text) {
		int gameMode = Integer.parseInt(text.substring(10));
		gameLobby.addPlayerToLobby(clientTable.getPlayer(myClientsID), gameMode, this, udpReceiver);
		lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());

		if (debug) System.out.println("Added player to lobby...");
		if (debug) System.out.println(lobby.getCurrPlayerTotal());
		
		lobby.switchGameStatus();
		lobby.playGame(this, udpReceiver, gameMode);
		lobby.startGameLoop(udpReceiver, gameMode);
	}
	
	
//	public void playerInputChanged(String text){
//		//Protocol: "O:Up:Down:Left:Right:Shooting:Mouse:<mX>:<mY>:<id>"
//		String[] actions = text.split(":");
//		boolean up = false;
//		boolean down = false;
//		boolean left = false;
//		boolean right = false;
//		boolean shoot = false;
//		double mX = 0;
//		double mY = 0;
//
//		for(int i = 0; i < actions.length - 1; i++){
//			String act = actions[i];
//			switch(act){
//				case "Up"    : up = true;
//							   break;
//				case "Down"  : down = true;
//							   break;
//				case "Left"  : left = true;
//							   break;
//				case "Right" : right = true;
//				   			   break;
//				case "Mouse" : mX = Double.parseDouble(actions[i+1]);
//							   mY = Double.parseDouble(actions[i+2]);
//							   i = i + 3;
//							   break;
//				default		 : break;
//			}
//		}
//
//		int id = Integer.parseInt(actions[actions.length - 1]);
//
//		if(debug) System.out.println(inputReceiver == null);
//		if(inputReceiver != null){
//			inputReceiver.updatePlayer(id, up, down, left, right, shoot, mX, mY);
//		}
//
//	}

//	public void setInputReceiver(ArrayList<ServerMinimumPlayer> players){
//		inputReceiver.setPlayers(players);
//		if (debug)	System.out.println("Input receiver set");
//	}


	//===================OLD INTEGRATION===============================================

	/*
	 * Different actions performed depending on the messages received from
	 * clients.
	 */


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
	 * @param text
	 */
	private void getRedTeamAction() {
		Lobby lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		String redMems = lobby.getTeam(2);
		myMsgQueue.offer(new Message("Ret:Red:" + redMems));
	}

	/**
	 * Initialise playing a game, setting game status and starting a timer in the lobby.
	 * @param text Text passed to server, parsed for input.
	 */
	private void playModeAction(String text) {
		
		int gameMode = Integer.parseInt(text.substring(10));
		gameLobby.addPlayerToLobby(clientTable.getPlayer(myClientsID), gameMode, this, udpReceiver);
		lobby = gameLobby.getLobby(clientTable.getPlayer(myClientsID).getAllocatedLobby());
		int curTotal = lobby.getCurrPlayerTotal();
		// lobby.timerStart(this);
		lobby.timerStart(this, udpReceiver, gameMode);
		if (curTotal == lobby.getMaxPlayers()) {
			lobby.switchGameStatus();
			
		}
	}

	/**
	 * Set username for a given client.
	 * @param text Text sent to server from client, containing new username.
	 */
	public void setUsernameAction(String text) {
		String username = text.substring(13, text.length());
		clientTable.getPlayer(myClientsID).setUsername(username);
	}

	/* Methods to communicate between client and sender */

	/**
	 * Send a message to all clients in a given game.
	 * @param text Message to send to all clients.
	 */
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
//
//		if (text.contains("Move")){
//			//extract the id of the server player with a new location
//			String[] parsedMsg = text.split(":");
//			int id = Integer.parseInt(parsedMsg[2]);
//			double x = Double.parseDouble(parsedMsg[3]);
//			double y = Double.parseDouble(parsedMsg[4]);
//			double angle = Double.parseDouble(parsedMsg[5]);
//
//			//get that server player from the lobby
//			ServerPlayer currentPlayer = null;
//			for(ServerPlayer p : lobby.getRedTeam().getMembers())
//				if( id == p.getPlayerId())
//					currentPlayer = p;
//
//			if (currentPlayer == null){
//				for(ServerPlayer p : lobby.getBlueTeam().getMembers())
//					if( id == p.getPlayerId())
//						currentPlayer = p;
//			}
//			//update its location
//			currentPlayer.setX(x);
//			currentPlayer.setY(y);
//			currentPlayer.setAngle(angle);

//		}
	}

	/**
	 * Send a message to a particular client.
	 * @param id ID of client to send message to.
	 * @param text Message to send to client.
	 */
	public void sendToSpec(int id, String text) {
		if (debug) System.out.println("Sending msg to id: " + id);
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
