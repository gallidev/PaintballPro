package logic.server;

import enums.TeamEnum;
import players.EssentialPlayer;

import java.util.ArrayList;

/**
 * Class to represent a team of players in the game.
 * 
 * @author Alexandra Paduraru
 *
 */
public class Team {

	private TeamEnum colour;
	private ArrayList<EssentialPlayer> members;
	private int score;

	/**
	 * Initialises a new empty team, with 0 members and no score.
	 */
	public Team(TeamEnum colour) {
		members = new ArrayList<>();
		score = 0;
		this.colour = colour;
	}

	/**
	 * Increments the score of the team with a given number of points
	 * 
	 * @param additionalScore
	 *            The new points gained by the team
	 */
	public void incrementScore(int additionalScore) {
		score += additionalScore;
	}

	public void incrementScore() {
		score++;
	}

	/**
	 * Adds another player to the team and increments the number of team
	 * players.
	 * 
	 * @param p
	 *            The new team player.
	 */
	public void addMember(EssentialPlayer p) {
		members.add(p);
		colour = p.getColour();
	}

	/*
	 * Returns the number of players in the game.
	 */
	public int getMembersNo() {
		return members.size();
	}

	/**
	 * Checks whether or not a given player is a memebr of the team.
	 * 
	 * @param player
	 *            The player which needs to be tested.
	 * @return Whether or not the player is in the team.
	 */
	public boolean containsPlayer(EssentialPlayer player) {
		for (EssentialPlayer p : getMembers())
			if (p == player)
				return true;
		return false;
	}

	/**
	 * Get the team's score.
	 * 
	 * @return The current team score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Change the team's score.
	 * 
	 * @param newScore
	 *            The team's new score.
	 */
	public void setScore(int newScore) {
		score = newScore;
	}

	/**
	 * Method to retrieve the team's members.
	 * 
	 * @return The list of team members.
	 */
	public ArrayList<EssentialPlayer> getMembers() {
		return members;
	}

	/**
	 * Adds players as a member of the team.
	 * 
	 * @param teamPlayers
	 *            The array of players to team.
	 */
	public void setMembers(ArrayList<EssentialPlayer> teamPlayers) {
		members = teamPlayers;

		if (!teamPlayers.isEmpty())
			colour = teamPlayers.get(0).getColour();
	}

	/**
	 * Method to retrieve the team's colour.
	 * 
	 * @return The team's colour.
	 */
	public TeamEnum getColour() {
		return colour;
	}

	/**
	 * Method to change the team's colour.
	 * 
	 * @param c
	 *            The new team colour.
	 */
	public void setColour(TeamEnum c) {
		colour = c;
	}

}
