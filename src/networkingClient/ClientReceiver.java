package networkingClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import enums.TeamEnum;
import gui.GUIManager;
import javafx.application.Platform;
import javafx.scene.image.Image;
import logic.LocalPlayer;
import networkingInterfaces.ClientPlayerOld;
import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;
import physics.ClientPlayer;
import rendering.Map;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;
// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.
/**
 * Class to get messages from client, process and put appropriate message for a client.
 */
public class ClientReceiver extends Thread {
	private int clientID;
	private BufferedReader fromServer;
	private ClientSender sender;
	private MessageQueue myMsgQueue;
	private Message msg;
	private GUIManager m;
	private ClientPlayer cPlayer;
	private ArrayList<LocalPlayer> myTeam;
	private ArrayList<LocalPlayer> enemies;
	
	/**
	 * Construct the class, setting passed variables to local objects.
	 * @param Cid The ID of the client.
	 * @param reader Input stream reader for data.
	 * @param sender Sender class for sending messages to the client.
	 */
	public ClientReceiver(int Cid, BufferedReader reader, ClientSender sender, MessageQueue msgQueue, GUIManager m)
	{
		this.m = m;
		clientID = Cid;
		fromServer = reader;
		this.sender = sender;
		myMsgQueue = msgQueue;
		myTeam = new ArrayList<>();
		enemies = new ArrayList<>();
	}
	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	//Main integration component
	//@atp575
	public void run() {
		try {
			while (true) {
				//Get input from the client read stream.
				String text = fromServer.readLine();
				//If text isn't null and does not read "Exit:Client" do...
				if(text != null && text.compareTo("Exit:Client") != 0){
					//System.out.println("Received: " + text);
					// Protocols
					//In-game messages
					//Temporary sketch of sending player positions
					if (text.contains("Move")){
						moveAction(text);
					}
					
					if (text.contains("Bullet")){
						bulletAction(text);
					}
					
					if(text.contains("Ret:Red:"))
					{
						System.out.println("Got red");
						String[] red = text.substring(8).split("-");
						for (String r: red) {
							System.out.println("Got red:" + r);
						}
						m.updateRedLobby(red);
					}
					else if(text.contains("Ret:Blue:"))
					{
						System.out.println("Got blue");
						String[] blue = text.substring(9).split("-");
						for (String r: blue) {
							System.out.println("Got blue:" + r);
						}
						m.updateBlueLobby(blue);
					}
					else if(text.contains("Ret:Username:"))
					{
					}
					else if(text.contains("LTime:")){
						//m.setTimerStarted();
						String remTime = text.split(":")[1];
						int time = Integer.parseInt(remTime);
						m.setTimeLeft(time);
						m.setTimerStarted();
						System.out.println("Lobby has " + time + " left");
					}
					else if(text.contains("StartGame")){
						startGameAction(text);
					}
					else if(text.contains("EndGame"))
					{
						System.out.println("Game has ended for player with ID " + clientID);
						// Get data about scores, and pass into transition method
						int someScore = 0;
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								m.transitionTo("EndGame", someScore);
							}
							
						});
						
					}
					else if(text.contains("TimerStart"))
					{
						System.out.println("Timer Started");
						// Do stuff here, we have 10 secs till game start message sent.
						m.setTimerStarted();
					}
				}
				else // if the client wants to exit the system.
				{
					sender.stopThread();
					return;
				}
			}
		}
		catch (IOException e) {
			//If there is something wrong... exit cleanly.
			sender.stopThread();
			return;
		}
	}
	
	private void bulletAction(String text) {
		// Protocol message: SendToAll:Bullet:x:y:angle:
		String[] data = text.split(":");
		
		double x = Double.parseDouble(data[2]);
		double y = Double.parseDouble(data[3]);
		double angle = Double.parseDouble(data[4]);
		
		//LocalPlayer p = getPlayerWithID(id)
		
	}
	public ClientPlayer getClientPlayer(){
		return cPlayer;
	}
	
	/**
	 * Contains everything that needs to be done when a player receives the start signal: 
	 * take the client's id and team, then form the team and the enemy team.
	 * This information is then used by the renderer.
	 * @param text The text received from the server.
	 */
	public void startGameAction(String text){
		//get all the relevant data from the message : StartGame:2:Red:1:Red:
		String[] data = text.split(":");
		
		clientID = Integer.parseInt(data[1]);
		String team = data[2];
		Map map = Map.loadRaw("elimination");
		
		//add myself to my team
		//create my client
		if (team.equals("Red"))
			cPlayer = new ClientPlayer(map.getSpawns()[clientID - 1].x * 64, map.getSpawns()[clientID - 1].y * 64, clientID, false, map, m.getAudioManager(), TeamEnum.RED, this);
		else 
			cPlayer = new ClientPlayer(map.getSpawns()[clientID + 3].x * 64, map.getSpawns()[clientID + 3].y * 64, clientID, false, map, m.getAudioManager(), TeamEnum.BLUE, this);
		
		//extract the other members
		for (int i = 3; i < data.length-1; i=i+2){
			int id = Integer.parseInt(data[i]);
			if ( data[i+1].equals(team)){
				if (team.equals("Red"))
					myTeam.add(new LocalPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id, TeamEnum.RED));
				else
					myTeam.add(new LocalPlayer(map.getSpawns()[id - 1].x * 64, map.getSpawns()[id - 1].y * 64, id, TeamEnum.BLUE));
			}
			else{
				if (team.equals("Red"))
					enemies.add(new LocalPlayer(map.getSpawns()[id+3].x * 64, map.getSpawns()[id+3].y * 64, id, TeamEnum.RED));
				else
					enemies.add(new LocalPlayer(map.getSpawns()[id+3].x * 64, map.getSpawns()[id+3].y * 64, id, TeamEnum.BLUE));
			}
		}
		cPlayer.setEnemies(enemies);

		//for debugging
		System.out.println("game has started for player with ID " + clientID);
			
		Platform.runLater(new Runnable() {
				@Override
				public void run() {
					m.transitionTo("Elimination", null);
				}
		});
		}
	
		/**
		 * Gets a move signal from the server about a specific player. The method finds that player and updates
		 * the player's position on the map accordingly.
		 * @param text The protocol message containing the new x and y coordinates, as well as the angle of the player.
		 */
		public void moveAction(String text){
			String[] msg = text.split(":");
			//System.out.println("Text move action: " + Arrays.toString(msg));
			
			int id = Integer.parseInt(msg[2]);
			double x = Double.parseDouble(msg[3]);
			double y = Double.parseDouble(msg[4]);
			double angle = Double.parseDouble(msg[5]);
			
			//for(LocalPlayer p : myTeam)
				//System.out.println(p.getPlayerId());
			
			if (id != clientID){
				//find the player that need to be updated
				LocalPlayer p = getPlayerWithID(id);
				p.tick(x, y, angle);
			}
			
		}
		
		/**
		 * Retrieves a player with a specific id from the current game.
		 * @param id The player's id.
		 * @return The player with the given id.
		 */
		public LocalPlayer getPlayerWithID(int id){
			//Check if the Player is in my team
			for(LocalPlayer p: myTeam)
				if (p.getPlayerId() == id)
					return p;
			
			//otherwise, player is in the enemy team
			for(LocalPlayer p: enemies)
				if (p.getPlayerId() == id)
					return p;
			
			return null;
			
		}
			
		/**
		 * Returns the players that are in this Player's team. 
		 * @return
		 */
		public ArrayList<LocalPlayer> getMyTeam(){
			return myTeam;
		}
		
		/**
		 * Return all the players that are not in this Player's team.
		 * @return
		 */
		public ArrayList<LocalPlayer> getEnemies(){
			return enemies;
		}
		
		public ClientSender getSender(){
			return sender;
		}
}