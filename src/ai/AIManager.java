package ai;

import java.util.ArrayList;

import enums.TeamEnum;
import physics.CollisionsHandler;
import players.AIPlayer;
import players.EssentialPlayer;
import rendering.Map;
import serverLogic.Team;

public class AIManager{

	private Team team;
	private Map map;
	private CollisionsHandler collissionsHandler;

	public AIManager(Team t, Map m, CollisionsHandler ch){
		super();
		this.team = t;
		this.map = m;
		this.collissionsHandler = ch;
	}

	public void createPlayers(){
		int currentPlayersNo = team.getMembersNo();
		HashMapGen hashMaps = new HashMapGen(map);

		while (currentPlayersNo < 2){
			int newID = 0;
			if (team.getColour() == TeamEnum.RED)
				newID = currentPlayersNo;
			else
				newID = currentPlayersNo + 4;

			System.out.println("new id = " + newID);
			EssentialPlayer newPlayer = new AIPlayer(map.getSpawns()[newID].x * 64, map.getSpawns()[newID].y * 64, newID, map, team.getColour(), collissionsHandler, hashMaps);

			team.addMember(newPlayer);
			currentPlayersNo++;
		}

		ArrayList<EssentialPlayer> yourTeam = team.getMembers();
		for(int i = 0; i<yourTeam.size(); i++){
			yourTeam.get(i).setMyTeam(team);
		}
	}

	public void setOpponents(Team oppTeam){
		ArrayList<EssentialPlayer> yourTeam = team.getMembers();
		for(int i = 0; i < yourTeam.size(); i++){
			yourTeam.get(i).setOppTeam(oppTeam);
		}
	}



}
