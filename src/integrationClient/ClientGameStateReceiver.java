package integrationClient;

import javafx.application.Platform;
import physics.Flag;
import physics.PowerupType;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.GhostPlayer;

import java.util.ArrayList;

import static gui.GUIManager.renderer;

/**
 * Client-sided class which receives an action imposed by the server on the
 * player and executes it.
 *
 * @author Alexandra Paduraru
 *
 */
public class ClientGameStateReceiver {

	private static final boolean debug = false;
	private ArrayList<EssentialPlayer> players;
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
	public void updatePlayer(int id, double x, double y, double angle, boolean visible, boolean eliminated){

		EssentialPlayer playerToBeUpdated = getPlayerWithId(id);
		//System.out.println("angle :" + angle);

		if(playerToBeUpdated instanceof ClientPlayer){

			ClientPlayer cPlayer = (ClientPlayer) playerToBeUpdated;
			Platform.runLater(() ->
			{
				if(cPlayer.shouldIUpdatePosition(x, y)){
					playerToBeUpdated.relocate(x, y);
				}
				//playerToBeUpdated.setAngle(angle);
				playerToBeUpdated.setVisible(visible);
				playerToBeUpdated.setEliminated(eliminated);
			});

		}else{
			Platform.runLater(() ->
			{
				playerToBeUpdated.relocatePlayerWithTag(x, y);
				playerToBeUpdated.setAngle(angle);
				playerToBeUpdated.setVisible(visible);
				playerToBeUpdated.setEliminated(eliminated);
			});
		}


		if (debug) System.out.println("updated player with id : " + id);
	}

	public void powerupAction(int id, PowerupType type)
	{
		GhostPlayer player = getPlayerWithId(id);
		switch(type)
		{
			case SHIELD:
				renderer.getMap().getPowerups()[0].setVisible(false);
				player.setShieldEffect(true);
				break;
			case SPEED:
				renderer.getMap().getPowerups()[1].setVisible(false);
				break;
		}

	}

	/**
	 * Update a player's active bullets.
	 * @param id The id of the player.
	 * @param bullets String which contains the coordinates and the angle of the bullets fired by this player,
	 * 				  according to the protocol.
	 */

	public void updateBullets(int id){
		EssentialPlayer p = getPlayerWithId(id);

		if(p != null &&  !(p instanceof ClientPlayer)){
			Platform.runLater(() ->
			{
				p.shoot();
			});
		}
	}

	public void updateFlag(int id){

		EssentialPlayer player = getPlayerWithId(id);
		player.setHasFlag(true);
		flag.setVisible(false);

		GhostPlayer player = getPlayerWithId(id);
		Platform.runLater(() -> {
			player.setFlagStatus(true);
			flag.setVisible(false);
		});

		System.out.println("Player " + id + " captured the flag");
	}

	public void lostFlag(int id){

		EssentialPlayer player = getPlayerWithId(id);
		player.setHasFlag(false);
		flag.setVisible(true);
		flag.relocate(player.getLayoutX(), player.getLayoutY());

		GhostPlayer player = getPlayerWithId(id);
		Platform.runLater(() -> {
			player.setFlagStatus(false);
			flag.setVisible(true);
			flag.relocate(player.getLayoutX(), player.getLayoutY());
		});

		System.out.println("Player " + id + " lost the flag");

	}

	public void respawnFlag(int id, double x, double y){
		flag.setVisible(true);
		flag.relocate(x, y);

		EssentialPlayer player = getPlayerWithId(id);
		player.setHasFlag(false);
		GhostPlayer player = getPlayerWithId(id);

		Platform.runLater(() -> {
			flag.setVisible(true);
			flag.relocate(x, y);
			player.setFlagStatus(false);
		});

		System.out.println("Flag has been respawned");

	}

	public void updateSpeedGame(){
		for (EssentialPlayer p : players){
			p.updateGameSpeed();
		}
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
