package networkingInterfaces;

import networkingClient.Client;
import networkingClient.ClientReceiver;
import networkingClient.ClientSender;

// We start the client here from the GUI. 
public class GUIIntegration {

	public static void main(String[] args) {
		
		String nickname = ""; // We ask the user what their nickname is.
		int portNumber = 0; // The server is on a particular port.
		String machName = ""; // The machine has a particular name.
		
		// This loads up the client code.
		Client client = new Client(nickname,portNumber,machName);
		
		// We can then get the client sender and receiver threads.
		ClientSender sender = client.getSender();
		ClientReceiver receiver = client.getReceiver();
		
		/*
		 * We can use the sender class to send text following the protocols
		 * to the Server, by calling sender.sendmessage(text), where text
		 * is the message we wish to send. GUI buttons can therefore call this
		 * following the set up protocol and sending the appropriate text.
		 * For example, when we press the Switch Team button on the Lobby screen
		 * we run sender.sendmessage("SwitchTeam") and our team will switch.
		 * We must then remember to ask for team status' again by running
		 * server.sendmessage(Get:Blue) and server.sendmessage(Get:Red)
		 * and parsing the result to get team member's nicknames for display.
		 * All responses to our requests are sent to the ClientReceiver.
		 */
		
		/* 
		 * We can use the receiver class to match for things sent by the server
		 * and update things/act accordingly by running particular functions.
		 */
	}

}
