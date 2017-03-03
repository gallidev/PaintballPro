package networking.server;
import java.util.ArrayList;
import java.util.concurrent.*;

import networking.shared.MessageQueue;
import players.ServerBasicPlayer;
/**
 * Class to store important client-related information used by Client and Server.
 * 
 * @author MattW
 */
public class ClientTable {
	//Structures storing relevant data
	
	//Each client has a message queue.
	private ConcurrentMap<Integer,MessageQueue> queueTable  = new ConcurrentHashMap<Integer,MessageQueue>();
	//Each client has a udp message queue.
	private ConcurrentMap<String,MessageQueue> UDPqueueTable  = new ConcurrentHashMap<String,MessageQueue>();
	//Each client has a status relating to whether or not they are in a game.
	private ConcurrentMap<Integer,Boolean> inGameStatus = new ConcurrentHashMap<Integer,Boolean>();
	//Each client has a game score.
	private ConcurrentMap<Integer,Integer> Scores = new ConcurrentHashMap<Integer,Integer>();
	//Each client has a Player object..
	private ConcurrentMap<Integer, ServerBasicPlayer> playerInstances = new ConcurrentHashMap<Integer, ServerBasicPlayer>();
	//Stores table of IP addresses and corresponding Client IDs.
	private ConcurrentMap<String, Integer> clientIPTable = new ConcurrentHashMap<String, Integer>();
	//Stored table of Client IDs and their related IP addresses.
	private ConcurrentMap<Integer, String> clientIPTableIDKEY = new ConcurrentHashMap<Integer, String>();
	
	//Each user will have an incrementing unique id - allows multiple people with the same nickname.
	private int id = 1;
	
	/**
	 * Add client information to all of the relevant data structures.
	 * @param nickname The nickname of the Client.
	 * @return The id of the client that has just been added to the table.
	 */
	public synchronized int add(String nickname) 
	{
		queueTable.put(id, new MessageQueue()); //Make a new queue for the client.
		inGameStatus.put(id, false); //Set initial in-game status as false.
		Scores.put(id, 0); //Set score as 0.
		ServerBasicPlayer player = new ServerBasicPlayer(id);
		player.setUsername(nickname);
		playerInstances.put(id,player);
		id++; //Increment current id value for next client to connect.
		return (id-1); //Return this client's id value.
	}
	
	/**
	 * Add an ip and message queue.
	 * @param ip IP address to relate a message queue to.
	 */
	public synchronized void addUDPQueue(String ip)
	{
		UDPqueueTable.put(ip, new MessageQueue());
	}
	
	/**
	 * Get the UDP message queue for a client with particular ID.
	 * @param clientID ID of the client.
	 * @return UDP message queue.
	 */
	public synchronized MessageQueue getUDPQueueWithID(int clientID)
	{
		return UDPqueueTable.get(clientIPTable.get(clientID));
	}
	
	/**
	 * Get the UDP message queue for a client with particular IP.
	 * @param ip IP of the client.
	 * @return UDP message queue.
	 */
	public synchronized MessageQueue getUDPQueueWithIP(String ip)
	{
		return UDPqueueTable.get(ip);
	}
	
	/**
	 * Add a new IP address to relevant tables.
	 * @param ip IP address to add.
	 * @param clientID ID of the related client.
	 */
	public synchronized void addNewIP(String ip, int clientID)
	{
		clientIPTable.put(ip,clientID);
		clientIPTableIDKEY.putIfAbsent(clientID, ip);
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	public synchronized int getID(String ip)
	{
		return clientIPTable.get(ip);
	}
	
	public synchronized String getIP(int id)
	{
		return clientIPTableIDKEY.get(id);
	}
	
	public synchronized ServerBasicPlayer getPlayer(int clientID)
	{
		return playerInstances.get(clientID);
	}
	
	/**
	 * Remove client information from the data structures.
	 * @param clientID The id of the client to remove from the tables.
	 */
	public synchronized void removeClient(int clientID)
	{
		queueTable.remove(clientID);
		inGameStatus.remove(clientID);
		Scores.remove(clientID);
		playerInstances.remove(clientID);
	}
	
	/**
	 * Set the 'in game' status of the client to true.
	 * @param player1 ID of client game Player 1.
	 * @param player2 ID of client game Player 2.
	 */
	public synchronized void setGame(int player1, int player2)
	{
		inGameStatus.replace(player1, true);
		inGameStatus.replace(player2, true);
	}
	
	/**
	 * Set the 'in game' status of the client to false.
	 * @param player1 ID of client game Player 1.
	 * @param player2 ID of client game Player 2.
	 */
	public synchronized void unsetGame(int player1, int player2)
	{
		inGameStatus.replace(player1, false);
		inGameStatus.replace(player2, false);
	}
	/**
	 * Increment the score of the winning client in a game.
	 * @param clientID The ID of the client who won the game.
	 */
	public void wonGame(int clientID)
	{
		Scores.replace(clientID,((this.getScore(clientID))+1));  
	}
	/**
	 * Get the score of a client.
	 * @param clientID The id of the client to get the score of.
	 * @return The score of the passed client.
	 */
	public int getScore(int clientID)
	{
		return Scores.get(clientID);
	}
	/**
	 * Returns all of the scores stored in the table score data structure.
	 * @return ArrayList of all scores.
	 */
	public ArrayList<Integer> getScores()
	{
		ArrayList<Integer> scores = new ArrayList<>();
		//Cycle through all score values and add to an ArrayList.
		for (int key : queueTable.keySet()) {
			scores.add(Scores.get(key));
		}
		return scores;
	}
	/**
	 * Get the game status of a particular client.
	 * @param clientID The id of the client.
	 * @return The game status of the client.
	 */
	public boolean gameStatus(int clientID)
	{
		return inGameStatus.get(clientID); 
	}
	/**
	 * Get the message queue of a client. 
	 * @param clientID The id of the client to get the message id for.
	 * @return The message queue of the client.
	 */
	public MessageQueue getQueue(int clientID) {
		//Null if not in the table.
		return queueTable.get(clientID);
	}
	public MessageQueue getUDPqueue(int clientID) {
		return UDPqueueTable.get(clientID);
	}
}