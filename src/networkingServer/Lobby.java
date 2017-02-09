package networkingServer;


import java.util.ArrayList;
import java.util.concurrent.*;

import networkingClient.ClientReceiver;
import networkingClient.ClientSender;
import networkingInterfaces.TeamMatchMode;
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

	public int getCurrPlayerTotal() {
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
	}
	
	// remove player from team and alter everyone's respective positions in the lobby to accomodate
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
	
	public String getTeam(int teamNum) // 1 for blue, 2 for red.
	{
		String retStr = "";
		if(teamNum == 1)
		{
			for(Player player : blueTeam.values())
			{
				retStr = retStr + player.getUsername() + "-";
			}
		}
		else
		{
			for(Player player : redTeam.values())
			{
				retStr = retStr + player.getUsername() + "-";
			}
		}
		return retStr.substring(0, retStr.length()-1);
	}
	
	public Player[] getPlayers()
	{
		Player[] playArr = new Player[getCurrPlayerTotal()];
		int index = 0;
		for(Player player : blueTeam.values())
		{
			playArr[index] = player;
			index++;
		}
		for(Player player : redTeam.values())
		{
			playArr[index] = player;
			index++;
		}
		return playArr;
	}
	
	public void playGame(ServerMsgSender sender, ServerMsgReceiver receiver)
	{
		TeamMatchMode teamMatch = new TeamMatchMode();
		// Do stuff here.
	}
}