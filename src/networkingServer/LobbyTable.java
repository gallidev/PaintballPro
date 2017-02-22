package networkingServer;

import java.util.concurrent.*;

/**
 * Class to store important client-related information used by Client and
 * Server.
 */
public class LobbyTable {
	// Structures storing relevant data.

	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort

	// Must check if max player number is reached before trying to add new
	// players.

	// Each open lobby is stored here.
	private ConcurrentMap<Integer, Lobby> lobbyList = new ConcurrentHashMap<Integer, Lobby>();

	// Each lobby will have an incrementing unique id - allows for each lobby to
	// be identified.
	private int id = 1;

	/**
	 * Remove client information from the data structures.
	 * 
	 * @param clientID
	 *            The id of the client to remove from the tables.
	 */

	// run a thread to check if a lobby has 0 players, if it does, remove it
	public synchronized void removeLobby(int lobbyID) {
		// We reset the allocated lobby to each of our players.
		for (Player player : lobbyList.get(lobbyID).getPlayers()) {
			player.setAllocatedLobby(-1);
		}
		// We then remove the lobby from the list.
		lobbyList.remove(lobbyID);
	}

	public synchronized void removePlayer(Player playerToRemove) {
		Lobby allocatedLobby = lobbyList.get(playerToRemove.getAllocatedLobby());
		allocatedLobby.removePlayer(playerToRemove);
	}

	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort
	public synchronized void addPlayerToLobby(Player player, int gameMode, ServerMsgReceiver receiver) {
		boolean addedToGame = false;
		int lobbyAllocated = 0;
		for (Lobby lobby : lobbyList.values()) {
			if (lobby.getGameType() == gameMode && !lobby.isMaxPlayersReached()) {
				lobby.addPlayer(player, 0); // 0 indicated add at next available place.
				lobbyAllocated = lobby.getID();
				addedToGame = true;
				break;
			}

		}
		if (!addedToGame) // all lobbies of that type are full, make a new one.
		{
			Lobby newLobby = new Lobby(id, gameMode);
			newLobby.addPlayer(player, 0);
			lobbyAllocated = newLobby.getID();
			lobbyList.put(id, newLobby);
			id++;
		}
		player.setAllocatedLobby(lobbyAllocated);
		if (this.getLobby(lobbyAllocated).isMaxPlayersReached()) {
			this.getLobby(lobbyAllocated).timerStart(receiver);
			receiver.sendToAll("TimerStart");
		}
		String redMems = "Ret:Red:" + this.getLobby(lobbyAllocated).getTeam(2);
		String blueMems = "Ret:Blue:" + this.getLobby(lobbyAllocated).getTeam(1);
		receiver.sendToAll(redMems);
		receiver.sendToAll(blueMems);
	}

	public synchronized Lobby getLobby(int lobbyId) {
		return lobbyList.get(lobbyId);
	}

	public synchronized void switchTeams(Player player, ServerMsgReceiver receiver) {
		lobbyList.get(player.getAllocatedLobby()).switchTeam(player, receiver);
	}
}
