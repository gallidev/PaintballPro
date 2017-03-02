package networking.client;

import java.util.ArrayList;

import players.ClientLocalPlayer;

/**
 * Stores teams for each client - their team and enemy team by their perspective.
 * 
 * @author MattW
 */
public class TeamTable {
	private ArrayList<ClientLocalPlayer> myTeam;
	private ArrayList<ClientLocalPlayer> enemies;
	
	/**
	 * Set up teams.
	 */
	public TeamTable()
	{
		myTeam = new ArrayList<ClientLocalPlayer>();
		enemies = new ArrayList<ClientLocalPlayer>();
	}

	/**
	 * Return team myTeam.
	 * @return myTeam arraylist.
	 */
	public ArrayList<ClientLocalPlayer> getMyTeam() {
		return myTeam;
	}

	/**
	 * Sets contents of myTeam arraylist.
	 * @param myTeam Representation of myTeam arraylist.
	 */
	public void setMyTeam(ArrayList<ClientLocalPlayer> myTeam) {
		this.myTeam = myTeam;
	}

	/**
	 * Return team enemies.
	 * @return enemies arraylist.
	 */
	public ArrayList<ClientLocalPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * Sets contents of enemies arraylist.
	 * @param enemies Representation of enemies arraylist.
	 */
	public void setEnemies(ArrayList<ClientLocalPlayer> enemies) {
		this.enemies = enemies;
	}
}
