package integration.client;

import javafx.application.Platform;
import physics.Bullet;
import physics.Flag;
import physics.Powerup;
import physics.PowerupType;
import players.ClientPlayer;
import players.EssentialPlayer;

import java.util.ArrayList;

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
	private Powerup[] powerups;
	public boolean integrationTest;

	/**
	 * Initialises a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param players The list of all players in the game.
	 *
	 */
	public ClientGameStateReceiver(ArrayList<EssentialPlayer> players, Powerup[] powerups) {
		this.players = players;
		this.powerups = powerups;
	}

	/**
	 * Initialises a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param players The list of all players in the game.
	 * @param flag The flag of the capture the flag mode
	 */
	public ClientGameStateReceiver(ArrayList<EssentialPlayer> players, Flag flag, Powerup[] powerups) {
		this.players = players;
		this.flag = flag;
		this.powerups = powerups;
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
		EssentialPlayer player = getPlayerWithId(id);
		switch(type)
		{
			case SHIELD:
				powerups[0].setVisible(false);
				player.giveShield();
				break;
			case SPEED:
				powerups[1].setVisible(false);
				player.giveSpeed();
				break;
		}

	}

	public void powerUpRespawn(PowerupType type, int location)
	{
		switch(type)
		{
			case SHIELD:
				powerups[0].resetPosition(location);
				powerups[0].setVisible(true);
				break;
			case SPEED:
				powerups[1].resetPosition(location);
				powerups[1].setVisible(true);
				break;
		}
	}

	public void shieldRemovedAction(int id)
	{
		EssentialPlayer player = getPlayerWithId(id);
		player.setShieldEffect(false);
	}


	public void generateBullet(int playerId, int bulletId, double originX, double originY, double angle){
		EssentialPlayer p = getPlayerWithId(playerId);

		if(p != null){
			Platform.runLater(() -> {
				p.generateBullet(bulletId, originX, originY, angle);
			});
		}
	}

	public void destroyBullet(int playerId, int bulletId) {
		EssentialPlayer p = getPlayerWithId(playerId);

		if(p!=null){
			Bullet b = getBulletWithId(p, bulletId);
			if(b != null){
				Platform.runLater(() -> {
					System.out.println("destroyed bullet : " + playerId + " bulletid: " + bulletId);
					b.setVisible(false);
					b.setActive(false);
				});
			}
		}


	}

	public void updateFlag(int id){
		EssentialPlayer player = getPlayerWithId(id);

		Platform.runLater(() -> {
			player.setHasFlag(true);
			flag.setVisible(false);
		});

		System.out.println("Player " + id + " captured the flag");
	}

	public void lostFlag(int id){
		EssentialPlayer player = getPlayerWithId(id);
		Platform.runLater(() -> {
			player.setHasFlag(false);
			flag.setVisible(true);
			flag.relocate(player.getLayoutX(), player.getLayoutY());
		});

		System.out.println("Player " + id + " lost the flag");

	}

	public void respawnFlag(int id, double x, double y){
		flag.setVisible(true);
		flag.relocate(x, y);

		EssentialPlayer player = getPlayerWithId(id);

		Platform.runLater(() -> {
			flag.setVisible(true);
			flag.relocate(x, y);
			player.setHasFlag(false);
		});

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

	public Bullet getBulletWithId(EssentialPlayer p, int id){
		for (Bullet b : p.getBullets()){
			if (b.getBulletId() == id)
				return b;
		}
		return null;
	}

	public Flag getFlag()
	{
		return flag;
	}

	public Powerup[] getPowerups()
	{
		return powerups;
	}

}
