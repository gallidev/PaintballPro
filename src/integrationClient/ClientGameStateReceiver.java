package integrationClient;

import javafx.application.Platform;
import physics.Flag;
import players.EssentialPlayer;
import players.GhostPlayer;

import java.util.ArrayList;

/**
 * Client-sided class which receives an action imposed by the server on the
 * player and executes it.
 *
 * @author Alexandra Paduraru
 *
 */
public class ClientGameStateReceiver {

	private ArrayList<EssentialPlayer> players;

	private static final boolean debug = false;
	private Flag flag;


	/**
	 * Initialises a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param players The list of all players in the game.
	 *
	 */
	public ClientGameStateReceiver(ArrayList<EssentialPlayer> players) {
		this.players = players;
	}

	/**
	 * Initialises a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param players The list of all players in the game.
	 * @param flag The flag of the capture the flag mode
	 */
	public ClientGameStateReceiver(ArrayList<EssentialPlayer> players, Flag flag) {
		this.players = players;
		this.flag = flag;
	}

	/**
	 * Updates a ghost player's location.
	 * @param id The id of the player which has changed location.
	 * @param x The new x coordinate of the player.
	 * @param y The new y coordinate of the player.
	 * @param angle The new angle of the player.
	 * @param visible Whether or not the player is visible(i.e. it has been eliminated>
	 */
	public void updatePlayer(int id, double x, double y, double angle, boolean visible){

		EssentialPlayer playerToBeUpdated = getPlayerWithId(id);
		//System.out.println("angle :" + angle);
		Platform.runLater(() ->
		{
			playerToBeUpdated.relocate(x, y);
			playerToBeUpdated.setAngle(angle);
			playerToBeUpdated.setVisible(visible);
		});
		if (debug) System.out.println("updated player with id : " + id);
	}

	/**
	 * Update a player's active bullets.
	 * @param id The id of the player.
	 * @param bullets String which contains the coordinates and the angle of the bullets fired by this player,
	 * 				  according to the protocol.
	 */

	public void updateBullets(int id){
		EssentialPlayer p = getPlayerWithId(id);
		if(p!= null){
			//p.shoot();
		}
	}

	public void updateFlag(int id){

		EssentialPlayer player = getPlayerWithId(id);
		player.setHasFlag(true);
		flag.setVisible(false);

		System.out.println("Player " + id + " captured the flag");
	}

	public void lostFlag(int id){

		EssentialPlayer player = getPlayerWithId(id);
		player.setHasFlag(false);
		flag.setVisible(true);
		flag.relocate(player.getLayoutX(), player.getLayoutY());

		System.out.println("Player " + id + " lost the flag");

	}

	public void respawnFlag(int id, double x, double y){
		flag.setVisible(true);
		flag.relocate(x, y);

		EssentialPlayer player = getPlayerWithId(id);
		player.setHasFlag(false);
		System.out.println("Flag has been respawned");

	}


	/**
	 * Helper method to find the player with a specific id from the entire list of players in the game.
	 * @param id The player's id.
	 */
	public EssentialPlayer getPlayerWithId(int id){
		for (EssentialPlayer p : players){
			if (p.getPlayerId() == id)
				return p;
		}
		return null;
	}



}
