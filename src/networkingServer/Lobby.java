package networkingServer;


import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import enums.TeamEnum;
import logic.RoundTimer;
import logic.ServerPlayer;
import logic.Team;
import logic.TeamMatchMode;
import networkingClient.ClientSender;
import networkingInterfaces.ServerGame;
import physics.GeneralPlayer;

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
	private static final int lobbyTime = 10; //lobby runs for 2 minutes


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

	// Add player to teams alternatively.
	// We check in LobbyTable if max players is reached.
	public void addPlayer(Player playerToAdd, int specific)
	{
		// Specific - 0 = random, 1 = blue, 2 = red;
		int totPlayers = getCurrPlayerTotal();
		if(((totPlayers % 2 == 1) || (specific == 0 || specific == 2)) && (currPlayerRedNum <= (MaxPlayers/2)))
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
				currPlayerBlueNum--;
				removed = true;
				for(int i = (counter+1); i < (MaxPlayers/2); i++)
				{
					if(blueTeam.containsKey(i))
					{
						blueTeam.replace(i-1, blueTeam.get(i));
						blueTeam.remove(i);
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
					currPlayerRedNum--;
					for(int i = (counter+1); i < (MaxPlayers/2); i++)
					{
						if(redTeam.containsKey(i))
						{
							redTeam.replace(i-1, redTeam.get(i));
							redTeam.remove(i);
						}
					}
				}
				counter++;
			}
		}
	}

	// switch player's team
	public void switchTeam(Player playerToSwitch, ServerMsgReceiver receiver)
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
		String redMems = "Ret:Red:" + getTeam(2);
		String blueMems = "Ret:Blue:" + getTeam(1);
		receiver.sendToAll(redMems);
		receiver.sendToAll(blueMems);
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
		if(retStr.length() > 1)
			return retStr.substring(0, retStr.length()-1);
		else
			return "";
	}

	public Player[] getPlayers()
	{
		ArrayList<Player> playArr = new ArrayList<>();
		playArr.addAll(blueTeam.values());
		playArr.addAll(redTeam.values());
		Player[] playArrReturn = new Player[playArr.size()];
		playArr.toArray(playArrReturn);
		return playArrReturn;
	}

	private Team convertTeam(ServerMsgReceiver receiver,ConcurrentMap<Integer,Player> team,int teamNum)
	{
		Team newTeam = new Team();
		for(Player origPlayer : team.values())
		{
			ServerPlayer player = null;
			if(teamNum == 1)
				player = new ServerPlayer(origPlayer.getID(),receiver,0,0,TeamEnum.BLUE);
			else
				player = new ServerPlayer(origPlayer.getID(),receiver,0,0,TeamEnum.RED);
			newTeam.addMember(player);
		}
		return newTeam;
	}

	/**
	 * Method to be called from the GUI when the lobby ends to start the game logic.
	 * @param sender
	 * @param receiver
	 */
	public void playGame(ServerMsgReceiver receiver)
	{
		Team red = convertTeam(receiver, redTeam, 2);
		Team blue = convertTeam(receiver, blueTeam, 1);
		ServerGame currentSessionGame = new ServerGame(GameType, red, blue, receiver);
		Player[] allPlayers = getPlayers();
		
		//String to be sent needs to contain: 
		//StartGame:<myID><myTeam>...8 times
		
		for(int i = 0; i < allPlayers.length; i++){
			String toBeSent = "StartGame:";
			
			//the current player's info
			toBeSent += allPlayers[i].getID() + ":" + getTeamAssoc(allPlayers[i].getID()) + ":";
			
			//adding to the string the information about all the other players
			for (int j = 0; j < allPlayers.length; j++)
				if (i != j)
					toBeSent += allPlayers[j].getID() + ":" + getTeamAssoc(allPlayers[j].getID()) + ":"; 
			
			receiver.sendToID(allPlayers[i], toBeSent);
		}
		
		currentSessionGame.startGame();
		
		// sends the end game signal to all clients
		while(!currentSessionGame.getGame().isGameFinished()){}
		currentSessionGame.endGame();
	}

	// A timer, accessed by the client for game countdown.
	public void timerStart(ServerMsgReceiver receiver) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				RoundTimer timer = new RoundTimer(lobbyTime);
				timer.startTimer();
				long lastTime = -1;
				while(!timer.isTimeElapsed()){
					try {
						if (lastTime != timer.getTimeLeft()) {
							System.out.println("Timer changed: from " + lastTime + " to " + timer.getTimeLeft());
							lastTime = timer.getTimeLeft();
							receiver.sendToAll("LTime:" + timer.getTimeLeft());
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {

					}
				}
				playGame(receiver);
			}
		});
		t.start();
	}
	
	private String getTeamAssoc(int playerID)
	{
		for(Player player : redTeam.values())
		{
			if(player.getID() == playerID)
			{
				return "Red";
			}
		}
		for(Player player : blueTeam.values())
		{
			if(player.getID() == playerID)
			{
				return "Blue";
			}
		}
		return "";
	}
}
