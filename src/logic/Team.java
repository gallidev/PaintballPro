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
	
	/**
	 * Increments the score of the team with a given number of points
	 * @param additionalScore The new points gained by the team
	 */
	public void incrementScore(int additionalScore){
		score += additionalScore;
	}
	
	/**
	 * Adds another player to the team and increments the number of team players.
	 * @param p The new team player.
	 */
	public void addMember(Player p){
		members[membersNo] = p;
		membersNo++;
	}
	
	 /**
	 * Change one of the team player's location.
	 * @param p The player to be moved.
	 * @param newXCoord The new x coordinate of the player.
	 * @param newYCoord The new y coordinate of the player.
	 */
	public void updatePlayerLocation(Player p, int newXCoord, int newYCoord){
		p.setXCoordinate(newXCoord);
		p.setYCoordinate(newYCoord);
	}
	
	/* Getters and setters */
	
	public int getMembersNo() {
		return membersNo;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int newScore){
		score = newScore;
	}
	

	public Player[] getMembers(){
		return members;
	}
	
	
}
