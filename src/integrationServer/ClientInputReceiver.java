package integrationServer;

import networkingClient.ClientSender;

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
public class ClientInputReceiver {

	private ClientSender toClients;

	/**
	 * Initializes a new input receiver with a server receiver which will
	 * receive information from clients and an client sender, which will send
	 * the clients the action that they will need to perform after the server
	 * computes it.
	 * 
	 * @param toClients The client sender which will send clients what they should do.
	 */
	public ClientInputReceiver(ClientSender toClients) {
		super();
		this.toClients = toClients;
	}
	
	public void moveUpAction(){
		//do sth here to update player location
		//collision handling
		//return an action that has to be sent to the client(through ServerActionSender)
	}
	
	public void moveDownAction(){
		//do sth here to update player location
		//collision handling
		//return an action that has to be sent to the client(through ServerActionSender)
	}
	
	public void moveLeftAction(){
		//do sth here to update player location
		//collision handling
		//return an action that has to be sent to the client(through ServerActionSender)
	}
	
	public void moveRightAction(){
		//do sth here to update player location
		//collision handling
		//return an action that has to be sent to the client(through ServerActionSender)
	}
	
	public void mouseMovedAction(){
		//do sth here to update player location
		//collision handling
		//return an action that has to be sent to the client(through ServerActionSender)
	}

}
