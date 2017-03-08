package integrationClient;

import java.util.ArrayList;

import enums.TeamEnum;
import physics.Bullet;
import physics.GhostBullet;
import players.GhostPlayer;

/**
 * Client-sided class which receives an action imposed by the server on the
 * player and executes it.
 *
 * @author Alexandra Paduraru
 *
 */
public class ClientGameStateReceiver {

	private ArrayList<GhostPlayer> players;

	/**
	 * Initializes a new action receiver with a player which will be controlled
	 * by the actions received from the server.
	 *
	 * @param player
	 *            The player upon which the actions take place.
	 */
	public ClientGameStateReceiver(ArrayList<GhostPlayer> players) {
		this.players = players;
	}

	public void updatePlayer(int id, double x, double y, double angle, boolean visible){

		GhostPlayer playerToBeUpdated = getPlayerWithId(id);

		playerToBeUpdated.setSyncX(x);
		playerToBeUpdated.setSyncY(y);
		playerToBeUpdated.setRotationAngle(angle);
		playerToBeUpdated.setSyncVisible(visible);
	}

	/**
	 * Helper method to find the player with a specific id from the entire list of players in the game.
	 * @param id The player's id.
	 */
	private GhostPlayer getPlayerWithId(int id){
		for (GhostPlayer p : players){
			if (p.getPlayerId() == id)
				return p;
		}
		return null;
	}

	public void updateBullets(int id, String[] bullets){
		GhostPlayer p = getPlayerWithId(id);

		if (p != null) { // the player is not us

			ArrayList<GhostBullet> firedBullets = new ArrayList<>();
			for (int i = 0; i < bullets.length - 2; i = i + 3) {

				int bulletId = Integer.parseInt(bullets[i]);
				double x = Double.parseDouble(bullets[i+1]);
				double y = Double.parseDouble(bullets[i + 2]);

				firedBullets.add(new GhostBullet(bulletId, x, y, p.getTeam()));
			}
			p.getFiredBullets().clear();
			p.setFiredBullets(firedBullets);
		}
	}


}
