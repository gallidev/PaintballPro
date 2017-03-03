package networking.game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import networking.server.ClientTable;
import networking.server.Lobby;
import networking.server.LobbyTable;
import players.ServerBasicPlayer;
import players.ServerPlayer;

/**
 * Server-side Sender and Receiver using UDP protocol for in-game transmission.
 * One per server.
 * 
 * @author MattW
 */
public class UDPServer extends Thread{

	private boolean debug = false;
	private ClientTable clients;
	private LobbyTable lobbyTab;
	private DatagramSocket serverSocket;
	
	/**
	 * Constructor, sets global variables to those passed.
	 * @param clientTable Table storing all necessary client information.
	 * @param lobby Table storing all necessary lobby information.
	 */
	public UDPServer(ClientTable clientTable, LobbyTable lobby) {
		clients = clientTable;
		this.lobbyTab = lobby;
	}
	
	/**
	 * Loop through, reading messages from the server.
	 * Main method, ran when the thread is started.
	 */
	public void run()
	{
		try {
			serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[1024];
			while(true)
			{
			      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			      
			      serverSocket.receive(receivePacket);
			      String sentence = new String( receivePacket.getData());
			      
			      if(debug)System.out.println("RECEIVED: " + sentence);
			      
			      InetAddress IPAddress;
			      int port;
			     
			      if(sentence.contains("ExitUDP"))
			    	  break;
			      else if(sentence.contains("Connect:"))
			      {
			    	  IPAddress = receivePacket.getAddress();
			    	  port = receivePacket.getPort();
			    	  int clientID = Integer.parseInt(sentence.substring(8));
			    	  clients.addNewIP(IPAddress.toString(), clientID);
			    	  clients.addUDPQueue(IPAddress.toString());
			      }
			      else
			      {
			    	  // We assume that all players in a game are now connected and their ip addresses inserted into clientTable.
			    	  // if they are not, we will not be able to send messaged to them using sendToAll.
			    	  	String ipFrom = receivePacket.getAddress().toString();
						// In-Game Status'
						// ---------------
						if (sentence.contains("Scored"))
							newScoreAction(sentence,ipFrom);
			    	  
						// Server Actions
						// ---------------
						// Send a message to all clients in the game.
						// Includes : sending moves, bullets
						if (sentence.contains("SendToAll:"))
							sendToAll(sentence,ipFrom);
						
						// Reset the client when they exit the game.
						if (sentence.contains("Exit:Game"))
							exitGame(ipFrom);
						
			      }
			}
		} catch(Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
		finally
		{
			// TODO - Let's do some closing stuff here.
			serverSocket.close();
		}
	}

	/**
	 * Send a message to all clients in a game - based on Lobby.
	 * @param toBeSent Message to be sent to all clients.
	 * @param lobbyID ID of game lobby.
	 */
	public void sendToAll(String toBeSent, int lobbyID) {
		byte[] sendData = new byte[1024];
		sendData = toBeSent.getBytes();
		
		// We get all players in the same game as the transmitting player.
		ServerBasicPlayer[] players = lobbyTab.getLobby(lobbyID).getPlayers();
		// Let's send a message to them all.
		for(ServerBasicPlayer player : players)
		{
			int id = player.getID();
			String playerIP = clients.getIP(id);
			// Parse IP to get first part and port number.
			String ipAddr = playerIP.split(":")[0];
			String ipPort = playerIP.split(":")[1];
			try{
				// Let's send the message.
				InetAddress sendAddress = InetAddress.getByName(ipAddr);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, Integer.parseInt(ipPort));
				serverSocket.send(sendPacket);
			}
			catch(Exception e)
			{
				if(debug) System.out.println("Cannot send message:"+toBeSent+", to:" +ipAddr);
			}
		}
		
		Lobby lobby = lobbyTab.getLobby(lobbyID); 
		
		//Given a move, the server player's location needs to be updated
		if (toBeSent.contains("Move")){
				makeMove(lobby,toBeSent);
		}
	}
	
	/**
	 * Send a message to all clients in a game - based on Lobby.
	 * @param toBeSent Message to be sent to all clients.
	 * @param ip IP of a particular client in the lobby we want to send messages to.
	 */
	public void sendToAll(String toBeSent, String ip)
	{
		// we get the lobby id.
		int lobbyID = clients.getPlayer(clients.getID(ip)).getAllocatedLobby();
		// we can now send to all clients in the same lobby as the origin client.
		sendToAll(toBeSent,lobbyID);
	}
	
	// -------------------------------------
	// -----------Game Methods--------------
	// -------------------------------------
	/**
	 * Updates a team's score based on the information got from a client. Helps
	 * the server keep track of each team's score(the teams are stored in the
	 * Lobby).
	 * 
	 * @param text The protocol message for updating a team's score.
	 * 
	 * @author Alexandra Paduraru and Matthew Walters
	 */
	public void newScoreAction(String text, String ip)
	{
		// Protocol : "Scored:<Team>"
		String teamColour = text.split(":")[1];
		Lobby lobby = lobbyTab.getLobby(clients.getPlayer(clients.getID(ip)).getAllocatedLobby()); 
		if (teamColour.equals("Red"))
			lobby.getRedTeam().incrementScore(1);
		else
			lobby.getBlueTeam().incrementScore(1);

		// debugging code
		if(debug) System.out.println("Red team score: " + lobby.getRedTeam().getScore());
		if(debug) System.out.println("Blue team score: " + lobby.getBlueTeam().getScore());
	}
	
	/**
	 * We reset status of some objects storing game-specific information.
	 * @param ip IP of a particular client to remove.
	 */
	private void exitGame(String ip) {
		ServerBasicPlayer myPlayer = clients.getPlayer(clients.getID(ip));
		lobbyTab.removePlayer(myPlayer);
		myPlayer.setAllocatedLobby(-1);
	}
	
	/**
	 * We represent a move being made by a player.
	 * @param lobby Lobby that the player is in.
	 * @param text Text to parse movement information.
	 * 
	 * @author Alexandra Paduraru and Matthew Walters
	 */
	private void makeMove(Lobby lobby, String text)
	{
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