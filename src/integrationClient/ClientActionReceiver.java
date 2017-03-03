package integrationClient;

import players.GhostPlayer;

/**
 * Client-sided class which receives an action imposed by the server on the
 * player and executes it.
 * 
 * @author Alexandra Paduraru
 *
 */
public class ClientActionReceiver {

	private GhostPlayer player;
	private int id;

	/**
	 * Initializes a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 * 
	 * @param player
	 *            The player upon which the actions take place.
	 */
	public ClientActionReceiver(GhostPlayer player) {
		this.player = player;
		id = player.getPlayerId();
	}
	
	//???NOT SURE
	public void updatePlayer(double x, double y, double angle){
		player.setX(x);
		player.setY(y);
	}


}
