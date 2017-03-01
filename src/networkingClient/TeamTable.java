package networkingClient;

import java.util.ArrayList;

import players.ClientLocalPlayer;

public class TeamTable {
	private ArrayList<ClientLocalPlayer> myTeam;
	private ArrayList<ClientLocalPlayer> enemies;
	
	public TeamTable()
	{
		
	}

	public ArrayList<ClientLocalPlayer> getMyTeam() {
		return myTeam;
	}

	public void setMyTeam(ArrayList<ClientLocalPlayer> myTeam) {
		this.myTeam = myTeam;
	}

	public ArrayList<ClientLocalPlayer> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<ClientLocalPlayer> enemies) {
		this.enemies = enemies;
	}
	
	
	
}
