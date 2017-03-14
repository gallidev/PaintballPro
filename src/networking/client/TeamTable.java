package networking.client;

import java.util.ArrayList;
import players.GhostPlayer;

/**
 * Stores teams for each client - their team and enemy team by their perspective.
 *
 * @author Matthew Walters
 */
public class TeamTable {
	private ArrayList<GhostPlayer> myTeam;
	private ArrayList<GhostPlayer> enemies;

	/**
	 * Set up teams.
	 */
	public TeamTable()
	{
		myTeam = new ArrayList<GhostPlayer>();
		enemies = new ArrayList<GhostPlayer>();
	}

	/**
	 * Return team myTeam.
	 * @return myTeam arraylist.
	 */
	public ArrayList<GhostPlayer> getMyTeam() {
		return myTeam;
	}

	/**
	 * Sets contents of myTeam arraylist.
	 * @param myTeam Representation of myTeam arraylist.
	 */
	public void setMyTeam(ArrayList<GhostPlayer> myTeam) {
		this.myTeam = myTeam;
	}

	/**
	 * Return team enemies.
	 * @return enemies arraylist.
	 */
	public ArrayList<GhostPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * Sets contents of enemies arraylist.
	 * @param enemies Representation of enemies arraylist.
	 */
	public void setEnemies(ArrayList<GhostPlayer> enemies) {
		this.enemies = enemies;
	}
}