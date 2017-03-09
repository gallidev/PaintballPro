package networking.game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import gui.GUIManager;
import integrationClient.ClientGameStateReceiver;
import networking.client.TeamTable;
import physics.Bullet;
import players.ClientLocalPlayer;
import players.GeneralPlayer;
import players.GhostPlayer;
import players.ServerMinimumPlayer;

/**
 * Client-side Sender and Receiver using UDP protocol for in-game transmission.
 * One per client.
 *
 * @author MattW
 */
public class UDPClient extends Thread {

	private boolean debug = true;
	private int clientID;
	private String nickname;

	DatagramSocket clientSocket;
	InetAddress IPAddress;

	GUIManager m;

	TeamTable teams;
	private ClientGameStateReceiver gameStateReceiver;

	public boolean bulletDebug = false;

	/**
	 * We establish a connection with the UDP server... we tell it we are connecting for the first time so that
	 * it stores our information server-side.
	 * @param clientID ID allocated to the client.
	 * @param udpServIP IP for the server-side UDP socket.
	 * @param guiManager Manager of GUI.
	 * @param teams Both client's and opposing teams.
	 */
	public UDPClient(int clientID, String udpServIP, GUIManager guiManager, TeamTable teams, int portNum, String nickname)
	{
		int port = portNum;
		// 9877
		this.clientID = clientID;
		this.m = guiManager;
		this.teams = teams;
		this.nickname = nickname;

		if(debug) System.out.println("Making new UDP Client");

		// Let's establish a connection to the running UDP server and send our client id.
		try{
			clientSocket = new DatagramSocket(port);
			IPAddress = InetAddress.getByName(udpServIP);
			if(debug) System.out.println("IPAddress is:"+IPAddress.getHostAddress());
			String sentence = "Connect:"+clientID;
			if(debug) System.out.println("sending data:"+sentence);
			sendMessage(sentence);
			if(debug) System.out.println("sent");
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String sentSentence = new String(receivePacket.getData());
			if(debug) System.out.println(sentSentence.trim());
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

				// In-game messages
				switch(receivedPacket.charAt(0)){

					case '1' : updatePlayerAction(receivedPacket) ;
							   break;
					case '3' : updateScoreAction(receivedPacket);
							   break;
					case '4' : updateBulletAction(receivedPacket);
							   break;
					case '6' : getRemainingTime(receivedPacket);


				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			if(debug) System.err.println(e.getStackTrace());
		}
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
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 19876);
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

			;
			if(gameStateReceiver != null){
				gameStateReceiver.updatePlayer(id, x, y, angle, visibility);
			}
		}



	}

	public void updateScoreAction(String text){
		int redScore = Integer.parseInt(text.split(":")[1]);
		int blueScore = Integer.parseInt(text.split(":")[2]);

		//do stuff here to update the GUI
	}

	public void setGameStateReceiver(ClientGameStateReceiver gameStateReceiver){
		this.gameStateReceiver = gameStateReceiver;

		//set the corresponding GhostPlayer's nickname
		gameStateReceiver.getPlayerWithId(clientID).setNickname(nickname);
	}

	public void updateBulletAction(String text){
		// Protocol message: 4:id:idBullet:x:y:...

		int id = Integer.parseInt(text.split(":")[1]);

		//get all the bullets
		String[] data = text.split(":");

		System.out.print("Received bullets: " );

		System.out.print("Received bullets: " );

		for(int i = 0; i < data.length; i++){
			if (data[i].isEmpty())
				System.out.print("EMPTY ");
			else
				System.out.print(data[i] + " ");
		}

		String[] bullets = Arrays.copyOfRange(data, 2, data.length);
//		System.out.print("Just the bullets: ");
//
//		for(int i = 0; i < bullets.length; i++){
//			if (bullets[i].isEmpty())
//				System.out.print("EMPTY ");
//			else
//				System.out.print(bullets[i] + " ");
//		}


		/*for(int i = 0; i < bullets.length; i++){
			if (bullets[i].isEmpty())
				System.out.print("EMPTY ");
			else
				System.out.print(bullets[i] + " ");
		}*/


		if(gameStateReceiver != null){
			gameStateReceiver.updateBullets(id, bullets);
		}
	}

	private void getRemainingTime(String sentence) {

		String time = sentence.split(":")[1];

		//do stuff here to update the UI

		if (debug) System.out.println("remaining time on client: " + time);

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
}