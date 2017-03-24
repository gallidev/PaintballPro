package networking.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ai.AIManager;
import ai.HashMapGen;
import enums.TeamEnum;
import integration.server.ServerGameSimulation;
import integration.server.ServerGameStateSender;
import integration.server.ServerInputReceiver;
import javafx.scene.image.Image;
import logic.RoundTimer;
import logic.server.CaptureTheFlagMode;
import logic.server.Team;
import logic.server.TeamMatchMode;
import networking.game.UDPServer;
import physics.CollisionsHandler;
import players.AIPlayer;
import players.EssentialPlayer;
import players.ServerBasicPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

/**
 * Class to represent a lobby.
 *
 * @author Matthew Walters
 */
public class Lobby {
	// Structures storing relevant data.

	// Lobby information
	private static final int lobbyTime = 10;
	private static int maxId;

	// Required for all players
	public boolean inGameStatus;

	private int id;
	private Map map;

	// Game information
	private int gameType;
	private int maxPlayers;
	private LobbyTimer timer;

	// Team information
	private ArrayList<EssentialPlayer> players;
	private boolean debug = false;
	private boolean testEnv = false;
	private CollisionsHandler collissionsHandler;
	private ConcurrentMap<Integer,ArrayList<ServerBasicPlayer>> teams;
	private int currPlayerBlueNum;
	private int currPlayerRedNum;
	private Team red;
	private Team blue;

	/**
	 * Sets passed variables and initialises some defaults.
	 *
	 * @param myid
	 *            ID of lobby.
	 * @param PassedGameType
	 *            Game mode that the lobby is used for.
	 * @param testEnv
	 *            Flag to determine whether this class is under test.
	 */
	public Lobby(int myid, int PassedGameType, boolean testEnv) {

		inGameStatus = false;
		gameType = PassedGameType;
		maxPlayers = 8;
		currPlayerBlueNum = 0;
		currPlayerRedNum = 0;
		id = myid;
		players = new ArrayList<>();
		this.testEnv = testEnv;
		teams = new ConcurrentHashMap<Integer,ArrayList<ServerBasicPlayer>>(); // 1 - red, 2 - blue
		teams.put(1, new ArrayList<ServerBasicPlayer>());
		teams.put(2, new ArrayList<ServerBasicPlayer>());
		timer = null;

		if (!testEnv) {
			// setting up the map
			if (PassedGameType == 1)
				map = Map.loadRaw("elimination");
			else
				map = Map.loadRaw("ctf");

			// setting up the collision handler
			collissionsHandler = new CollisionsHandler(map);
			red = new Team(TeamEnum.RED);
			blue = new Team(TeamEnum.BLUE);
		}
	}

	/**
	 * Retrieve the lobby id.
	 *
	 * @return Lobby id.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Get the game status of the current lobby
	 *
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
	 *
	 * @return Integer representing the game type.
	 */
	public int getGameType() {
		return gameType;
	}

	/**
	 * Returns whether or not max players have been reached.
	 *
	 * @return True if max players reached, False otherwise.
	 */
	public boolean isMaxPlayersReached() {
		return getCurrPlayerTotal() == maxPlayers;
	}

	/**
	 * Retrieves the current number of players in the lobby.
	 *
	 * @return Total number of players in the lobby currently.
	 */
	public int getCurrPlayerTotal() {
		return teams.get(1).size() + teams.get(2).size();
	}

	/**
	 * Add a player to a team - red of blue.
	 *
	 * @param playerToAdd
	 *            Player object to add to a team.
	 * @param specific
	 *            Is there a team we should try to allocate to? 0 = random, 1 =
	 *            blue, 2 = red
	 */
	public void addPlayer(ServerBasicPlayer playerToAdd, int specific) {
		// Add player to teams - alternate teams unless specified (when a client
		// requests to switch teams).
		// We check in LobbyTable if max players is reached.
		int totPlayers = getCurrPlayerTotal();

		if (((specific == 0) && (totPlayers % 2 == 0)) || ((specific == 2)) && (teams.get(1).size() <= (maxPlayers / 2))) {
			teams.get(1).add(playerToAdd);
		} else {
			teams.get(2).add(playerToAdd);
		}
	}

	/**
	 * Remove player from a team and re-order other team members as appropriate.
	 *
	 * @param playerToRemove
	 *            Player obejct to remove from the team.
	 */
	public void removePlayer(ServerBasicPlayer playerToRemove) {

		boolean removed = false;
		int counter = 0;

		Iterator<ServerBasicPlayer> it = teams.get(2).iterator();
		for(int i = 0; i < teams.get(2).size(); i++) {
			ServerBasicPlayer player = it.next();
			/*
			 * We look through until we find the player we are looking for, we
			 * then remove this player from the team and shift all of the items
			 * above the player down by one position.
			 */
			if (player.getID() == playerToRemove.getID()) {
				teams.get(2).remove(counter);
				removed = true;
			}
			counter++;
		}
		if (!removed) {
			counter = 0;
			it = teams.get(1).iterator();
			for(int i = 0; i < teams.get(1).size(); i++) {
				ServerBasicPlayer player = it.next();
				/*
				 * We look through until we find the player we are looking for,
				 * we then remove this player from the team and shift all of the
				 * items above the player down by one position.
				 */
				if (player.getID() == playerToRemove.getID()) {
					teams.get(1).remove(counter);
				}
				counter++;
			}
		}
	}

	/**
	 * Switch a player's team.
	 *
	 * @param playerToSwitch
	 *            Player object to switch.
	 * @param receiver
	 *            Server Receiver used to retrieve/send messages between clients
	 */
	public synchronized void switchTeam(ServerBasicPlayer playerToSwitch, ServerReceiver receiver) {

		boolean switched = false;
		String redMems;
		String blueMems;
		
		for (ServerBasicPlayer player : teams.get(2)) {
			/*
			 * We look through until we find the player we are looking for, we
			 * then remove them from their original team and add them to the
			 * other team.
			 */
			if (player.getID() == playerToSwitch.getID()) {
				if (currPlayerRedNum < (maxPlayers / 2)) {
					removePlayer(playerToSwitch);
					addPlayer(playerToSwitch, 2);
					switched = true;
					break;
				}
			}
		}
		if (!switched) {
			for (ServerBasicPlayer player : teams.get(1)) {
				/*
				 * We look through until we find the player we are looking for,
				 * we then remove them from their original team and add them to
				 * the other team.
				 */
				if (player.getID() == playerToSwitch.getID()) {
					if (currPlayerBlueNum < (maxPlayers / 2)) {
						removePlayer(playerToSwitch);
						addPlayer(playerToSwitch, 1);
						switched = true;
						break;
					}
				}
			}
		}
		redMems = "Ret:Red:" + getTeam(2);
		blueMems = "Ret:Blue:" + getTeam(1);
		if (!testEnv) {
			receiver.sendToAll(redMems);
			receiver.sendToAll(blueMems);
		}
	}

	/**
	 * Retrieve a particular team.
	 *
	 * @param teamNum
	 *            Number of team to return - 1=blue, 2=red.
	 * @return String representation of team.
	 */
	public String getTeam(int teamNum) {
		String retStr = "";
		if (teamNum == 1) {
			for (ServerBasicPlayer player : teams.get(2)) {
				retStr = retStr + player.getUsername() + "-";
			}
		} else {
			for (ServerBasicPlayer player : teams.get(1)) {
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
	 *
	 * @return Array of ServerBasicPlayer objects.
	 */
	public ServerBasicPlayer[] getPlayers() {
		ArrayList<ServerBasicPlayer> playArr = new ArrayList<>();
		ServerBasicPlayer[] playArrReturn;

		playArr.addAll(teams.get(2));
		playArr.addAll(teams.get(1));
		playArrReturn = new ServerBasicPlayer[playArr.size()];
		playArr.toArray(playArrReturn);

		return playArrReturn;
	}

	/**
	 * Convert teams from Lobby structures to in-game Structures.
	 *
	 * @param receiver
	 *            Server receiver to retrieve messages sent to the server.
	 * @param team
	 *            Team representation.
	 * @param teamNum
	 *            Team number.
	 * @return Team class.
	 *
	 * @author Alexandra Paduraru and Matthew Walters
	 */
	private Team convertTeam(ServerReceiver receiver, ArrayList<ServerBasicPlayer> team, int teamNum) {
		Team newTeam = new Team(teamNum == 1 ? TeamEnum.BLUE : TeamEnum.RED);
		for (ServerBasicPlayer origPlayer : team) {

			UserPlayer player = null;

			Image imagePlayer = ImageFactory.getPlayerImage(TeamEnum.RED);

			int serverId = origPlayer.getID();

			int spawnLoc = 0;

			if (newTeam.getColour() == TeamEnum.RED) {
				spawnLoc = newTeam.getMembersNo();
				newTeam.setColour(TeamEnum.RED);
			} else {
				spawnLoc = newTeam.getMembersNo() + 4;
				newTeam.setColour(TeamEnum.BLUE);
			}

			if (teamNum == 1) {
				player = new UserPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, serverId,
						map.getSpawns(), TeamEnum.BLUE, collissionsHandler, imagePlayer, map.getGameMode(),
						ServerGameSimulation.GAME_HERTZ);
				player.setNickname(origPlayer.getUsername());
			} else {
				player = new UserPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, serverId,
						map.getSpawns(), TeamEnum.RED, collissionsHandler, imagePlayer, map.getGameMode(),
						ServerGameSimulation.GAME_HERTZ);
				player.setNickname(origPlayer.getUsername());
			}

			newTeam.addMember(player);
		}
		return newTeam;
	}

	/**
	 * Method used to add nicknames to the AI players in multiplayer. The names
	 * are read from an external file in the res folder.
	 *
	 * @author Alexandra Paduraru
	 */
	public void setPlayerNames() {
		File names = new File("res/names.txt");
		Scanner readNames;
		try {
			readNames = new Scanner(names);
			for (EssentialPlayer p : red.getMembers()) {
				if (p instanceof AIPlayer)
					p.setNickname(readNames.nextLine());
			}

			for (EssentialPlayer p : blue.getMembers()) {
				if (p instanceof AIPlayer)
					p.setNickname(readNames.nextLine());
			}

			readNames.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/* Methods used to start the game */

	/**
	 * A timer, accessed by the client for game countdown.
	 *
	 * @param receiver
	 *            TCP Server Receiver used to retrieve/send messages between
	 *            clients.
	 * @param udpServer
	 *            UDP Server Receiver used to retrieve/send messages between
	 *            clients in game.
	 * @param gameMode
	 *            Mode of the game : 1 = Team Match, 2 = KoTH, 3 = CTF, 4 =
	 *            Escort
	 *
	 */
	public void timerStart(ServerReceiver receiver, UDPServer udpServer, int gameMode) {
		if (timer == null) {
			timer = new LobbyTimer(lobbyTime, receiver, udpServer, gameMode, this);
			timer.start();
		}
	}
	
	public void stopTimer() {
		if (timer != null) {
			timer.m_running = false;
			try {
				timer.join(100);
			} catch (InterruptedException e) {
				//
			}
			timer = null;
		}
	}

	/**
	 * Creates all the necessary information to start a new game, such as
	 * players, teams, collision handling.
	 *
	 * @param receiver
	 *            A server receiver used to send the start game information to
	 *            the clients.
	 * @param udpServer
	 *            An UDP server used after the game starts.
	 * @param gameMode
	 *            The game mode to be played : 1 for Team Match Mode and 2 for
	 *            CTF
	 *
	 * @author Alexandra Paduraru
	 * @author Filippo Galli
	 */
	public void playGame(ServerReceiver receiver, UDPServer udpServer, int gameMode) {
		red = convertTeam(receiver, teams.get(1), 2);
		blue = convertTeam(receiver, teams.get(2), 1);

		if (debug)
		{
			System.out.println("Lobby game mode: " + gameMode);
			System.out.println("Red user players: " + red.getMembersNo());
		}

		HashMapGen hashMaps = new HashMapGen(map);

		ServerInputReceiver inputReceiver;

		// filling the game with AI players
		AIManager redAIM;
		AIManager blueAIM;

		redAIM = new AIManager(red, map, collissionsHandler, getMaxId(), hashMaps);
		//redAIM.createPlayers();

		blueAIM = new AIManager(blue, map, collissionsHandler, getMaxId(), hashMaps);
		//blueAIM.createPlayers();

		// setting team players and enemies
		for (EssentialPlayer p : red.getMembers()) {
			p.setOppTeam(blue);
			p.setMyTeam(red);
		}

		for (EssentialPlayer p : blue.getMembers()) {
			p.setOppTeam(red);
			p.setMyTeam(blue);
		}

		setPlayerNames();

		collissionsHandler.setRedTeam(red);
		collissionsHandler.setBlueTeam(blue);

		players.addAll(red.getMembers());
		players.addAll(blue.getMembers());

		if (debug) {
			System.out.println("Players are: ");
			for (EssentialPlayer p : players) {
				System.out.print(p.getPlayerId() + " ");
			}
		}

		inputReceiver = new ServerInputReceiver(players);

		udpServer.setInputReceiver(inputReceiver);

		map.getPowerups()[0].addAlternatePowerup(map.getPowerups()[1]);
		map.getPowerups()[1].addAlternatePowerup(map.getPowerups()[0]);

		for (EssentialPlayer p : players) {
			String toBeSent = "2:" + gameMode + ":";
			toBeSent += map.getPowerups()[0].getIndex() + ":" + map.getPowerups()[1].getIndex() + ":";
			if(map.getFlag() != null)
				toBeSent += map.getFlag().getIndex() + ":";
			else
				toBeSent += "0:";
			// the current player's info
			toBeSent += p.getPlayerId() + ":" + (p.getTeam() == TeamEnum.RED ? "Red" : "Blue") + ":" + p.getNickname()
					+ ":";

			// adding to the string the information about all the other players
			for (EssentialPlayer aux : players)
				if (aux.getPlayerId() != p.getPlayerId())
					toBeSent += aux.getPlayerId() + ":" + (aux.getTeam() == TeamEnum.RED ? "Red" : "Blue") + ":"
							+ aux.getNickname() + ":";

			if (p instanceof UserPlayer) {
				receiver.sendToSpec(p.getPlayerId(), toBeSent);
			}
		}
	}

	/**
	 * Starts the game simulation on the server in a given game mode.
	 *
	 * @param udpServer
	 *            The UDP server used to send in-game information.
	 * @param gameMode
	 *            The game mode that is going to be played.
	 *
	 * @author Alexandra Paduraru
	 * @author Filippo Galli
	 */
	public void startGameLoop(UDPServer udpServer, int gameMode) {

		ServerGameSimulation gameloop = null;
		ServerGameStateSender stateSender;

		if (gameMode == 1)
			gameloop = new ServerGameSimulation(new TeamMatchMode(red, blue));
		else
			gameloop = new ServerGameSimulation(new CaptureTheFlagMode(red, blue));

		gameloop.runGameLoop();
		stateSender = new ServerGameStateSender(udpServer, players, id);
		stateSender.setGameLoop(gameloop);
		map.getPowerups()[0].setListener(stateSender);
		map.getPowerups()[1].setListener(stateSender);

		collissionsHandler.setListener(stateSender);
		for(EssentialPlayer p: players){
			p.setCollisionsHandlerListener(stateSender);
		}
		stateSender.startSending();

		inGameStatus = true;
	}

	/**
	 * Computes the maximum ID assigned to a user player in the game. This
	 * method is used to assign ID to AI players filling up the teams.
	 *
	 * @return The maximum id of a user player.
	 *
	 * @author Alexandra Paduraru
	 */
	private int getMaxId() {
		int id = -1;
		for (EssentialPlayer p : red.getMembers())
			if (p.getPlayerId() > id)
				id = p.getPlayerId();

		for (EssentialPlayer p : blue.getMembers())
			if (p.getPlayerId() > id)
				id = p.getPlayerId();

		return id;
	}

	// Getters and setters

	/**
	 * Return winner of a game.
	 *
	 * @return Winner team enum of a game.
	 */
//	public TeamEnum getWinner() {
//		return currentSessionGame.getGame().whoWon().getColour();
//	}

	/**
	 * Sets the current maximum id of a user player.
	 *
	 * @param newMax
	 *            The new maximum id.
	 */
	public static void setMaxId(int newMax) {
		maxId = newMax;
	}

	/**
	 * Return red converted team.
	 *
	 * @return Red Team object.
	 */
	public Team getRedTeam() {
		return red;
	}

	/**
	 * Return blue converted team.
	 *
	 * @return Blue Team object.
	 */
	public Team getBlueTeam() {
		return blue;
	}

	/**
	 * Get maximum number of players that the Lobby will allow.
	 *
	 * @return Max player value.
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setRedTeam(Team team){
		red = team;
	}
	
	public void setBlueTeam(Team team){
		blue = team;
	}
}