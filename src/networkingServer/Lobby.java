package networkingServer;


import java.util.ArrayList;
import java.util.concurrent.*;

import networkingSharedStuff.MessageQueue;

/**
 * Class to store important client-related information used by Client and Server.
 */
public class Lobby {
	//Structures storing relevant data.
	
	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort	
	private boolean inGameStatus;
	private int GameType;
	private int MaxPlayers;
	private boolean MaxPlayersReached;
	private int currPlayerNum;
	private ConcurrentMap<Integer,Player> blueTeam = new ConcurrentHashMap<Integer,Player>(); // Team num 1
	private ConcurrentMap<Integer,Player> redTeam = new ConcurrentHashMap<Integer,Player>(); // Team num 2
	
	public Lobby(int PassedGameType, int PassedMaxPlayers)
	{
		inGameStatus = false;
		GameType = PassedGameType;
		MaxPlayers = 8;
		MaxPlayersReached = false;
		currPlayerNum = 0;
	}

	public boolean getInGameStatus() {
		return inGameStatus;
	}

	public void setInGameStatus(boolean inGameStatus) {
		this.inGameStatus = inGameStatus;
	}

	public int getGameType() {
		return GameType;
	}

	public void setGameType(int gameType) {
		GameType = gameType;
	}

	public int getMaxPlayers() {
		return MaxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		MaxPlayers = maxPlayers;
	}

	public boolean isMaxPlayersReached() {
		return MaxPlayersReached;
	}

	public void setMaxPlayersReached(boolean maxPlayersReached) {
		MaxPlayersReached = maxPlayersReached;
	}

	public int getCurrPlayerNum() {
		return currPlayerNum;
	}

	public void incrementCurrPlayerNum() {
		this.currPlayerNum = this.currPlayerNum++;
	}
	
	public void decrementCurrPlayerNum() {
		this.currPlayerNum = this.currPlayerNum--;
	}

	// add player to team
	
	// remove player from team
	
	// switch player's team
	
	
}
