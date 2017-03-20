package networking.server;

import java.util.concurrent.*;
import networking.shared.MessageQueue;
import players.ServerBasicPlayer;

/**
 * Class to store important client-related information used by Client and Server.
 * 
 * @author Matthew Walters
 */
public class ClientTable {
	
	//Structures storing relevant data
	
	//Each client has a message queue.
	private ConcurrentMap<Integer,MessageQueue> queueTable  = new ConcurrentHashMap<Integer,MessageQueue>();
	//Each client has a udp message queue.
	private ConcurrentMap<String,MessageQueue> UDPqueueTable  = new ConcurrentHashMap<String,MessageQueue>();
	//Each client has a Player object..
	private ConcurrentMap<Integer, ServerBasicPlayer> playerInstances = new ConcurrentHashMap<Integer, ServerBasicPlayer>();
	//Stores table of IP addresses and corresponding Client IDs.
	private ConcurrentMap<String, Integer> clientIPTable = new ConcurrentHashMap<String, Integer>();
	//Stored table of Client IDs and their related IP addresses.
	private ConcurrentMap<Integer, String> clientIPTableIDKEY = new ConcurrentHashMap<Integer, String>();
	
	//Each user will have an incrementing unique id - allows multiple people with the same nickname.
	private int id = 1;
	
	private boolean debug = false;
	
	/**
	 * Add client information to all of the relevant data structures.
	 * @param nickname The nickname of the Client.
	 * @return The id of the client that has just been added to the table.
	 */
	public synchronized int add(String nickname) 
	{
		queueTable.put(id, new MessageQueue()); //Make a new queue for the client.
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
	 * Get the id of a client given a corresponding ip.
	 * @param ip IP of the connected client.
	 * @return Id of the client.
	 */
	public synchronized int getID(String ip)
	{
		return clientIPTable.get(ip);
	}
	
	/**
	 * Get the ip of a client given a corresponding id.
	 * @param id ID of client. 
	 * @return IP of client.
	 */
	public synchronized String getIP(int id)
	{
		return clientIPTableIDKEY.get(id);
	}

	/**
	 * Get player instance of a particular client.
	 * @param clientID ID of client to get their player instance for.
	 * @return Player instance.
	 */
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
		playerInstances.remove(clientID);
		String ip = clientIPTableIDKEY.get(clientID);
		if(ip != null)
		{
					clientIPTableIDKEY.remove(clientID,ip);
					clientIPTable.remove(ip,clientID);
					UDPqueueTable.remove(ip);
		}
	}
	
	/**
	 * Get the message queue of a client. 
	 * @param clientID The id of the client to get the message id for.
	 * @return The message queue of the client.
	 */
	public MessageQueue getQueue(int clientID) {
		if (debug) {
			for(Integer i : queueTable.keySet())
				System.out.print(i + " ");
		}
		//Null if not in the table.
		return queueTable.get(clientID);
	}

	/**
	 * Checks whether a username has been used.
	 * @param username Username to check.
	 * @return True if username is available, false if not available.
	 */
	public boolean checkUsernameAvailable(String username) {
		for(ServerBasicPlayer player : playerInstances.values())
		{
			if(player.getUsername().contains(username))
				return false;
		}
		return true;
	}
}