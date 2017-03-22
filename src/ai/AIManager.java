package ai;

import enums.TeamEnum;
import integration.server.ServerGameSimulation;
import logic.server.Team;
import networking.server.Lobby;
import physics.CollisionsHandler;
import players.AIPlayer;
import players.EssentialPlayer;
import rendering.Map;

public class AIManager{

	private Team team;
	private Map map;
	private CollisionsHandler collissionsHandler;
	int nextId;
	private HashMapGen hashMaps;

	public AIManager(Team t, Map m, CollisionsHandler ch, int maxId, HashMapGen hm){
		super();
		this.team = t;
		this.map = m;
		this.collissionsHandler = ch;
		nextId = maxId + 1;
		hashMaps = hm;
	}

	public void createPlayers(){
		int currentPlayersNo = team.getMembersNo();


		while (currentPlayersNo < 4){
			int spawnLoc = 0;

			if (team.getColour() == TeamEnum.RED)
				spawnLoc = currentPlayersNo;
			else
				spawnLoc = currentPlayersNo + 4;

			EssentialPlayer newPlayer = new AIPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, nextId, map, team.getColour(), collissionsHandler, hashMaps, map.getGameMode(), ServerGameSimulation.GAME_HERTZ);

			System.out.println("Created AI with id " + newPlayer.getPlayerId());
			team.addMember(newPlayer);
			currentPlayersNo++;
			nextId++;
		}

		Lobby.setMaxId(nextId);
		System.out.println("new max id AI = " + nextId);
	}

	public Team getTeam(){
		return team;
	}



}
