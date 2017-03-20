package networking.client;

import java.util.ArrayList;

import players.EssentialPlayer;

/**
 * Stores teams for each client - their team and enemy team by their perspective.
 *
 * @author Matthew Walters
 */
public class TeamTable {
	private ArrayList<EssentialPlayer> enemyTeam;
	private ArrayList<EssentialPlayer> myTeam;
	
	/**
	 * Set up teams.
	 */
	public TeamTable()
	{
		enemyTeam = new ArrayList<EssentialPlayer>();
		myTeam = new ArrayList<EssentialPlayer>();
	}

	/**
	 * Return team myTeam.
	 * @return myTeam arraylist.
	 */
	public ArrayList<EssentialPlayer> getMyTeam() {
		return myTeam;
	}

	/**
	 * Sets contents of myTeam arraylist.
	 * @param myTeam Representation of myTeam arraylist.
	 */
	public void setMyTeam(ArrayList<EssentialPlayer> myTeam) {
		this.myTeam = myTeam;
	}

	/**
	 * Return team enemies.
	 * @return enemies arraylist.
	 */
	public ArrayList<EssentialPlayer> getEnemies() {
		return enemyTeam;
	}

	/**
	 * Sets contents of enemies arraylist.
	 * @param enemies Representation of enemies arraylist.
	 */
	public void setEnemies(ArrayList<EssentialPlayer> enemies) {
		this.enemyTeam = enemies;
	}
}