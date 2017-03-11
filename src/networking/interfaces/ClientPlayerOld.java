//package networking.interfaces;
//
//import networking.client.ClientReceiver;
//import networking.client.ClientSender;
//import networking.shared.MessageQueue;
//import oldCode.players.GeneralPlayer;
//
///**
// * Client-sided version of a player, which stores the information required by each player to perform in the game.\
// * Developed for the integration of the game logic and physics.
// * @author Alexandra Paduraru
// *
// */
//public class ClientPlayerOld {
//
//	private GeneralPlayer player;
//	private ClientSender sender;
//
//	/**
//	 * Initializes a new Client Player, which will communicate to the server through a Client Sender and a Client Receiver.
//	 * The player can be either an AI or a user, which is going to be specified by
//	 * @param sender The client sender.
//	 * @param receiver The client receiver.
//	 * @param playerType It is either an instance of GeneralPlayer or AIPlayer, depending on the player's type and game behaviour.
//	 */
//	public ClientPlayerOld(ClientSender sender, ClientReceiver receiver, GeneralPlayer playerType){
//		// Do stuff here.
//		this.player = playerType;
//		this.sender = sender;
//	}
//
//	/**
//	 * Get the client sender thread object.
//	 * @return CLient sender thread.
//	 */
//	public ClientSender getSender(){
//		return sender;
//	}
//
//}