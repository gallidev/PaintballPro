package logic;

import physics.Player;
import rendering.Map;

public class PlayGame {
	
	private Map gameMap;
	private Team team1;
	private Team team2;
	
	public PlayGame(){
		gameMap = new Map();
	}
	
	private void initializeTeamsWithPlayers(Player[] team1Players, Player[] team2Players){
		team1.setMembers(team1Players);
		team2.setMembers(team2Players);
	}

	//set the initial position of all players
	
	
	//create players & teams
	
	//start a game in a given mode
	
	//???continuously check if the game is finished
}
