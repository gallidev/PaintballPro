package networkingServer;


import java.util.ArrayList;
import java.util.concurrent.*;

import networkingSharedStuff.MessageQueue;

/**
 * Class to store important client-related information used by Client and Server.
 */
public class LobbyTable {
	//Structures storing relevant data.
	
	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort
	
	// Must check if max player number is reached before trying to add new players.
	
	//Each open lobby is stored here.
	private ConcurrentMap<Integer,Lobby> lobbyList = new ConcurrentHashMap<Integer,Lobby>();

	//Each lobby will have an incrementing unique id - allows for each lobby to be identified.
	private int id = 1;

	/**
	 * Add client information to all of the data structures.
	 * @param nickname The nickname of the Client.
	 * @return The id of the client that has just been added to the table.
	 */
	public synchronized int add(Lobby lobbyToAdd) 
	{
		lobbyList.put(id, lobbyToAdd);
		id++; //Increment current id value for next client to connect.

		return (id-1); //Return this client's id value.
	}
	
	/**
	 * Remove client information from the data structures.
	 * @param clientID The id of the client to remove from the tables.
	 */
	public synchronized void removeLobby(int lobbyID)
	{
		lobbyList.remove(lobbyID);
	}
	
	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort
	public synchronized int addPlayerToLobby(Player player, int gameMode)
	{
		boolean addedToGame = false;
		int lobbyAllocated = 0;
		for(Lobby lobby : lobbyList.values())
		{
			if(lobby.getGameType() == gameMode && !lobby.isMaxPlayersReached())
			{	
				lobby.addPlayer(player, 0); // 0 indicated add at next available place.
				lobbyAllocated = lobby.getID();
				addedToGame = true;
				break;
			}
			
		}
		if(!addedToGame) // all lobbies of that type are full, make a new one.
		{
			Lobby newLobby = new Lobby(id,gameMode);
			newLobby.addPlayer(player, 0);
			lobbyAllocated = newLobby.getID();
			lobbyList.put(id, newLobby);
			id++;
		}
		return lobbyAllocated;
	}
	
	
}
