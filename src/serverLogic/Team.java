package serverLogic;

import enums.TeamEnum;
import oldCode.players.ServerPlayer;
import players.EssentialPlayer;

import java.util.ArrayList;


/**
 * Class to represent a team of players in the game.
 * @author Alexandra Paduraru
 *
 */
public class Team {

	private ArrayList<EssentialPlayer> members;
	private int score;
	private TeamEnum colour;

	/**
	 * Initialises a new empty team, with 0 members and no score.
	 */
	public Team(TeamEnum colour){
		members = new ArrayList<>();
		score = 0;
		this.colour = colour;
	}

	/**
	 * Increments the score of the team with a given number of points
	 * @param additionalScore The new points gained by the team
	 */
	public void incrementScore(int additionalScore){
		score += additionalScore;
	}
	
	public void incrementScore(){
		score++;
	}

	/**
	 * Adds another player to the team and increments the number of team players.
	 * @param p The new team player.
	 */
	public void addMember(EssentialPlayer p){
		members.add(p);
		colour = p.getColour();
	}


	public int getMembersNo() {
		return members.size();
	}

	public boolean containsPlayer(EssentialPlayer player){
		for(EssentialPlayer p: getMembers())
			if ( p == player )
				return true;
		return false;
	}
	

	/* Getters and setters */

	public int getScore() {
		return score;
	}

	public void setScore(int newScore){
		score = newScore;
	}


	public ArrayList<EssentialPlayer> getMembers(){

		return members;
	}

	/**
	 * Adds players as a member of the team.
	 * @param teamPlayers The array of players to team.
	 */
	public void setMembers(ArrayList<EssentialPlayer> teamPlayers) {
		members = teamPlayers;
		
		if (!teamPlayers.isEmpty())
		colour = teamPlayers.get(0).getColour();
	}

	public TeamEnum getColour(){
		return colour;
	}

	public void setColour(TeamEnum c){
		colour = c;
	}

//	public void setMap(Map map){
//		for (ServerPlayer p : members){
//			p.setMap(map);
//		}
//	}
	

}
