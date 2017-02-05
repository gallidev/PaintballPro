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
	private int currPlayerBlueNum;
	private int currPlayerRedNum;
	private ConcurrentMap<Integer,Player> blueTeam = new ConcurrentHashMap<Integer,Player>(); // Team num 1
	private ConcurrentMap<Integer,Player> redTeam = new ConcurrentHashMap<Integer,Player>(); // Team num 2
	private int id;
	
	public Lobby(int myid, int PassedGameType)
	{
		inGameStatus = false;
		GameType = PassedGameType;
		MaxPlayers = 8;
		MaxPlayersReached = false;
		currPlayerBlueNum = 0;
		currPlayerRedNum = 0;
		id = myid;
	}

	public int getID()
	{
		return id;
	}
	
	public boolean getInGameStatus() {
		return inGameStatus;
	}

	public void switchGameStatus() {
		this.inGameStatus = !this.inGameStatus;
	}

	public int getGameType() {
		return GameType;
	}

	public boolean isMaxPlayersReached() {
		return getCurrPlayerTotal() == MaxPlayers;
	}

	private int getCurrPlayerTotal() {
		return currPlayerBlueNum + currPlayerRedNum;
	}

	// add player to team
	// NOTE - check somewhere else if max players is reached.
	public void addPlayer(Player playerToAdd, int specific)
	{
		// Specific - 0 = random, 1 = blue, 2 = red;
		if(currPlayerBlueNum >= (MaxPlayers/2) && (specific == 0 || specific == 2))
		{	
			redTeam.put(currPlayerRedNum, playerToAdd);
			currPlayerRedNum++;
		}
		else
		{
			blueTeam.put(currPlayerBlueNum, playerToAdd);
			currPlayerBlueNum++;
		}
		if(isMaxPlayersReached())
			MaxPlayersReached = true;
		else
			MaxPlayersReached = false;
	}
	
	// remove player from team and alter everyone's respective positions in the lobby to accomodate.
	public void removePlayer(Player playerToRemove)
	{
		boolean removed = false;
		int counter = 0;
		for(Player player : blueTeam.values())
		{
			if(player.getID() == playerToRemove.getID())
			{
				blueTeam.remove(counter);
				for(int i = (counter+1); i < (MaxPlayers/2); i++)
				{
					if(blueTeam.containsKey(i))
					{
						blueTeam.replace(i-1, blueTeam.get(i));
						blueTeam.remove(i);
						removed = true;
						currPlayerBlueNum--;
						break;
					}
				}
			}
			counter++;
		}
		if(!removed)
		{
			counter = 0;
			for(Player player : redTeam.values())
			{
				if(player.getID() == playerToRemove.getID())
				{
					redTeam.remove(counter);
					for(int i = (counter+1); i < (MaxPlayers/2); i++)
					{
						if(redTeam.containsKey(i))
						{
							redTeam.replace(i-1, redTeam.get(i));
							redTeam.remove(i);
							currPlayerRedNum--;
							removed = true;
							break;
						}
					}
				}
				counter++;
			}
		}
	}
	
	// switch player's team
	public void switchTeam(Player playerToSwitch)
	{
		boolean switched = false;
		for(Player player : blueTeam.values())
		{
			if(player.getID() == playerToSwitch.getID())
			{
				if(currPlayerRedNum < (MaxPlayers/2))
				{
					removePlayer(playerToSwitch);
					addPlayer(playerToSwitch,2);
					switched = true;
					break;
				}
			}
		}
		if(!switched)
		{
			for(Player player : redTeam.values())
			{
				if(player.getID() == playerToSwitch.getID())
				{
					if(currPlayerBlueNum < (MaxPlayers/2))
					{
						removePlayer(playerToSwitch);
						addPlayer(playerToSwitch,1);
						switched = true;
						break;
					}
				}
			}
		}
	
	}
}