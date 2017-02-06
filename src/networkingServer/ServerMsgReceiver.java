package networkingServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;


// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

/**
 * Class to get messages from client, process and put appropriate message for a client.
 */
public class ServerMsgReceiver extends Thread {
	
	private int myClientsID;
	private BufferedReader myClient;
	private ClientTable clientTable;
	private ServerMsgSender sender;
	private LobbyTable gameLobby;
	
	/**
	 * Construct the class, setting passed variables to local objects.
	 * @param clientID The ID of the client.
	 * @param reader Input stream reader for data.
	 * @param table Table storing client information.
	 * @param sender Sender class for sending messages to the client.
	 */
	public ServerMsgReceiver(int clientID, BufferedReader reader, ClientTable table, ServerMsgSender sender, LobbyTable passedGameLobby) {
		myClientsID = clientID;
		myClient = reader;
		clientTable = table;
		this.sender = sender;
		gameLobby = passedGameLobby;
	}
	
	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	public void run() {
		try {
			while (true) {
				//Get input from the client read stream.
				String text = myClient.readLine();
				
				//If text isnt null and does not read "Exit:Client" do...
				if(text != null && text.compareTo("Exit:Client") != 0){
					
					if(text.contains("Play:Mode:"))
					{
						int gameMode = Integer.parseInt(text.substring(10));
						gameLobby.addPlayerToLobby(clientTable.getPlayer(myClientsID), gameMode);
						//
					}
					//If the client wants the currently connected client nicknames.
					if(text.contains("Retrieve Lobby List"))
					{
						getLobbyList();
					}
					//If the client wants the currently connected client scores.
					if(text.contains("Retrieve Scores"))
					{
						getScores();
					}
					//If the client wants to play a game with a specific client.
					if(text.contains("Play With:"))
					{				
						int recipient = Integer.parseInt(text.substring(10));
						playWith(recipient);				
					}
					//If the client wants to respond to a game request from a client.
					if(text.contains("Game:"))
					{
						String[] values = text.split(":", 3);
						gameResponse(values);
					}
					//When the client makes a game move against the other client.
					if(text.contains("Move:"))
					{
						makeMove(text);
					}
					//When there is a winner of the game.
					if(text.contains("Winner:"))
					{
						clientTable.wonGame(Integer.parseInt(text.substring(7)));
					}
					//If one client exits the game.
					if(text.contains("EXIT:GAME:"))
					{
						exitGame(text);
					}
				}
				else 
				{
					//Send exit message to the client.
					Message msg = new Message(text);
					MessageQueue recipientsQueue = clientTable.getQueue(myClientsID);
					recipientsQueue.offer(msg);
					
					//Remove client from client table data as they are exiting the system.
					clientTable.removeClient(myClientsID);
					
					//Close client stream.
					myClient.close();
					
					//Close server sender thread from the client.
					sender.stopThread();
					
					return;
				}
			}
		}
		catch (IOException e) {
			//If there is something wrong... send exit message to client
			Message msg = new Message("Exit:Client");
			MessageQueue recipientsQueue = clientTable.getQueue(myClientsID);
			recipientsQueue.offer(msg);
			
			//Remove client from table.
			clientTable.removeClient(myClientsID);
			
			//stop the Server sender thread.
			sender.stopThread();
			
			return;
		}
	}
	
	/**
	 * Functionality for exiting a game with a client.
	 * @param text Exit string to pass to client.
	 */
	private void exitGame(String text)
	{
		//Send passed text to the client which will make it stop the game.
		Message msg = new Message(text);
		MessageQueue recipientsQueue = clientTable.getQueue(Integer.parseInt(text.substring(10)));
		//If client exits
		if(recipientsQueue != null)
		{
			recipientsQueue.offer(msg);
		}
		else
			System.err.println("Message for unexistent client " 
					+ text.substring(10));
		//Set the players as no longer in game.
		clientTable.unsetGame(myClientsID, Integer.parseInt(text.substring(10)));
	}
	
	/**
	 * Functionality for making a move against an opponent client.
	 * @param text Move string to pass to opponent client.
	 */
	private void makeMove(String text)
	{
		//Split the move text received.
		String[] values = text.split(":");
		Message msg = new Message(text);
		MessageQueue recipientsQueue = clientTable.getQueue(Integer.parseInt(values[1])); //Values[1] is the opponent client to send moves to.
		if(recipientsQueue != null)
		{
			recipientsQueue.offer(msg);
		}
		else
			System.err.println("Message for unexistent client " 
					+ values[1]);
	}
	
	/**
	 * Functionality to get the scores of current client(s).
	 */
	private synchronized void getScores()
	{
		ArrayList<String> nicknames; //ArrayList of connected client's nicknames.
		//Get nicknames.
		nicknames = clientTable.getNicknames(); 
		
		ArrayList<Integer> scores; //ArrayList of connected client's scores.
		//Get scores.
		scores = clientTable.getScores();
		
		//Send a message to acknowledge start of score sending.
		Message msg = new Message("Start: Scores");
		MessageQueue recipientsQueue = clientTable.getQueue(myClientsID);
		recipientsQueue.offer(msg);
		
		//Send the score data, cycling through each piece of data.
		for(int i = 0; i < nicknames.size(); i++)
		{
			msg = new Message((nicknames.get(i)) + ":" + Integer.toString(scores.get(i)));
			recipientsQueue.offer(msg);
		}
		//Send a message to acknowledge end of score sending.
		msg = new Message("End: Scores");
		recipientsQueue.offer(msg);
	}
	
	/**
	 * Functionality to get the list of current client(s) connected to the server.
	 */
	private synchronized void getLobbyList()
	{
		ArrayList<String> nicknames;
		nicknames = clientTable.getNicknames();
		
		//Send a message to acknowledge start of connected client information sending.
		Message msg = new Message("Start: Lobby");
		MessageQueue recipientsQueue = clientTable.getQueue(myClientsID);
		recipientsQueue.offer(msg);
		
		//Cycle through each piece of data
		for(int i = 0; i < nicknames.size(); i++)
		{
			//Split by : to get id of each client.
			String values[] = nicknames.get(i).split(":");
			int id = Integer.parseInt(values[0]);
			//Construct message with nickname (including id) and their game status.
			msg = new Message(nicknames.get(i) + ":" + Boolean.toString(clientTable.gameStatus(id)));
			recipientsQueue.offer(msg);
		}
		//Send a message to acknowledge end of connected client information sending.
		msg = new Message("End: Lobby");
		recipientsQueue.offer(msg);
	}
	
	/**
	 * Functionality to request to play a game of tic-tac-toe with another client.
	 * @param recipient ID of the client to request a game with.
	 */
	private synchronized void playWith(int recipient)
	{
		//Build request message to play a game with a client.
		Message msg = new Message("Request:" + myClientsID + ":" + "Play a game with " + clientTable.getNickname(myClientsID) +"?");
		MessageQueue recipientsQueue = clientTable.getQueue(recipient);
		if (recipientsQueue != null)
		{
			//If the client is currently not in a game.
			if(!clientTable.gameStatus(recipient))
			{
				//Send the message to the recipient.
				recipientsQueue.offer(msg);
				
				//Change recipient queue to the client using this thread - the one that sent request.
				recipientsQueue = clientTable.getQueue(myClientsID);
				
				//Send message to acknowledge start of PlayWith response data.
				msg = new Message("Start: PlayWith");
				recipientsQueue.offer(msg);	
				
				//Acknowledge that request was sent to the other client.
				msg = new Message("Request Sent to " + clientTable.getNickname(recipient));
				recipientsQueue.offer(msg);	
				
				//Send message to acknowledge end of PlayWith response data.
				msg = new Message("End: PlayWith");
				recipientsQueue.offer(msg);
			}
			else
			{
				recipientsQueue = clientTable.getQueue(myClientsID);
				
				//Send message to acknowledge start of PlayWith response data.
				msg = new Message("Start: PlayWith");
				recipientsQueue.offer(msg);
				
				//Respond that the client that the client wanted to play with is currently in a game.
				msg = new Message("Sorry, " + clientTable.getNickname(recipient) + " is currently in game.");
				recipientsQueue.offer(msg);
				
				//Send message to acknowledge end of PlayWith response data.
				msg = new Message("End: PlayWith");
				recipientsQueue.offer(msg);
			}
		}
		else
			System.err.println("Message for unexistent client " 
					+ recipient);		
	}
	
	/**
	 * Functionality to start a game between two clients.
	 * @param values Response values containing useful information such as opponent id.
	 */
	public void gameResponse(String[] values)
	{
		//Turn passed recipient id value into an int.
		int recipID = Integer.parseInt(values[1]);
		
		//If client responded to game request with a yes...
		if((values[2].toLowerCase()).contains("yes"))
		{
			//If both clients aren't in a game currently...
			if(!clientTable.gameStatus(recipID) && !clientTable.gameStatus(myClientsID))
			{
				//player 1 is initiating client
				//player 2 is accepting client
				//Set game between clients.
				clientTable.setGame(recipID,myClientsID);
				
				//Send start game message to the opponent (the other client).
				Message msg = new Message("Start Game:"+myClientsID+":"+clientTable.getNickname(myClientsID));
				MessageQueue recipientsQueue = clientTable.getQueue(recipID);
				if(recipientsQueue != null)
				{
					recipientsQueue.offer(msg);
					//System.out.println("Send message"+recipID);
				}
				else
					System.err.println("Message for unexistent client " 
							+ values[1]);
				
				//Send start game message to this client.
				msg = new Message("Start Game:"+recipID+":"+clientTable.getNickname(recipID));
				recipientsQueue = clientTable.getQueue(myClientsID);
				if(recipientsQueue != null)
				{
					recipientsQueue.offer(msg);
				}
				else
					System.err.println("Message for unexistent client " 
							+ values[1]);
			}
			//If this client is currently in a game...
			else if(clientTable.gameStatus(myClientsID))
			{
				//Send response to other client that a game cannot be started.
				Message msg = new Message("Response: Cannot start game as currently in game.");
				MessageQueue recipientsQueue = clientTable.getQueue(recipID);
				if(recipientsQueue != null)
				{
					recipientsQueue.offer(msg);
				}
				else
					System.err.println("Message for unexistent client " 
							+ values[1]);
			}
			//If the other client is currently in a game...
			else if(clientTable.gameStatus(recipID))
			{
				//Send response to this client that a game cannot be started.
				Message msg = new Message("Response: Cannot start game as currently in game.");
			    MessageQueue recipientsQueue = clientTable.getQueue(myClientsID);
				if(recipientsQueue != null)
				{
					recipientsQueue.offer(msg);
				}
				else
					System.err.println("Message for unexistent client " 
							+ values[1]);
			}
		}
		else
		{
			//Send a response to the other client that this client declined the game request.
			Message msg = new Message("Response:" + clientTable.getNickname(myClientsID) + " declined your request.");
			MessageQueue recipientsQueue = clientTable.getQueue(recipID);
			if(recipientsQueue != null)
			{
				recipientsQueue.offer(msg);
			}
			else
				System.err.println("Message for unexistent client " 
						+ values[1]);
		}
	}
}
