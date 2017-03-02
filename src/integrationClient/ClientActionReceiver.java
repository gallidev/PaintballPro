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

	// can this be a user player as well?
	// need to discuss what to do here to the GhostPlayer
	
	public void movePlayerDown(){
		
	}
	
	public void movePlayerUp(){
		
	}
	
	public void movePlayerLeft(){
		
	}

	public void movePlayerRight(){
	
	}
	
	//+++method for the mouse

}
