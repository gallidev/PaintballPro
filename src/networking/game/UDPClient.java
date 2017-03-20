package networking.game;

import enums.TeamEnum;
import gui.GUIManager;
import integrationClient.ClientGameStateReceiver;
import javafx.application.Platform;
import networking.client.TeamTable;
import players.EssentialPlayer;
import players.GhostPlayer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Client-side Sender and Receiver using UDP protocol for in-game transmission.
 * One per client.
 *
 * @author Matthew Walters
 */
public class UDPClient extends Thread {

	public static double PINGDELAY = 0;

	public boolean bulletDebug = false;
	public boolean connected = false;
	public boolean testSendToAll = false;
	public boolean testIntegration = false;
	private boolean debug = false;
	private int clientID;
	private String nickname;
	private ClientGameStateReceiver gameStateReceiver;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
	private GUIManager m;
	private TeamTable teams;
	private int sIP;
	private boolean active = true;
	private GUIManager guiManager;

	private HashMap<Integer, Long> lastPlayedEliminatedSound = new HashMap<Integer, Long>();


	/**
	 * We establish a connection with the UDP server... we tell it we are connecting for the first time so that
	 * it stores our information server-side.
	 *
	 * @param clientID ID allocated to the client.
	 * @param udpServIP IP for the server-side UDP socket.
	 * @param guiManager Manager of GUI.
	 * @param teams Both client's and opposing teams.
	 * @param portNum port to send and receive packets.
	 * @param nickname Nickname of client.
	 */
	public UDPClient(int clientID, String udpServIP, int udpServPort, GUIManager guiManager, TeamTable teams, int portNum, String nickname)
	{
		int port = portNum;
		this.clientID = clientID;
		this.m = guiManager;
		this.teams = teams;
		this.nickname = nickname;
		this.guiManager = guiManager;

		sIP = udpServPort;

		if(debug) System.out.println("Making new UDP Client");

		// Let's establish a connection to the running UDP server and send our client id.
		boolean error = true;
		while (error) {
			error = false;
			try {
				if (debug) System.out.println("Attempting to make client socket");

				boolean run = true;
				while(run)
				{
					try
					{
						clientSocket = new DatagramSocket(port);
						run = false;
					}
					catch(Exception e)
					{
						port++;
					}
				}

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
				if (debug) e.printStackTrace();
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

			    if (receivedPacket.contains("Exit"))
			    {
			    	clientSocket.close();
			    	return;
			    }
				else if (receivedPacket.contains("TestSendToAll"))
				{
					testSendToAll = true;
				}
				else if (receivedPacket.contains("0:1:Up:Left:Right:Shoot:2:3"))
				{
					testIntegration = true;
				}

				switch(receivedPacket.charAt(0)){

					case '1' : updatePlayerAction(receivedPacket) ;
							   break;
					case '2' : getWinnerAction(receivedPacket);
							   break;
					case '3' : updateScoreAction(receivedPacket);
							   break;
					case '4' : generateBullet(receivedPacket);
							   break;
					case '6' : getRemainingTime(receivedPacket);
							   break;
//					case '7' : capturedFlagAction(receivedPacket);
//							   break;
					case '8' : capturedFlagAction(receivedPacket);
							   break;
					case '7' : lostFlagAction(receivedPacket);
							   break;
					case '!' : baseFlagAction(receivedPacket);
							   break;
					case 'T' : pingTimeUpdate(receivedPacket);
							   break;
					case '@' : hitWallAction(receivedPacket);
							   break;
					case '#' : eliminatedPlayerAction(receivedPacket);
							   break;
					case '$' : powerUpAction(receivedPacket);
							   break;
					case '%' : shieldRemovedAction(receivedPacket);
							   break;
				}

			}
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					e.printStackTrace();
				}
			});
			if(debug) System.out.println("Closing Client.");
			if(clientSocket.isConnected())
				clientSocket.close();
		}
		System.out.println("Closing UDP Client");
	}

	//use this to switch back to the normal player image
	private void shieldRemovedAction(String receivedPacket) {
		int id = Integer.parseInt(receivedPacket.split(":")[1]);

		//System.out.println("player with id " + id + " does not have shield anymore" );

	}

	//use this to remove powerups from maps and change to the shield player image
	private void powerUpAction(String receivedPacket) {
		int id = Integer.parseInt(receivedPacket.split(":")[2]);

		switch (receivedPacket.split(":")[1])
		{
		case "0" : //powerup is speed
					System.out.println("Player " +id +" took speed powerup");
					break;
		case "1" : System.out.println("Player" + id +" took shield powerup");
				   break;
		}



	}

	private void getWinnerAction(String text) {
		// Protocol: 2:Red/Blue:RedScore:BlueScore

		//String winner = text.split(":")[1];
		String redScore = text.split(":")[2];
		String blueScore = text.split(":")[3];

		Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(GUIManager.renderer.getHud() != null)
							GUIManager.renderer.getHud().setWinner(Integer.parseInt(redScore), Integer.parseInt(blueScore));
					}
				});
		active = false;
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
			if (debug) e.printStackTrace();
			e.printStackTrace();
		}
	}

	public void stopThread()
	{
		super.interrupt();
		if(clientSocket.isConnected())
			clientSocket.close();
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

		if(!text.equals("")){
			String[] actions = text.split(":");

			int id = Integer.parseInt(actions[1]);
			double x = Double.parseDouble(actions[2]);
			double y = Double.parseDouble(actions[3]);
			double angle = Double.parseDouble(actions[4]);

			boolean visibility = true;
			if (actions[5].equals("false"))
				visibility = false;

			boolean eliminated = false;
			if (actions[6].equals("true"))
				eliminated = true;

//			if (visibility == false)
//				System.out.println("I'm invisible " + id);

			if(gameStateReceiver != null){
				gameStateReceiver.updatePlayer(id, x, y, angle, visibility, eliminated);
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

		if (GUIManager.renderer!= null && GUIManager.renderer.getHud() != null){

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					GUIManager.renderer.setRedScore(redScore);
					GUIManager.renderer.setBlueScore(blueScore);
				}
			});
		}

		if (debug) System.out.println("Red score: " + redScore);
		if (debug) System.out.println("Blue score: " + blueScore);

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
			//gameStateReceiver.updateBullets(id, bullets);
		}
	}

	public void generateBullet(String text){
		// Protocol message: 4:id:idBullet:x:y:...

		int id = Integer.parseInt(text.split(":")[1]);

		//get all the bullets
		String[] data = text.split(":");

		if(gameStateReceiver != null){
			gameStateReceiver.updateBullets(id);
		}
	}

	private void getRemainingTime(String sentence) {

		String time = sentence.split(":")[1];

		if (debug) System.out.println("remaining time on client: " + time);
		if (GUIManager.renderer!= null && GUIManager.renderer.getHud() != null){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (GUIManager.renderer.getHud() != null)
						GUIManager.renderer.setTimeRemaining(Integer.parseInt(time));
				}
			});
		}
	}

	private void capturedFlagAction(String text){
		//Protocol : 8:<id>
		int id = Integer.parseInt(text.split(":")[1]);

		if(gameStateReceiver != null){
			gameStateReceiver.updateFlag(id);

			System.out.println("flag captured");
		}

	}

	private void lostFlagAction(String text){
		//Protocol : 9:<id>
		int id = Integer.parseInt(text.split(":")[1]);

		if(gameStateReceiver != null){
			gameStateReceiver.lostFlag(id);
		}

		System.out.println("flag lost");

	}

	private void baseFlagAction(String text){
		//Protocol : !:<id>

		int id = Integer.parseInt(text.split(":")[1]);
		double x = Double.parseDouble(text.split(":")[2]);
		double y = Double.parseDouble(text.split(":")[3]);

		if(gameStateReceiver != null){
			gameStateReceiver.respawnFlag(id, x, y);
		}
		System.out.println("flag rebased");
	}

	private void hitWallAction(String text){
		//Protocol: "@:<x>:<y>"

		double x = Double.parseDouble(text.split(":")[1]);
		double y = Double.parseDouble(text.split(":")[2]);
		String colour = text.split(":")[3];

	}

	private void pingTimeUpdate(String receivedPacket) {
		//Protocol: T:id:SentfromCLientTime:ReceivedAtServerTime
		String[] actions = receivedPacket.split(":");

		long ClientTime = Long.parseLong(actions[2]);

		System.out.println("toServerAndBack ping : " + (System.currentTimeMillis() - ClientTime));
		PINGDELAY = (System.currentTimeMillis() - ClientTime) + 20;



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
	private EssentialPlayer getPlayerWithID(int id) {
		// Check if the Player is in my team
		for (EssentialPlayer p : teams.getMyTeam())
			if (p.getPlayerId() == id)
				return p;

		// otherwise, player is in the enemy team
		for (EssentialPlayer p : teams.getEnemies())
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

	private void eliminatedPlayerAction(String text){
		int id = Integer.parseInt(text.split(":")[1]);

		if (lastPlayedEliminatedSound.containsKey(id)) {
			if (lastPlayedEliminatedSound.get(id) + 3000 < System.currentTimeMillis()) {
				guiManager.getAudioManager().playSFX(guiManager.getAudioManager().sfx.splat, (float)1.0);
				lastPlayedEliminatedSound.put(id, System.currentTimeMillis());
			}
		} else {
			guiManager.getAudioManager().playSFX(guiManager.getAudioManager().sfx.splat, (float)1.0);
			lastPlayedEliminatedSound.put(id, System.currentTimeMillis());
		}


		//System.out.println("Player " + id + " eliminated");
	}

	public boolean isActive(){
		return active;
	}
}