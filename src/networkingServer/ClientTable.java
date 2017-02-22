package networkingServer;


import java.util.ArrayList;
import java.util.concurrent.*;

import networkingShared.MessageQueue;

/**
 * Class to store important client-related information used by Client and Server.
 */
public class ClientTable {
	//Structures storing relevant data
	
	//Each client has a message queue.
	private ConcurrentMap<Integer,MessageQueue> queueTable  = new ConcurrentHashMap<Integer,MessageQueue>();
	//Each client has a status relating to whether or not they are in a game.
	private ConcurrentMap<Integer,Boolean> inGameStatus = new ConcurrentHashMap<Integer,Boolean>();
	//Each client has a game score.
	private ConcurrentMap<Integer,Integer> Scores = new ConcurrentHashMap<Integer,Integer>();
	//Each client has a Player object..
	private ConcurrentMap<Integer, Player> playerInstances = new ConcurrentHashMap<Integer, Player>();
	
	//Each user will have an incrementing unique id - allows multiple people with the same nickname.
	private int id = 1;

	/**
	 * Add client information to all of the data structures.
	 * @param nickname The nickname of the Client.
	 * @return The id of the client that has just been added to the table.
	 */
	public synchronized int add(String nickname) 
	{
		queueTable.put(id, new MessageQueue()); //Make a new queue for the client.
		inGameStatus.put(id, false); //Set initial in-game status as false.
		Scores.put(id, 0); //Set score as 0.
		Player player = new Player(id);
		player.setUsername(nickname);
		playerInstances.put(id,player);
		id++; //Increment current id value for next client to connect.

		return (id-1); //Return this client's id value.
	}
	
	public synchronized Player getPlayer(int clientID)
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
}
