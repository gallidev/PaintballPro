package serverLogic;

import java.util.ArrayList;

import logic.GameMode;
import players.PhysicsClientPlayer;
import players.ServerPlayer;
import rendering.Map;

/**
 * Class to play a game in a chosen Game Mode, initialises both teams and sends
 * the server a signal when the game is finished.
 * 
 * @author Alexandra Paduraru
 */
public class PlayGame {

	private Map gameMap;
	private Team team1;
	private Team team2;
	private GameMode mode;

	/**
	 * Starts the game in the chosen mode and sets the teams in the game.
	 * 
	 * @param mode
	 *            The chosen game mode to play the game.
	 * @param team1Players
	 *            All players that will form the first team.
	 * @param team2Players
	 *            All players that will form the second team.
	 */
	public PlayGame(GameMode mode, ArrayList<ServerPlayer> team1Players, ArrayList<ServerPlayer> team2Players) {
		gameMap = new Map();
		initializeTeamsWithPlayers(team1Players, team2Players);
		this.mode = mode;
	}

	/**
	 * Adds the players to the first and second team.
	 * 
	 * @param team1Players
	 *            All players that will form the first team.
	 * @param team2Players
	 *            All players that will form the first team.
	 */
	private void initializeTeamsWithPlayers(ArrayList<ServerPlayer> team1Players, ArrayList<ServerPlayer> team2Players) {
		//team1.setMembers(team1Players);
		//team2.setMembers(team2Players);
	}

	/**
	 * When the game is finished, that information is sent to the server, which
	 * then uses it to tell the players that the game is over and which team has
	 * won.
	 */
	public void finishGame() {
		// if (mode.isGameFinished())
		// send the game finished signal to the server
		//send the winning team to the server.
	}
}
