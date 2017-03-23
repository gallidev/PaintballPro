package ai;

import enums.TeamEnum;
import integration.server.ServerGameSimulation;
import logic.server.Team;
import networking.server.Lobby;
import physics.CollisionsHandler;
import players.AIPlayer;
import players.EssentialPlayer;
import rendering.Map;

/**
 * Class to create all AI players required to fill in a team, up to a maximum of
 * 4 players per team.
 * 
 * @author Alexandra Paduraru
 *
 */
public class AIManager {

	private CollisionsHandler collissionsHandler;
	private HashMapGen hashMaps;
	private Map map;
	private int nextId;
	private Team team;

	/**
	 * Initialises a new AI manager by assigning the necessary information
	 * needed to fill in the given team.
	 * 
	 * @param team
	 *            A team of players that needs to be filled with AI players.
	 * @param map
	 *            The map of the game that will be played by the team.
	 * @param collHandler
	 *            The collisions handler used by the team players.
	 * @param maxId
	 *            The current maximum id help by a player in the game to be
	 *            played.
	 * @param hashmaps
	 *            The hashmaps used by the AI players.
	 */
	public AIManager(Team team, Map map, CollisionsHandler collHandler, int maxId, HashMapGen hashmaps) {
		super();
		this.team = team;
		this.map = map;
		this.collissionsHandler = collHandler;
		nextId = maxId + 1;
		hashMaps = hashmaps;
	}

	/**
	 * Creates all the AI players required to fill in the team.
	 */
	public void createPlayers() {
		int currentPlayersNo;
		currentPlayersNo = team.getMembersNo();

		while (currentPlayersNo < 4) {
			int spawnLoc = 0;

			if (team.getColour() == TeamEnum.RED)
				spawnLoc = currentPlayersNo;
			else
				spawnLoc = currentPlayersNo + 4;

			EssentialPlayer newPlayer;
			newPlayer = new AIPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, nextId, map,
					team.getColour(), collissionsHandler, hashMaps, map.getGameMode(), ServerGameSimulation.GAME_HERTZ);

			team.addMember(newPlayer);
			currentPlayersNo++;
			nextId++;
		}

		Lobby.setMaxId(nextId);
	}

	/**
	 * Method that returns the current team.
	 * 
	 * @return The team.
	 */
	public Team getTeam() {
		return team;
	}

}
