package offlineLogic;

import enums.TeamEnum;
import players.AIPlayer;

import java.util.ArrayList;

/**
 * A team of just AI players which can be used for the single player mode.
 * @author Alexandra Paduraru
 *
 */
public class OfflineTeam {

	private ArrayList<AIPlayer> members;
	private TeamEnum colour;
	private int score;
	
	/**
	 * Initializes the team with its members and the team colour.
	 * @param members The list of players in the team.
	 * @param colour The enum corresponding to the team colour.
	 */
	public OfflineTeam(ArrayList<AIPlayer> members, TeamEnum colour){
		this.members = members;
		score = 0;
		this.colour = colour;
	}

	/**
	 * Adds an additional player to the team.
	 * @param p The new player to be added in the team.
	 */
	public void addMember(AIPlayer p){
		members.add(p);
	}
	
	/**
	 * Increases the team score by a specific amount.
	 * @param additionalScore The number of points that have to be added to the current team score.
	 */
	public void updateScore(int additionalScore){
		score += additionalScore;
	}
	
	/**
	 * Increases the team score by 1 point.
	 */
	public void incrementScore(){
		score++;
	}
	
	/* Getters and setters */
	
	/**
	 * Retrieves the team players.
	 * @return The list of AI players in the team.
	 */
	public ArrayList<AIPlayer> getMembers() {
		return members;
	}

	/**
	 * Sets the team players.
	 * @param members The list of the new AI players that will form the team.
	 */
	public void setMembers(ArrayList<AIPlayer> members) {
		this.members = members;
	}
	
	/**
	 * Returns the colour of the team as a TeamEnum.
	 * @return The team colour.
	 */
	public TeamEnum getColour() {
		return colour;
	}
	
	/**
	 * Changes the team colour.
	 * @param colour The new team colour.
	 */
	public void setColour(TeamEnum colour) {
		this.colour = colour;
	}
	
	/**
	 * Returns the team score.
	 * @return The score gained by the team so far.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Changes the team score.
	 * @param score The new team score.
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
}
