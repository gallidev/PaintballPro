package integration.client;

import audio.AudioManager;
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

	// for testing purposes
	public boolean integrationTest;

	private static final boolean debug = false;
	private Flag flag;
	private ArrayList<EssentialPlayer> players;
	private EssentialPlayer currentPlayer;
	private Powerup[] powerups;
	private AudioManager audio;

	/**
	 * Initialises a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param players
	 *            The list of all players in the game.
	 *
	 */
	public ClientGameStateReceiver(ArrayList<EssentialPlayer> players, EssentialPlayer currentPlayer, Powerup[] powerups, AudioManager audio){
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.powerups = powerups;
		this.audio = audio;
	}

	/**
	 * Initialises a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param players
	 *            The list of all players in the game.
	 * @param flag
	 *            The flag of the capture the flag mode
	 */
	public ClientGameStateReceiver(ArrayList<EssentialPlayer> players, EssentialPlayer currentPlayer, Flag flag, Powerup[] powerups, AudioManager audio) {
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.flag = flag;
		this.powerups = powerups;
		this.audio = audio;
	}

	/**
	 * Updates a ghost player's location.
	 *
	 * @param id
	 *            The id of the player which has changed location.
	 * @param x
	 *            The new x coordinate of the player.
	 * @param y
	 *            The new y coordinate of the player.
	 * @param angle
	 *            The new angle of the player.
	 * @param visible
	 *            Whether or not the player is visible(i.e. it has been
	 *            eliminated>
	 */
	public void updatePlayer(int id, double x, double y, double angle, boolean visible, boolean eliminated) {

		EssentialPlayer playerToBeUpdated;
		playerToBeUpdated = getPlayerWithId(id);
		// System.out.println("angle :" + angle);

		if (playerToBeUpdated instanceof ClientPlayer) {

			ClientPlayer cPlayer = (ClientPlayer) playerToBeUpdated;
			Platform.runLater(() -> {
				if (cPlayer.shouldIUpdatePosition(x, y)) {
					playerToBeUpdated.relocate(x, y);
				}
				// playerToBeUpdated.setAngle(angle);
				playerToBeUpdated.setVisible(visible);
				playerToBeUpdated.setEliminated(eliminated);
			});

		} else {
			Platform.runLater(() -> {
				playerToBeUpdated.relocatePlayerWithTag(x, y);
				playerToBeUpdated.setAngle(angle);
				playerToBeUpdated.setVisible(visible);
				playerToBeUpdated.setEliminated(eliminated);
			});
		}

		if (debug)
			System.out.println("updated player with id : " + id);
	}

	/**
	 * Update a player, when it has picked up a power-up.
	 * @param id The player's id.
	 * @param type The type of power-up picked.
	 */
	public void powerupAction(int id, PowerupType type) {
		EssentialPlayer player;
		player = getPlayerWithId(id);

		switch (type) {
		case SHIELD:
			powerups[0].setVisible(false);
			player.setShield(true);
			audio.playSFX(audio.sfx.pickup, (float)1.0);
			break;
		case SPEED:
			powerups[1].setVisible(false);
			player.setSpeed(true);
			audio.playSFX(audio.sfx.pickup, (float)1.0);
			break;
		default: break;
		}

	}

	/**
	 * Make a power-up respawn after it has been picked.
	 * @param type The power-up type.
	 * @param location The new location of the power-up.
	 */
	public void powerUpRespawn(PowerupType type, int location) {
		switch (type) {
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

	/**
	 * Remove a player's shield.
	 * @param id The id of the player that needs to be updated.
	 */
	public void shieldRemovedAction(int id) {
		EssentialPlayer player;
		player = getPlayerWithId(id);
		player.setShieldEffect(false);
	}

	/**
	 * Generate a player's bullets.
	 * @param playerId The player id.
	 * @param bulletId The first bullet's id.
	 * @param originX The x coordinate of the original location of the player when it shot.
	 * @param originY The y coordinate of the original location of the player when it shot.
	 * @param angle The angle of the player when it shot.
	 */
	public void generateBullet(int playerId, int bulletId, double originX, double originY, double angle) {
		EssentialPlayer p = getPlayerWithId(playerId);
		if (p.equals(currentPlayer))
			audio.playSFX(audio.sfx.getRandomPaintball(), (float)1.0);
		if (p != null) {
			Platform.runLater(() -> {
				p.generateBullet(bulletId, originX, originY, angle);
			});
		}
	}

	/**
	 * Destroyed the bullets when they have to disappear from the map.
	 * @param playerId The player's id whose bullets need to be destroyed.
	 * @param bulletId The id of the bullet that needs to be destroyed.
	 */
	public void destroyBullet(int playerId, int bulletId) {
		EssentialPlayer p;
		p = getPlayerWithId(playerId);

		if (p != null) {
			Bullet b = getBulletWithId(p, bulletId);
			if (b != null) {
				Platform.runLater(() -> {
					if(debug) System.out.println("destroyed bullet : " + playerId + " bulletid: " + bulletId);
					b.setVisible(false);
					b.setActive(false);
				});
			}
		}

	}

	/**
	 * Update the flag and player visibility when a player has captured the flag.
	 * @param id The id of the player which captured the flag.
	 */
	public void updateFlag(int id) {
		EssentialPlayer player;
		player = getPlayerWithId(id);

		audio.playSFX(audio.sfx.flagcollect, (float)1.0);

		Platform.runLater(() -> {
			player.setHasFlag(true);
			flag.setVisible(false);
		});

	}

	/**
	 * Update the flag and player visibility when a player has lost the flag.
	 * @param id The id of the player which lost the flag.
	 */
	public void lostFlag(int id) {
		EssentialPlayer player = getPlayerWithId(id);
		Platform.runLater(() -> {
			player.setHasFlag(false);
			flag.setVisible(true);
			flag.relocate(player.getLayoutX(), player.getLayoutY());
		});

	}

	/**
	 * Update the flag visibility and location when a flag needs to be respawned.
	 * @param id The id of the player which captured the flag.
	 */
	public void respawnFlag(int id, double x, double y) {
		flag.setVisible(true);
		flag.relocate(x, y);

		EssentialPlayer player;
		player = getPlayerWithId(id);

		Platform.runLater(() -> {
			flag.setVisible(true);
			flag.relocate(x, y);
			player.setHasFlag(false);
		});
	}

	/**
	 * Helper method to find the player with a specific id from the entire list
	 * of players in the game.
	 *
	 * @param id
	 *            The player's id.
	 */
	public EssentialPlayer getPlayerWithId(int id) {
		for (EssentialPlayer p : players) {
			if (p.getPlayerId() == id){
				return p;
			}
		}
		return null;
	}

	/**
	 * Returns the bullet with a given id.
	 * @param p The player whose bullet is looked for.
	 * @param id The bullet id to be found.
	 * @return The bullet with a given id nelonging to the given player.
	 */
	public Bullet getBulletWithId(EssentialPlayer p, int id) {
		for (Bullet b : p.getBullets()) {
			if (b.getBulletId() == id)
				return b;
		}
		return null;
	}

	/**
	 * Returns the game flag.
	 * @return The flag in the game.
	 */
	public Flag getFlag() {
		return flag;
	}

	/**
	 * Returns an array of power-ups available in the game.
	 * @return The game power-ups.
	 */
	public Powerup[] getPowerups() {
		return powerups;
	}

}
