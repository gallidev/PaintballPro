package networking.server;

import java.util.concurrent.*;
import networking.game.UDPServer;
import players.ServerBasicPlayer;

/**
 * Class to store important client-related information used by Client and
 * Server.
 * 
 * @author Matthew Walters
 */
public class LobbyTable {
	// Structures storing relevant data.

	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort

	// Each open lobby is stored here.
	private ConcurrentMap<Integer, Lobby> lobbyList = new ConcurrentHashMap<Integer, Lobby>();

	// Each lobby will have an incrementing unique id - allows for each lobby to
	// be identified.
	private int id = 1;

	public boolean testEnv = false;

	/**
	 * Remove lobby information from the data structures.
	 * 
	 * @param lobbyID
	 *            The id of the lobby to remove from the tables.
	 */
	// run a thread to check if a lobby has 0 players, if it does, remove it
	public synchronized void removeLobby(int lobbyID) {
		// We reset the allocated lobby to each of our players.
		for (ServerBasicPlayer player : lobbyList.get(lobbyID).getPlayers()) {
			player.setAllocatedLobby(-1);
		}
		// We then remove the lobby from the list.
		lobbyList.remove(lobbyID);
	}

	/**
	 * Remove a player and all of their information from the lobby.
	 * 
	 * @param playerToRemove
	 *            Player to remove from the lobby.
	 */
	public synchronized int removePlayer(ServerBasicPlayer playerToRemove) {
		try {
			Lobby allocatedLobby = lobbyList.get(playerToRemove.getAllocatedLobby());
			allocatedLobby.removePlayer(playerToRemove);
			playerToRemove.setAllocatedLobby(-1);
			if (allocatedLobby.getCurrPlayerTotal() == 0)
				lobbyList.remove(allocatedLobby.getID());
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	/**
	 * Add a player and all relevant information to a lobby.
	 * 
	 * @param player
	 *            Player to add
	 * @param gameMode
	 *            Mode of gameplay selected - Team Match, Capture the flag etc.
	 * @param receiver
	 *            Server Receiver used to retrieve/send messages between clients
	 * @param udpReceiver
	 *            Server Receiver used to retrieve/send messages between clients
	 *            in-game
	 */
	public synchronized void addPlayerToLobby(ServerBasicPlayer player, int gameMode, ServerReceiver receiver,
			UDPServer udpReceiver) {
		boolean addedToGame = false;
		int lobbyAllocated = 0;
		for (Lobby lobby : lobbyList.values()) {
			if (lobby.getGameType() == gameMode && !lobby.isMaxPlayersReached() && !lobby.inGameStatus) {
				lobby.addPlayer(player, 0); // 0 indicated add at next available
											// place.
				lobbyAllocated = lobby.getID();
				addedToGame = true;
				break;
			}
		}
		if (!addedToGame) // all lobbies of that type are full, make a new one.
		{
			Lobby newLobby = new Lobby(id, gameMode, testEnv);
			newLobby.addPlayer(player, 0);
			lobbyAllocated = newLobby.getID();
			lobbyList.put(id, newLobby);
			id++;
		}

		player.setAllocatedLobby(lobbyAllocated);
		if (this.getLobby(lobbyAllocated).isMaxPlayersReached()) {
			this.getLobby(lobbyAllocated).timerStart(receiver, udpReceiver, gameMode);
			receiver.sendToAll("TimerStart");
		}
		String redMems = "Ret:Red:" + this.getLobby(lobbyAllocated).getTeam(2);
		String blueMems = "Ret:Blue:" + this.getLobby(lobbyAllocated).getTeam(1);
		if (!testEnv) {
			receiver.sendToAll(redMems);
			receiver.sendToAll(blueMems);
		}
	}

	/**
	 * Retrieve a particular lobby
	 * 
	 * @param lobbyId
	 *            ID of lobby to retrieve
	 * @return Lobby.
	 */
	public synchronized Lobby getLobby(int lobbyId) {
		return lobbyList.get(lobbyId);
	}

	/**
	 * Switch a player's allocated team.
	 * 
	 * @param player
	 *            Player to switch.
	 * @param receiver
	 *            Server Receiver object just to send messages to client.
	 */
	public synchronized void switchTeams(ServerBasicPlayer player, ServerReceiver receiver) {
		lobbyList.get(player.getAllocatedLobby()).switchTeam(player, receiver);
	}
}