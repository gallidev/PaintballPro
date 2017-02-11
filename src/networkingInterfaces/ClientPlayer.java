package networkingInterfaces;

import networkingClient.ClientReceiver;
import networkingClient.ClientSender;
import physics.GeneralPlayer;

/**
 * Client-sided version of a player, which stores the information required by each player to perform in the game.\
 * Developed for the integration of the game logic and physics.
 * @author Alexandra Paduraru
 *
 */
public class ClientPlayer {
	
	private GeneralPlayer player;
	
	/**
	 * Initializes a new Client Player, which will communicate to the server through a Client Sender and a Client Receiver. 
	 * The player can be either an AI or a user, which is going to be specified by 
	 * @param sender The client sender.
	 * @param receiver The client receiver.
	 * @param playerType It is either an instance of GeneralPlayer or AIPlayer, depending on the player's type and game behaviour.
	 */
	public ClientPlayer(ClientSender sender, ClientReceiver receiver, GeneralPlayer playerType){
		// Do stuff here.
		this.player = playerType;
	}
	
}