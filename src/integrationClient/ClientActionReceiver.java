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

	/**
	 * Initializes a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 * 
	 * @param player
	 *            The player upon which the actions take place.
	 */
	public ClientActionReceiver(GhostPlayer player) {
		this.player = player;
	}


}
