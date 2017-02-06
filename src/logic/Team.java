package logic;

import physics.Player;

/**
 * Class to represent a team of players in the game.
 * @author Alexandra Paduraru
 *
 */
public class Team {

	private Player[] members;
	private int membersNo;
	private int score;
	
	/**
	 * Initialises a new empty team, with 0 members and no score.
	 */
	public Team(){
		members = new Player[4];
		membersNo = 0;
		score = 0;
	}
	
	public int getMembersNo() {
		return membersNo;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int newScore){
		score = newScore;
	}
	
	public void incrementScore(int additionalScore){
		score += additionalScore;
	}

	public Player[] getMembers(){
		return members;
	}
	
	public void addMember(Player p){
		members[membersNo] = p;
		membersNo++;
	}
	
	
	//!!!!Override equals
	public void updatePlayerLocation(Player p, int newXCoord, int newYCoord){
		for (int i = 0; i < membersNo; i++)
			if (members[i].equals(p)){
				members[i].setXCoord = newXCoord;
				members[i].setYCoord = newYCoord;

			}
				
	}
	
}
