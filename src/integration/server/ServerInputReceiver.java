package integration.server;

import java.util.ArrayList;

import networking.server.ServerSender;
import players.EssentialPlayer;
import players.UserPlayer;

/**
 * Receives input information (server-side) from all clients and updates the
 * server game information accordingly.
 *
 * The class performs the computation required server-side(collision-handling,
 * etc)
 *
 * @author Alexandra Paduraru
 *
 */
public class ServerInputReceiver {

	private boolean debug = false;
	private ArrayList<EssentialPlayer> players;

	/**
	 * Initialises a new Server input receiver with all the players currently
	 * playing in the game.
	 *
	 * @param players
	 *            A list of all the players involved in the game.
	 */
	public ServerInputReceiver(ArrayList<EssentialPlayer> players) {
		super();
		this.players = players;
	}

	public ServerInputReceiver() {
		super();
	}

	/**
	 * Computes all the required changes on a player, based on the inputs
	 * received from the client. This method performs all the collision handling
	 * and updates the player based on that and the user input.
	 *
	 * @param id
	 *            The id of the player to be updated.
	 * @param up
	 *            Whether or not a player has moved up.
	 * @param down
	 *            Whether or not a player has moved down.
	 * @param left
	 *            Whether or not a player has moved left.
	 * @param right
	 *            Whether or not a player has moved right.
	 * @param shooting
	 *            Whether or not a player has started shooting.
	 * @param mouseX
	 *            The new x coordinate of the user.
	 * @param mouseY
	 *            The new y coordinate of the user.
	 */
	public void updatePlayer(int id, int counterFrame, boolean up, boolean down, boolean left, boolean right, boolean shooting,
			double angle) {

		// double timeUpdating = 0;
		//
		// timeUpdating = System.nanoTime();

		UserPlayer playerToBeUpdated;
		playerToBeUpdated = (UserPlayer) getPlayerWithId(id);

		if (debug)
			System.out.println("angle: " + angle);

		// update everything
		playerToBeUpdated.setCounterFrame(counterFrame);
		playerToBeUpdated.setUp(up);
		playerToBeUpdated.setDown(down);
		playerToBeUpdated.setLeft(left);
		playerToBeUpdated.setRight(right);
		playerToBeUpdated.setShoot(shooting);
		playerToBeUpdated.updateRotation(angle);

		// System.out.println("timeUpdating a player in nanoSeconds: " + (
		// System.nanoTime() - timeUpdating));
	}

	/**
	 * Helper method to find the player with a specific id from the entire list
	 * of players in the game.
	 *
	 * @param id
	 *            The player's id.
	 */
	private EssentialPlayer getPlayerWithId(int id) {
		for (EssentialPlayer p : players) {
			if (p.getPlayerId() == id)
				return p;
		}
		return null;
	}

	/**
	 * Sets the game players.
	 *
	 * @param players
	 *            A list of all the players involved in the game.
	 */
	public void setPlayers(ArrayList<EssentialPlayer> players) {
		this.players = players;
	}

	/**
	 * Retrieve the players currently in the game.
	 *
	 * @return The list of players in the game.
	 */
	public ArrayList<EssentialPlayer> getPlayers() {
		return players;
	}

}
