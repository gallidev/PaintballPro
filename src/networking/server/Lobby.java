package networking.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.AIManager;
import enums.TeamEnum;
import integrationServer.ServerGameSimulation;
import integrationServer.ServerGameStateSender;
import integrationServer.ServerInputReceiver;
import javafx.scene.image.Image;
import logic.RoundTimer;
import networking.game.UDPServer;
import networking.interfaces.ServerGame;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.ServerBasicPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import serverLogic.CaptureTheFlagMode;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

/**
 * Class to represent a lobby.
 *
 * @author Matthew Walters
 */
public class Lobby {
	// Structures storing relevant data.

	// Lobby information
	private static final int lobbyTime = 10;
	private int id;
	private boolean inGameStatus;

	// Game information
	private int GameType;
	private int MaxPlayers;
	private ServerGame currentSessionGame;
	private Thread timer;

	// Team information
	private int currPlayerBlueNum;
	private int currPlayerRedNum;
	private ConcurrentMap<Integer, ServerBasicPlayer> blueTeam = new ConcurrentHashMap<Integer, ServerBasicPlayer>();
	private ConcurrentMap<Integer, ServerBasicPlayer> redTeam = new ConcurrentHashMap<Integer, ServerBasicPlayer>();
	private Team red;
	private Team blue;
	private ArrayList<EssentialPlayer> players;
	private static int maxId;

	//required for all players
	Map map;

	private boolean debug = false;
	private CollisionsHandler collissionsHandler;

	private boolean testEnv = false;

	/**
	 * Sets passed variables and initialises some defaults.
	 * @param myid ID of lobby.
	 * @param PassedGameType Game mode that the lobby is used for.
	 * @param testEnv Flag to determine whether this class is under test.
	 */
	public Lobby(int myid, int PassedGameType, boolean testEnv) {
		
		inGameStatus = false;
		GameType = PassedGameType;
		MaxPlayers = 8;
		currPlayerBlueNum = 0;
		currPlayerRedNum = 0;
		id = myid;
		players = new ArrayList<>();
		this.testEnv = testEnv;

		if(!testEnv)
		{
			//setting up the map
			if (PassedGameType == 1)
				map = Map.loadRaw("elimination");
			else
				map = Map.loadRaw("ctf");

			//setting up the collision handler
			collissionsHandler = new CollisionsHandler(map);
			red = new Team(TeamEnum.RED);
			blue = new Team(TeamEnum.BLUE);
		}
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
		if(!testEnv)
		{
			receiver.sendToAll(redMems);
			receiver.sendToAll(blueMems);
		}
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
	 * Convert teams from Lobby structures to in-game Structures.
	 * @param receiver Server receiver to retrieve messages sent to the server.
	 * @param team Team representation.
	 * @param teamNum Team number.
	 * @return Team class.
	 */
	private Team convertTeam(ServerReceiver receiver, ConcurrentMap<Integer, ServerBasicPlayer> team, int teamNum) {
		Team newTeam = new Team(teamNum == 1 ? TeamEnum.BLUE : TeamEnum.RED);
		for (ServerBasicPlayer origPlayer : team.values()) {

			UserPlayer player = null;

			Image imagePlayer = ImageFactory.getPlayerImage(TeamEnum.RED);

			int serverId = origPlayer.getID();

			int spawnLoc = 0;

			if (newTeam.getColour() == TeamEnum.RED){
				spawnLoc = newTeam.getMembersNo();
				newTeam.setColour(TeamEnum.RED);
			}
			else{
				spawnLoc = newTeam.getMembersNo() + 4;
				newTeam.setColour(TeamEnum.BLUE);
			}

			//provisionally hard-coded
			if (teamNum == 1)
				player = new UserPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, serverId, map.getSpawns(),  TeamEnum.BLUE, collissionsHandler, imagePlayer);
			else
				player = new UserPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, serverId, map.getSpawns(),  TeamEnum.RED, collissionsHandler, imagePlayer);

			newTeam.addMember(player);
		}
		return newTeam;
	}

	/**
	 * A timer, accessed by the client for game countdown.
	 * @param receiver TCP Server Receiver used to retrieve/send messages between clients.
	 * @param udpServer UDP Server Receiver used to retrieve/send messages between clients in game.
	 * @param gameMode Mode of the game : 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort
	 *
	 */
	public void timerStart(ServerReceiver receiver, UDPServer udpServer, int gameMode) {
		if (timer == null){
			timer = new Thread(new Runnable() {
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
					playGame(receiver,udpServer, gameMode);
					startGameLoop(udpServer, gameMode);
				}
			});
			timer.start();
		}

	}

	/**
	 * Return winner of a game.
	 * @return Winner team enum of a game.
	 */
	public TeamEnum getWinner() {
		return currentSessionGame.getGame().whoWon().getColour();
	}

	/**
	 * Return red converted team.
	 * @return Red Team object.
	 */
	public serverLogic.Team getRedTeam() {
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
	 * Creates all the necessary information to start a new game.
	 * @param receiver A server receiver used to send the start game information to the clients.
	 * @param udpServer An UDP server used after the game starts.
	 * @param gameMode The game mode to be played : 1 for Team Match Mode and 2 for CTF
	 * 
	 * @author Alexandra Paduraru
	 * @author Filippo Galli
	 */
	public void playGame(ServerReceiver receiver, UDPServer udpServer, int gameMode){
		red = convertTeam(receiver, redTeam, 2);
		blue = convertTeam(receiver, blueTeam, 1);

		if (debug) System.out.println("Lobby game mode: " + gameMode);
		System.out.println("Red user players: " + red.getMembersNo());

		//filling the game with AI players
		AIManager redAIM = new AIManager(red, map, collissionsHandler, getMaxId());
		redAIM.createPlayers();

		AIManager blueAIM = new AIManager(blue, map, collissionsHandler, getMaxId());
		blueAIM.createPlayers();

		//setting team players and enemies
		for(EssentialPlayer p : red.getMembers()){
			p.setOppTeam(blue);
			p.setMyTeam(red);
		}

		for(EssentialPlayer p : blue.getMembers()){
			p.setOppTeam(red);
			p.setMyTeam(blue);
		}

		collissionsHandler.setRedTeam(red);
		collissionsHandler.setBlueTeam(blue);

		players.addAll(red.getMembers());
		players.addAll(blue.getMembers());

		if(debug){
			System.out.println("Players are: ");
			for(EssentialPlayer p : players){
				System.out.print(p.getPlayerId() + " ");
			}
		}

		ServerInputReceiver inputReceiver = new ServerInputReceiver(players);

		udpServer.setInputReceiver(inputReceiver);

		for (EssentialPlayer p : players) {
			String toBeSent = "2:" + gameMode + ":";

			// the current player's info
			toBeSent += p.getPlayerId() + ":" + (p.getTeam() == TeamEnum.RED ? "Red" : "Blue") + ":";

			// adding to the string the information about all the other players
			for (EssentialPlayer aux : players)
				if (aux.getPlayerId() != p.getPlayerId())
					toBeSent += aux.getPlayerId() + ":" + (aux.getTeam() == TeamEnum.RED ? "Red" : "Blue") + ":";

			if (p instanceof UserPlayer)
				receiver.sendToSpec(p.getPlayerId(), toBeSent);
		}
	}

	/**
	 * Starts the game simulation on the server in a given game mode.
	 * @param udpServer The UDP server used to send in-game information.
	 * @param gameMode The game mode that is going to be played.
	 */
	public void startGameLoop(UDPServer udpServer, int gameMode){

		ServerGameSimulation gameloop = null;
		if (gameMode == 1)
			gameloop = new ServerGameSimulation( new TeamMatchMode(red, blue));
		else
			gameloop = new ServerGameSimulation( new CaptureTheFlagMode(red, blue));

		gameloop.startExecution();
		ServerGameStateSender stateSender = new ServerGameStateSender(udpServer, players, id);
		stateSender.setGameLoop(gameloop);
		stateSender.startSending();
	}

	/**
	 * Computes the maximum ID assigned to a user player in the game.
	 * This method is used to assign ID to AI players filling up the teams.
	 * @return The maximum id of a user player.
	 */
	private int getMaxId(){
		int id = -1;
		for(EssentialPlayer p: red.getMembers())
			if (p.getPlayerId() > id )
				id = p.getPlayerId();

		for(EssentialPlayer p: blue.getMembers())
			if (p.getPlayerId() > id )
				id = p.getPlayerId();

		return id;
	}

	/**
	 * Sets the current maximum id of a user player.
	 * @param newMax The new maximum id.
	 */
	public static void setMaxId(int newMax){
		maxId = newMax;
	}

	/**
	 * Get maximum number of players that the Lobby will allow.
	 * @return Max player value.
	 */
	public int getMaxPlayers(){
		return MaxPlayers;
	}

}
