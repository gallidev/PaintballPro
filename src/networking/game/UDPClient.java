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

	DatagramSocket clientSocket;
	InetAddress IPAddress;

	GUIManager m;

	TeamTable teams;
	private ClientGameStateReceiver gameStateReceiver;

	/**
	 * We establish a connection with the UDP server... we tell it we are connecting for the first time so that
	 * it stores our information server-side.
	 * @param clientID ID allocated to the client.
	 * @param udpServIP IP for the server-side UDP socket.
	 * @param guiManager Manager of GUI.
	 * @param teams Both client's and opposing teams.
	 */
	public UDPClient(int clientID, String udpServIP, GUIManager guiManager, TeamTable teams, int portNum)
	{
		int port = portNum;
		// 9877
		this.clientID = clientID;
		this.m = guiManager;
		this.teams = teams;

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
			if(debug) System.err.println(e.getStackTrace());
		}
	}

	/**
	 * Loop through, reading messages from the server.
	 * Main method, ran when the thread is started.
	 */
	public void run()
	{
		try{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			while(true)
			{
				clientSocket.receive(receivePacket);
				String sentSentence = new String(receivePacket.getData());
				if(debug) System.out.println("Received from server:"+sentSentence);
				
				// In-game messages
				switch(sentSentence.charAt(0)){
				
					case '1' : updatePlayerAction(sentSentence) ;
							   break;
					case '3' : updateScoreAction(sentSentence);
							   break;

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
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
		}
		catch(Exception e)
		{
			//e.printStackTrace(System.out);
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
	}

	/**
	 * Action starting when a player fires a bullet. It renders the bullet and
	 * also detects when a player has been eliminated.
	 *
	 * @param text
	 *            The protocol text containing information about the coordinates
	 *            and angle of the bullet, as well as the player id which shot
	 *            it.
	 *
	 * @author Alexandra Paduraru and Matthew Walters
	 */
	private void bulletAction(String text) {
//		// Protocol message: SendToAll:Bullet:id:team:x:y:angle:
//		String[] data = text.split(":");
//
//		int id = Integer.parseInt(data[2]);
//		String t = data[3];
//
//		ClientLocalPlayer p = (ClientLocalPlayer) getPlayerWithID(id);
//
//		if (p != null) // the player is not us
//		{
//			ArrayList<Bullet> firedBullets = new ArrayList<>();
//			for (int i = 4; i < data.length - 2; i = i + 3) {
//
//				double x = Double.parseDouble(data[i]);
//				double y = Double.parseDouble(data[i + 1]);
//				double angle = Double.parseDouble(data[i + 2]);
//
//				firedBullets.add(new Bullet(x, y, angle, p.getTeam()));
//			}
//			p.tickBullets(firedBullets);
//		}
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
	 * @author Alexandra Paduraru and Matthew Walters
	 */
	public void moveAction(String text) {
//		String[] msg = text.split(":");
//		if(debug) System.out.println("Text move action: " + text);
//
//		int id = Integer.parseInt(msg[2]);
//		double x = Double.parseDouble(msg[3]);
//		double y = Double.parseDouble(msg[4]);
//		double angle = Double.parseDouble(msg[5]);
//
//		if (id != clientID) {
//			// find the player that need to be updated
//			GhostPlayer p = getPlayerWithID(id);
//			p.tick(x, y, angle);
//		}
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