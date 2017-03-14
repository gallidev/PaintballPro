package networking.game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import enums.Menu;
import gui.GUIManager;
import integrationClient.ClientGameStateReceiver;
import javafx.application.Platform;
import networking.client.TeamTable;
import physics.Bullet;
import players.GhostPlayer;
import players.EssentialPlayer;

/**
 * Client-side Sender and Receiver using UDP protocol for in-game transmission.
 * One per client.
 *
 * @author MattW
 */
public class UDPClient extends Thread {

	private boolean debug = false;
	private int clientID;

	private String nickname;

	private ClientGameStateReceiver gameStateReceiver;

	public boolean bulletDebug = false;

	private DatagramSocket clientSocket;
	private InetAddress IPAddress;

	private GUIManager m;

	private TeamTable teams;

	public boolean connected = false;
	public boolean testSendToAll = false;
	
	int sIP;

	/**
	 * We establish a connection with the UDP server... we tell it we are connecting for the first time so that
	 * it stores our information server-side.
	 * @param clientID ID allocated to the client.
	 * @param udpServIP IP for the server-side UDP socket.
	 * @param guiManager Manager of GUI.
	 * @param teams Both client's and opposing teams.
	 * @param portNum port to send and receive packets.
	 * @param nickname Nickname of client.
	 */
	public UDPClient(int clientID, String udpServIP, int udpServPort,GUIManager guiManager, TeamTable teams, int portNum, String nickname)
	{
		int port = portNum;
		// 9877
		this.clientID = clientID;
		this.m = guiManager;
		this.teams = teams;
		this.nickname = nickname;

		sIP = udpServPort;
		
		if(debug) System.out.println("Making new UDP Client");

		// Let's establish a connection to the running UDP server and send our client id.
		boolean error = true;
		while (error) {
			error = false;
			try {
				if (debug) System.out.println("Attempting to make client socket");
				clientSocket = new DatagramSocket(port);
				if (debug) System.out.println("Attempting to get ip address");
				IPAddress = InetAddress.getByName(udpServIP);
				if (debug) System.out.println("IPAddress is:" + IPAddress.getHostAddress());
				String sentence = "Connect:" + clientID;
				if (debug) System.out.println("sending data:" + sentence);
				sendMessage(sentence);
				if (debug) System.out.println("sent");
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String sentSentence = new String(receivePacket.getData());
				if(sentSentence.contains("Successfully Connected"))
					connected = true;
				if (debug) System.out.println(sentSentence.trim());
			} catch (Exception e) {
				error = true;
				if (debug) System.err.println(e.getMessage());
				port++;
			}
		}
	}

	/**
	 * Loop through, reading messages from the server.
	 * Main method, ran when the thread is started.
	 */
	public void run()
	{
		if(debug) System.out.println("My nickname is: " + nickname);
		try{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			while(true)
			{
				clientSocket.receive(receivePacket);
			    String receivedPacket = new String(receivePacket.getData(), 0, receivePacket.getLength());
				if(debug) System.out.println("Received from server:"+receivedPacket);

				// -------------------------------------
				// -----------Game Messages-------------
				// -------------------------------------
				switch(receivedPacket.charAt(0)){

					case '1' : updatePlayerAction(receivedPacket) ;
							   break;
					case '3' : updateScoreAction(receivedPacket);
							   break;
					case '4' : updateBulletAction(receivedPacket);
							   break;
					case '5' : endGameAction();
							   break;
					case '6' : getRemainingTime(receivedPacket);
							   break;
					case '7' : capturedFlagAction();
							   break;
						

				}
				if (receivedPacket.contains("Exit"))
					break;
				else if (receivedPacket.contains("TestSendToAll"))
				{
					testSendToAll = true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			if(debug) System.err.println(e.getStackTrace());
		}
		finally{
			if(debug) System.out.println("Closing Client.");
			clientSocket.close();
		}
		if(debug) System.out.println("Closing Client.");
		clientSocket.close();
		if(debug) System.err.println("Socket closed");
	}

	

	/**
	 * Send messages to the server.
	 * @param msg Message to send.
	 */
	public void sendMessage(String msg)
	{
		try{
			if(debug) System.out.println("Attempting to send:"+msg);
			byte[] sendData = new byte[1024];
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, sIP);
			clientSocket.send(sendPacket);
		}
		catch(Exception e)
		{
			if (debug) System.out.println("Exception in sendMessage");
			e.printStackTrace();
			if(debug) System.err.println(e.getStackTrace());
		}
	}


	// -------------------------------------
	// -----------Game Methods--------------
	// -------------------------------------


	/**
	 * Update the player's location, based on the coordinates received from the server.
	 * @param text The protocol message containing the new coordinates of the player.
	 * 
	 * @author Alexandra Paduraru
	 * @author Filippo Galli
	 */
	private void updatePlayerAction(String text) {
		//Protocol: "1:<id>:<x>:<y>:<angle>:<visiblity>"
		if(debug)System.out.println(text);

		if(text != ""){
			String[] actions = text.split(":");

			int id = Integer.parseInt(actions[1]);
			double x = Double.parseDouble(actions[2]);
			double y = Double.parseDouble(actions[3]);
			double angle = Double.parseDouble(actions[4]);

			boolean visibility = true;
			if (actions[5].equals("false"))
				visibility = false;

//			if (visibility == false)
//				System.out.println("I'm invisible " + id);

			if(gameStateReceiver != null){
				gameStateReceiver.updatePlayer(id, x, y, angle, visibility);
			}
		}



	}

	/**
	 * Method which enables the client to receive the game score, in order to be shown in the client's GUI.
	 * @param text The protocol message containing the score.
	 * 
	 * @author Alexandra Paduraru
	 */
	public void updateScoreAction(String text){
		int redScore = Integer.parseInt(text.split(":")[1]);
		int blueScore = Integer.parseInt(text.split(":")[2]);

//		System.out.println("Red score: " + redScore);
//		System.out.println("Blue score: " + blueScore);

		//do stuff here to update the GUI
	}


	/**
	 * Receives all the player's active bullets and updates them accordingly on the client side,
	 * using the client game state receiver.
	 * @param text The protocol message containg the active bullets and the id of the player.
	 * 
	 * @author Alexandra Paduraru
	 * @author Filippo Galli
	 */
	public void updateBulletAction(String text){
		// Protocol message: 4:id:idBullet:x:y:...

		int id = Integer.parseInt(text.split(":")[1]);

		//get all the bullets
		String[] data = text.split(":");

		if (bulletDebug){
			System.out.print("Received bullets: " );

			System.out.print("Received bullets: " );

			for(int i = 0; i < data.length; i++){
				if (data[i].isEmpty())
					System.out.print("EMPTY ");
				else
					System.out.print(data[i] + " ");
			}

		}

		String[] bullets = Arrays.copyOfRange(data, 2, data.length);

		if(gameStateReceiver != null){
			gameStateReceiver.updateBullets(id, bullets);
		}
	}

	private void getRemainingTime(String sentence) {

		String time = sentence.split(":")[1];

		//do stuff here to update the UI

		if (debug) System.out.println("remaining time on client: " + time);

	}
	
	private void capturedFlagAction() {
		// TODO Auto-generated method stub
		//do stuff here to render
	}

	private void endGameAction() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				m.transitionTo(Menu.EndGame, 0);
			}
		});

	}


	/**
	 * Retrieves a player with a specific id from the current game.
	 *
	 * @param id
	 *            The player's id.
	 * @return The player with the given id.
	 *
	 * @author Alexandra Paduraru and Matthew Walters
	 */
	private GhostPlayer getPlayerWithID(int id) {
		// Check if the Player is in my team
		for (GhostPlayer p : teams.getMyTeam())
			if (p.getPlayerId() == id)
				return p;

		// otherwise, player is in the enemy team
		for (GhostPlayer p : teams.getEnemies())
			if (p.getPlayerId() == id)
				return p;

		return null;
	}
	
	/**
	 * Sets the client game state receiver, which deals with all the in-game information from the server.
	 * @param gameStateReceiver The new client game state receiver.
	 * 
	 * @author Alexandra Paduraru
	 */
	public void setGameStateReceiver(ClientGameStateReceiver gameStateReceiver){
		this.gameStateReceiver = gameStateReceiver;

		//set the corresponding GhostPlayer's nickname
		gameStateReceiver.getPlayerWithId(clientID).setNickname(nickname);
	}
}