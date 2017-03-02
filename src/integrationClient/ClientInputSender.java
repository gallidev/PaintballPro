package integrationClient;

import networkingClient.ClientSender;
import physics.InputHandler;

/**
 * Sends user inputs(client-sided) to the server.
 * @author Alexandra Paduraru
 *
 */
public class ClientInputSender {

	private ClientSender toServer;
	private InputHandler handler;
	private int id;
	
	/**
	 * Initialises a new input sender.
	 * @param sender The client sender used by networking to send all information to the server.
	 * @param handler The handler which handles all user inputs in the player.
	 * @param playerId The current player's id.
	 */
	public ClientInputSender(ClientSender sender, InputHandler handler, int playerId){
		toServer = sender;
		this.handler = handler;
		id = playerId;
	}
	
	/**
	 * Checks to see if the player's position has changed. If so, it sends to the server the user action
	 * (player goes up/down, left/right, mouse position changes) through the protocol.
	 */
	public void sendServer(){
		//Protocol: "0:" + Up/Down/Left/Right/Shooting/Mouse + ":id", depending on the player's action
		
		String toBeSent = "0:";
		//did player move up?
		if (handler.isUp())
			toBeSent += "Up:" + id;
		
		//did player move down?
		if (handler.isDown())
			toBeSent += ":Down:" + id;
				
		//did player move left?
		if (handler.isLeft())
			toBeSent += ":Left:" + id;
				
		//did player move right?
		if (handler.isRight())
			toBeSent += ":Right:" + id;
				
		//is the player shooting?
		if (handler.isShoot())
			toBeSent += ":Shooting:" + id;
		
		//did the mouse move?
		toBeSent += ":Mouse:" + handler.getMouseX() + ":" + handler.getMouseY() + ":" + id;
			
		toServer.sendMessage(toBeSent);
	}
	
	
}
