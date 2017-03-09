package ai;

import enums.TeamEnum;
import physics.CollisionsHandler;
import players.AIPlayer;
import players.ServerMinimumPlayer;
import rendering.Map;
import serverLogic.Team;

public class AIManager extends Thread {

	private Team team;
	private Map map;
	private CollisionsHandler collissionsHandler;
	
	public AIManager(Team t, Map m, CollisionsHandler ch){
		super();
		team = t;
		this.map = m;
		this.collissionsHandler = ch;
	}
	
	public void run(){
		int currentPlayersNo = team.getMembersNo();
		
		while (currentPlayersNo < 4){
			int newID = 0;
			if (team.getColour() == TeamEnum.RED)
				newID = currentPlayersNo;
			else
				newID = currentPlayersNo + 4;
			
			ServerMinimumPlayer newPlayer = new AIPlayer(map.getSpawns()[newID].x * 64, map.getSpawns()[newID].y * 64, newID, map, TeamEnum.RED,collissionsHandler);
			team.addMember(newPlayer);
			currentPlayersNo++;
		}
	
	}
	
}
