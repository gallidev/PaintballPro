package networking.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import enums.TeamEnum;
import integrationServer.GameSimulation;
import logic.RoundTimer;
import networking.game.UDPServer;
import networking.interfaces.ServerGame;
import physics.CollisionsHandler;
import players.ServerBasicPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import serverLogic.Team;

/**
 * Class to represent a lobby.
 */
public class Lobby {
	// Structures storing relevant data.

	// Game Modes - 1 = Team Match, 2 = KoTH, 3 = CTF, 4 = Escort
	// 1 - Blue Team, 2 - Red Team
	// A lobby runs for 2 minutes and then starts a game, if full it starts a 10
	// second countdown before running a game.

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

	public Lobby(int myid, int PassedGameType) {
		inGameStatus = false;
		GameType = PassedGameType;
		MaxPlayers = 8;
		currPlayerBlueNum = 0;
		currPlayerRedNum = 0;
		id = myid;
	}

	public int getID() {
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

	// Add player to teams - alternate teams unless specified (when a client
	// requests to switch teams).
	// We check in LobbyTable if max players is reached.
	public void addPlayer(ServerBasicPlayer playerToAdd, int specific) {
		// Specific - 0 = random, 1 = blue, 2 = red;
		int totPlayers = getCurrPlayerTotal();
		if (((totPlayers % 2 == 0) && (specific == 0 || specific == 2)) && (currPlayerRedNum <= (MaxPlayers / 2))) {
			redTeam.put(currPlayerRedNum, playerToAdd);
			currPlayerRedNum++;
		} else {
			blueTeam.put(currPlayerBlueNum, playerToAdd);
			currPlayerBlueNum++;
		}
	}

	// remove player from team and alter everyone's respective positions in the
	// lobby to accomodate
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

	// switch player's team
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

	public ServerBasicPlayer[] getPlayers() {
		ArrayList<ServerBasicPlayer> playArr = new ArrayList<>();
		playArr.addAll(blueTeam.values());
		playArr.addAll(redTeam.values());
		ServerBasicPlayer[] playArrReturn = new ServerBasicPlayer[playArr.size()];
		playArr.toArray(playArrReturn);
		return playArrReturn;
	}

//	private Team convertTeam(ServerMsgReceiver receiver, ConcurrentMap<Integer, ServerBasicPlayer> team, int teamNum) {
//		Team newTeam = new Team();
//		for (ServerBasicPlayer origPlayer : team.values()) {
//			ServerMinimumPlayer player = null;
//			if (teamNum == 1)
//				player = new UserPlayer(origPlayer.getID(), receiver, 0, 0, TeamEnum.BLUE);
//			else
//				player = new UserPlayer(origPlayer.getID(), receiver, 0, 0, TeamEnum.RED);
//			newTeam.addMember(player);
//		}
//		return newTeam;
//	}


	/**
	 * Method to be called from the GUI when the lobby ends to start the game
	 * logic.
	 *
	 * @param sender
	 * @param receiver
	 * @author Alexandra Paduraru
	 */

//	public void playGame(ServerMsgReceiver receiver) {
////		red = convertTeam(receiver, redTeam, 2);
////		blue = convertTeam(receiver, blueTeam, 1);
//
//		currentSessionGame = new ServerGame(GameType, red, blue, receiver);
//		ServerBasicPlayer[] allPlayers = getPlayers();
//
//		// String to be sent needs to contain:
//		// StartGame:<myID><myTeam>...8 times
//
//		for (int i = 0; i < allPlayers.length; i++) {
//			String toBeSent = "StartGame:";
//
//			// the current player's info
//			toBeSent += allPlayers[i].getID() + ":" + getTeamAssoc(allPlayers[i].getID()) + ":";
//
//			// adding to the string the information about all the other players
//			for (int j = 0; j < allPlayers.length; j++)
//				if (allPlayers[j].getID() != allPlayers[i].getID())
//					toBeSent += allPlayers[j].getID() + ":" + getTeamAssoc(allPlayers[j].getID()) + ":";
//
//			receiver.sendToSpec(allPlayers[i].getID(), toBeSent);
//		}
////
////		currentSessionGame.startGame();
////
////
////		while (!currentSessionGame.getGame().isGameFinished()) {}
////
////		// sends the end game signal to all clients
////		currentSessionGame.endGame(getWinner());
//		// currentSessionGame.endGame();
//	}


	// A timer, accessed by the client for game countdown.
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

	public TeamEnum getWinner() {
		return currentSessionGame.getGame().whoWon().getColour();
	}

	public Team getRedTeam() {
		return red;
	}

	public Team getBlueTeam() {
		return blue;
	}

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



	//====================NEW INTEGRATION BELOW=================================

	public void playGame(ServerReceiver receiver, UDPServer server){
		red = new Team();
		blue = new Team();

		Map map = Map.load("res/maps/" + "elimination" + ".json");

		double imageWidth = ImageFactory.getPlayerImage(TeamEnum.RED).getWidth();
		double imageHeight = ImageFactory.getPlayerImage(TeamEnum.RED).getHeight();
		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		UserPlayer redPlayer = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, imageWidth, imageHeight, map, TeamEnum.RED, collisionsHandler);
		UserPlayer bluePlayer = new UserPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64, 4, imageWidth, imageHeight, map, TeamEnum.BLUE, collisionsHandler);

		//add players to the teams
		red.addMember(redPlayer);
		blue.addMember(bluePlayer);

		collisionsHandler.setRedTeam(red.getMembers());
		collisionsHandler.setRedTeam(red.getMembers());

		GameSimulation simulator = new GameSimulation(red, blue);
		simulator.startExecution();
	}

}
