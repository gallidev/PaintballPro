package logic;

import java.util.ArrayList;

import enums.TeamEnum;
import players.AIPlayer;

public class OfflineTeam {

	private ArrayList<AIPlayer> members;
	private TeamEnum colour;
	private int score;
	
	public OfflineTeam(ArrayList<AIPlayer> members, TeamEnum colour){
		this.members = members;
		score = 0;
		this.colour = colour;
	}

	public void addMember(AIPlayer p){
		members.add(p);
	}
	
	public void updateScore(int additionalScore){
		score += additionalScore;
	}
	
	public void incrementScore(){
		score++;
	}
	
	/* Getters and setters */
	
	public ArrayList<AIPlayer> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<AIPlayer> members) {
		this.members = members;
	}
	public TeamEnum getColour() {
		return colour;
	}
	public void setColour(TeamEnum colour) {
		this.colour = colour;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
