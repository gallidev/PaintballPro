package networking.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import enums.TeamEnum;
import logic.RoundTimer;
import networking.game.UDPServer;
import networking.interfaces.ServerGame;
import players.ServerBasicPlayer;
import players.ServerPlayer;
import serverLogic.Team;

/**
 * Class to represent a lobby.
 * 
 * @author MattW
 */
public class Lobby {
	// Structures storing relevant data.

	// Lobby information
	private int id;
	private static final int lobbyTime = 10;
	private boolean inGameStatus;

	// Game information
	private int GameType;
	private int MaxPlayers;
	private ServerGame currentSessionGame;

	// Team information
	private int currPlayerBlueNum;
	private int currPlayerRedNum;
	private ConcurrentMap<Integer, ServerBasicPlayer> blueTeam = new ConcurrentHashMap<Integer, ServerBasicPlayer>();
	private ConcurrentMap<Integer, ServerBasicPlayer> redTeam = new ConcurrentHashMap<Integer, ServerBasicPlayer>();
	private Team red;
	private Team blue;

	/**
	 * Sets passed variables and inialised some defaults.
	 * @param myid ID of lobby.
	 * @param PassedGameType Game mode that the lobby is used for.
	 */
	public Lobby(int myid, int PassedGameType) {
		inGameStatus = false;
		GameType = PassedGameType;
		MaxPlayers = 8;
		currPlayerBlueNum = 0;
		currPlayerRedNum = 0;
		id = myid;
	}

	/**
	 * Retrieve the lobby id.
	 * @return Lobby id.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Get the game status of the current lobby
	 * @return True if in game, False if not in game.
	 */
	public boolean getInGameStatus() {
		return inGameStatus;
	}

	/**
	 * Switches the game status of the lobby.
	 */
	public void switchGameStatus() {
		this.inGameStatus = !this.inGameStatus;
	}

	/**
	 * Retrieve game mode type.
	 * @return Integer representing the game type.
	 */
	public int getGameType() {
		return GameType;
	}

	/**
	 * Returns whether or not max players have been reached.
	 * @return True if max players reached, False otherwise.
	 */
	public boolean isMaxPlayersReached() {
		return getCurrPlayerTotal() == MaxPlayers;
	}

	/**
	 * Retrieves the current number of players in the lobby.
	 * @return Total number of players in the lobby currently.
	 */
	public int getCurrPlayerTotal() {
		return currPlayerBlueNum + currPlayerRedNum;
	}

	/**
	 * Add a player to a team - red of blue.
	 * @param playerToAdd Player object to add to a team.
	 * @param specific Is there a team we should try to allocate to? 0 = random, 1 = blue, 2 = red
	 */
	public void addPlayer(ServerBasicPlayer playerToAdd, int specific) {
		// Add player to teams - alternate teams unless specified (when a client
		// requests to switch teams).
		// We check in LobbyTable if max players is reached.
		
		int totPlayers = getCurrPlayerTotal();
		if (((totPlayers % 2 == 0) && (specific == 0 || specific == 2)) && (currPlayerRedNum <= (MaxPlayers / 2))) {
			redTeam.put(currPlayerRedNum, playerToAdd);
			currPlayerRedNum++;
		} else {
			blueTeam.put(currPlayerBlueNum, playerToAdd);
			currPlayerBlueNum++;
		}
	}

	/**
	 * Remove player from a team and re-order other team members as appropriate.
	 * @param playerToRemove Player obejct to remove from the team.
	 */
	public void removePlayer(ServerBasicPlayer playerToRemove) {
		boolean removed = false;
		int counter = 0;
		for (ServerBasicPlayer player : blueTeam.values()) {
			/*
			 * We look through until we find the player we are looking for, we
			 * then remove this player from the team and shift all of the items
			 * above the player down by one position.
			 */
			if (player.getID() == playerToRemove.getID()) {
				blueTeam.remove(counter);
				currPlayerBlueNum--;
				removed = true;
				for (int i = (counter + 1); i < (MaxPlayers / 2); i++) {
					if (blueTeam.containsKey(i)) {
						blueTeam.replace(i - 1, blueTeam.get(i));
						blueTeam.remove(i);
					}
				}
			}
			counter++;
		}
		if (!removed) {
			counter = 0;
			for (ServerBasicPlayer player : redTeam.values()) {
				/*
				 * We look through until we find the player we are looking for,
				 * we then remove this player from the team and shift all of the
				 * items above the player down by one position.
				 */
				if (player.getID() == playerToRemove.getID()) {
					redTeam.remove(counter);
					currPlayerRedNum--;
					for (int i = (counter + 1); i < (MaxPlayers / 2); i++) {
						if (redTeam.containsKey(i)) {
							redTeam.replace(i - 1, redTeam.get(i));
							redTeam.remove(i);
						}
					}
				}
				counter++;
			}
		}
	}

	/**
	 * Switch a player's team.
	 * @param playerToSwitch Player object to switch.
	 * @param receiver Server Receiver used to retrieve/send messages between clients
	 */
	public void switchTeam(ServerBasicPlayer playerToSwitch, ServerReceiver receiver) {
		boolean switched = false;
		for (ServerBasicPlayer player : blueTeam.values()) {
			/*
			 * We look through until we find the player we are looking for, we
			 * then remove them from their original team and add them to the
			 * other team.
			 */
			if (player.getID() == playerToSwitch.getID()) {
				if (currPlayerRedNum < (MaxPlayers / 2)) {
					removePlayer(playerToSwitch);
					addPlayer(playerToSwitch, 2);
					switched = true;
					break;
				}
			}
		}
		if (!switched) {
			for (ServerBasicPlayer player : redTeam.values()) {
				/*
				 * We look through until we find the player we are looking for,
				 * we then remove them from their original team and add them to
				 * the other team.
				 */
				if (player.getID() == playerToSwitch.getID()) {
					if (currPlayerBlueNum < (MaxPlayers / 2)) {
						removePlayer(playerToSwitch);
						addPlayer(playerToSwitch, 1);
						switched = true;
						break;
					}
				}
			}
		}
		String redMems = "Ret:Red:" + getTeam(2);
		String blueMems = "Ret:Blue:" + getTeam(1);
		receiver.sendToAll(redMems);
		receiver.sendToAll(blueMems);
	}

	/**
	 * Retrieve a particular team.
	 * @param teamNum Number of team to return - 1=blue, 2=red. 
	 * @return  String representation of team.
	 */
	public String getTeam(int teamNum) {
		String retStr = "";
		if (teamNum == 1) {
			for (ServerBasicPlayer player : blueTeam.values()) {
				retStr = retStr + player.getUsername() + "-";
			}
		} else {
			for (ServerBasicPlayer player : redTeam.values()) {
				retStr = retStr + player.getUsername() + "-";
			}
		}
		if (retStr.length() > 1)
			return retStr.substring(0, retStr.length() - 1);
		else
			return "";
	}

	/**
	 * Retrieve an array of player object.
	 * @return Array of ServerBasicPlayer objects.
	 */
	public ServerBasicPlayer[] getPlayers() {
		ArrayList<ServerBasicPlayer> playArr = new ArrayList<>();
		playArr.addAll(blueTeam.values());
		playArr.addAll(redTeam.values());
		ServerBasicPlayer[] playArrReturn = new ServerBasicPlayer[playArr.size()];
		playArr.toArray(playArrReturn);
		return playArrReturn;
	}

	/**
	 * Converts a team from Lobby representation to Game representation. 
	 * @param receiver Server Receiver used to retrieve/send messages between clients
	 * @param team Lobby team to convert.
	 * @param teamNum Team number to convert.
	 * @return Converted Team object.
	 */
	private Team convertTeam(ServerReceiver receiver, ConcurrentMap<Integer, ServerBasicPlayer> team, int teamNum) {
		Team newTeam = new Team();
		for (ServerBasicPlayer origPlayer : team.values()) {
			ServerPlayer player = null;
			if (teamNum == 1)
				player = new ServerPlayer(origPlayer.getID(), receiver, 0, 0, TeamEnum.BLUE);
			else
				player = new ServerPlayer(origPlayer.getID(), receiver, 0, 0, TeamEnum.RED);
			newTeam.addMember(player);
		}
		return newTeam;
	}

	/**
	 * Method to be called from the GUI when the lobby ends to start the game
	 * logic.
	 * 
	 * @param receiver TCP Server Receiver used to retrieve/send messages between clients.
	 * @param udpReceiver UDP Server Receiver used to retrieve/send messages between clients in game.
	 * 
	 * @author Alexandra Paduraru
	 */
	public void playGame(ServerReceiver receiver, UDPServer udpReceiver) {
		red = convertTeam(receiver, redTeam, 2);
		blue = convertTeam(receiver, blueTeam, 1);

		currentSessionGame = new ServerGame(GameType, red, blue, udpReceiver, id);
		ServerBasicPlayer[] allPlayers = getPlayers();

		// String to be sent needs to contain:
		// StartGame:<myID><myTeam>...8 times

		for (int i = 0; i < allPlayers.length; i++) {
			String toBeSent = "StartGame:";

			// the current player's info
			toBeSent += allPlayers[i].getID() + ":" + getTeamAssoc(allPlayers[i].getID()) + ":";

			// adding to the string the information about all the other players
			for (int j = 0; j < allPlayers.length; j++)
				if (allPlayers[j].getID() != allPlayers[i].getID())
					toBeSent += allPlayers[j].getID() + ":" + getTeamAssoc(allPlayers[j].getID()) + ":";

			receiver.sendToSpec(allPlayers[i].getID(), toBeSent);
		}

		currentSessionGame.startGame();

		
		while (!currentSessionGame.getGame().isGameFinished()) {}

		// sends the end game signal to all clients
		currentSessionGame.endGame(getWinner());
		// currentSessionGame.endGame();
	}

	/**
	 * A timer, accessed by the client for game countdown.
	 * @param receiver TCP Server Receiver used to retrieve/send messages between clients.
	 * @param udpReceiver UDP Server Receiver used to retrieve/send messages between clients in game.
	 * 
	 * @author Alexandra Paduraru
	 */
	public void timerStart(ServerReceiver receiver, UDPServer udpReceiver) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				RoundTimer timer = new RoundTimer(lobbyTime);
				timer.startTimer();
				long lastTime = -1;
				while (!timer.isTimeElapsed()) {
					try {
						if (lastTime != timer.getTimeLeft()) {
							// System.out.println("Timer changed: from " +
							// lastTime + " to " + timer.getTimeLeft());
							lastTime = timer.getTimeLeft();
							receiver.sendToAll("LTime:" + timer.getTimeLeft());
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {

					}
				}
				playGame(receiver,udpReceiver);
			}
		});
		t.start();
	}

	/**
	 * Return winned of a game.
	 * @return Winner team enum of a game.
	 */
	public TeamEnum getWinner() {
		return currentSessionGame.getGame().whoWon().getColour();
	}

	/**
	 * Return red converted team.
	 * @return Red Team object.
	 */
	public Team getRedTeam() {
		return red;
	}

	/**
	 * Return blue converted team.
	 * @return Blue Team object.
	 */
	public Team getBlueTeam() {
		return blue;
	}

	/**
	 * Return the team association of a player.
	 * @param playerID Player to determine team association of.
	 * @return Team association.
	 */
	private String getTeamAssoc(int playerID) {
		for (ServerBasicPlayer player : redTeam.values()) {
			if (player.getID() == playerID) {
				return "Red";
			}
		}
		for (ServerBasicPlayer player : blueTeam.values()) {
			if (player.getID() == playerID) {
				return "Blue";
			}
		}
		return "";
	}
}