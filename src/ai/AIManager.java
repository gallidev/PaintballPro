package ai;

import enums.TeamEnum;
import networking.server.Lobby;
import physics.CollisionsHandler;
import players.AIPlayer;
import players.EssentialPlayer;
import rendering.Map;
import serverLogic.Team;

public class AIManager{

	private Team team;
	private Map map;
	private CollisionsHandler collissionsHandler;
	int nextId;

	public AIManager(Team t, Map m, CollisionsHandler ch, int maxId){
		super();
		team = t;
		this.map = m;
		this.collissionsHandler = ch;
		nextId = maxId + 1;
	}

	public void createPlayers(){
		int currentPlayersNo = team.getMembersNo();
		HashMapGen hashMaps = new HashMapGen(map);
		
		while (currentPlayersNo < 4){
			int spawnLoc = 0;
			if (team.getColour() == TeamEnum.RED)
				spawnLoc = currentPlayersNo;
			else
				spawnLoc = currentPlayersNo + 4;

			EssentialPlayer newPlayer = new AIPlayer(map.getSpawns()[spawnLoc].x * 64, map.getSpawns()[spawnLoc].y * 64, nextId, map, team.getColour(), collissionsHandler, hashMaps);
		
			System.out.println("Created AI with id " + newPlayer.getPlayerId());
			
			team.addMember(newPlayer);
			currentPlayersNo++;
			nextId++;
		}
		
		Lobby.setMaxId(nextId);

	}

}
