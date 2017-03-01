package networkingGame;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import gui.GUIManager;
import networkingClient.TeamTable;
import physics.Bullet;
import players.ClientLocalPlayer;
import players.GeneralPlayer;

// One per client.
public class UDPClientReceiver extends Thread {

	private boolean debug = false;
	private int clientID;
	
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	
	GUIManager m;
	
	TeamTable teams;
	
	// We establish a connection with the UDP server... we tell it we are connecting for the first time so that
	// it stores our information server-side.
	public UDPClientReceiver(int clientID, String udpServIP, GUIManager m, TeamTable teams)
	{
		this.clientID = clientID;
		this.m = m;
		this.teams = teams;
		// Let's establish a connection to the running UDP server and send our client id.
		try{
			clientSocket = new DatagramSocket();
			IPAddress = InetAddress.getByName(udpServIP);
			String sentence = "Connect:"+clientID;
			sendMessage(sentence);
			
		}
		catch (Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
	}
	
	// We loop, reading messages from the server.
	public void run()
	{
		try{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			while(true)
			{
				clientSocket.receive(receivePacket);
				String sentSentence = new String(receivePacket.getData());
				
				// Do any extra processing here for in-game commands.
				// In-game messages
				if (sentSentence.contains("Move"))
					moveAction(sentSentence);
				else if (sentSentence.contains("Bullet"))
					bulletAction(sentSentence);
				else if (sentSentence.contains("LTime:")) {
					String remTime = sentSentence.split(":")[1];
					int time = Integer.parseInt(remTime);
					m.setTimeLeft(time);
					m.setTimerStarted();
					if(debug) System.out.println("Lobby has " + time + " left");
				}
				else if (sentSentence.contains("Exit"))
					break;
			}
		}
		catch (Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
		clientSocket.close();
	}
	
	// We send messages to the server.
	public void sendMessage(String msg) 
	{	
		try{
			byte[] sendData = new byte[1024];
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
		}
		catch(Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
	}
	
	// -------------------------------------
	// -----------Game Methods--------------
	// -------------------------------------
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
		// Protocol message: SendToAll:Bullet:id:team:x:y:angle:
		String[] data = text.split(":");

		int id = Integer.parseInt(data[2]);
		String t = data[3];

		ClientLocalPlayer p = (ClientLocalPlayer) getPlayerWithID(id);

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
		String[] msg = text.split(":");
		// System.out.println("Text move action: " + Arrays.toString(msg));

		int id = Integer.parseInt(msg[2]);
		double x = Double.parseDouble(msg[3]);
		double y = Double.parseDouble(msg[4]);
		double angle = Double.parseDouble(msg[5]);

		if (id != clientID) {
			// find the player that need to be updated
			ClientLocalPlayer p = (ClientLocalPlayer) getPlayerWithID(id);
			p.tick(x, y, angle);
		}
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
	private GeneralPlayer getPlayerWithID(int id) {
		// Check if the Player is in my team
		for (GeneralPlayer p : teams.getMyTeam())
			if (p.getPlayerId() == id)
				return p;

		// otherwise, player is in the enemy team
		for (GeneralPlayer p : teams.getEnemies())
			if (p.getPlayerId() == id)
				return p;

		return null;
	}
}