package integrationServer;

import java.util.ArrayList;

import networking.server.ServerSender;
import players.ServerMinimumPlayer;

/**
 * Receives input information (server-side) from all clients and updates the
 * server game information accordingly.
 *
 * The class uses a ServerReceiver in order to receive the inputs from clients.
 * Then it performs the computation required server-side(collision-handling, etc)
 * It uses after a ClientSender to send the clients the action they need to perform.
 * @author Alexandra Paduraru
 *
 */
public class ServerInputReceiver {

	private ArrayList<ServerMinimumPlayer> players;
	/**
	 * Initializes a new input receiver with a server receiver which will
	 * receive information from clients and an client sender, which will send
	 * the clients the action that they will need to perform after the server
	 * computes it.
	 *
	 * @param toClients The client sender which will send clients what they should do.
	 */
	public ServerInputReceiver() {
		super();
	}


	public ServerInputReceiver(ArrayList<ServerMinimumPlayer> players) {
		super();
		this.players = players;
	}


	public void updatePlayer(int id, boolean up, boolean down, boolean left, boolean right, boolean shooting, double mouseX,
			double mouseY) {

		ServerMinimumPlayer playerToBeUpdated = getPlayerWithId(id);

		//update everything
		playerToBeUpdated.setUp(up);
		playerToBeUpdated.setDown(down);
		playerToBeUpdated.setLeft(left);
		playerToBeUpdated.setRight(right);
		playerToBeUpdated.setShoot(shooting);
		playerToBeUpdated.setMouseX(mouseX);
		playerToBeUpdated.setMouseY(mouseY);
	}

	/**
	 * Helper method to find the player with a specific id from the entire list of players in the game.
	 * @param id The player's id.
	 */
	private ServerMinimumPlayer getPlayerWithId(int id){
		for (ServerMinimumPlayer p : players){
			if (p.getPlayerId() == id)
				return p;
		}
		return null;
	}


	public void setPlayers(ArrayList<ServerMinimumPlayer> players){
		this.players = players;
	}

}
