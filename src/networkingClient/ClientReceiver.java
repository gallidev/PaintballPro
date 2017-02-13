package networkingClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import enums.TeamEnum;
import gui.GUIManager;
import javafx.application.Platform;
import networkingInterfaces.ClientPlayerOld;
import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;
import physics.ClientPlayer;


// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

/**
 * Class to get messages from client, process and put appropriate message for a client.
 */
public class ClientReceiver extends Thread {

	private int clientID;
	private BufferedReader fromServer;
	private ClientSender sender;
	private MessageQueue myMsgQueue;
	private Message msg;
	private GUIManager m;
	private ClientPlayer cPlayer;
	
	/**
	 * Construct the class, setting passed variables to local objects.
	 * @param Cid The ID of the client.
	 * @param reader Input stream reader for data.
	 * @param sender Sender class for sending messages to the client.
	 */
	public ClientReceiver(int Cid, BufferedReader reader, ClientSender sender, MessageQueue msgQueue, GUIManager m)
	{
		this.m = m;
		clientID = Cid;
		fromServer = reader;
		this.sender = sender;
		myMsgQueue = msgQueue;
	}

	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	public void run() {
		try {
			while (true) {
				//Get input from the client read stream.
				String text = fromServer.readLine();

				//If text isn't null and does not read "Exit:Client" do...
				if(text != null && text.compareTo("Exit:Client") != 0){

					//System.out.println("Received: " + text);

					// Protocols
					//In-game messages
					//Temporary sketch of sending player positions
//					if (text.contains("Move")){
//						String[] msg = text.split(":");
//						int id = Integer.parseInt(msg[2]);
//						double x = Double.parseDouble(msg[3]);
//						double y = Double.parseDouble(msg[4]);
//						double angle = Double.parseDouble(msg[5]);
//					}
					if(text.contains("Ret:Red:"))
					{
						System.out.println("Got red");
						String[] red = text.substring(8).split("-");
						for (String r: red) {
							System.out.println("Got red:" + r);
						}
						m.updateRedLobby(red);
					}
					else if(text.contains("Ret:Blue:"))
					{
						System.out.println("Got blue");
						String[] blue = text.substring(9).split("-");
						for (String r: blue) {
							System.out.println("Got blue:" + r);
						}
						m.updateBlueLobby(blue);
					}
					else if(text.contains("Ret:Username:"))
					{

					}
					else if(text.contains("LTime:")){
						//m.setTimerStarted();
						String remTime = text.split(":")[1];
						int time = Integer.parseInt(remTime);
						m.setTimeLeft(time);
						m.setTimerStarted();
						System.out.println("Lobby has " + time + " left");
					}
					else if(text.contains("StartGame"))
					{
						
						
						cPlayer = new ClientPlayer(0, 0, clientID, TeamEnum.RED, this); // Using 'this' is ugly code but currently can't think of another way.
						
						//for debugging
						System.out.println("Received start signal!");
						System.out.println("game gas started for player with ID " + clientID);

						//Do stuff here: show the game window, so that the players can start the game
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								m.transitionTo("Elimination", null);
							}
						});


					}
					else if(text.contains("EndGame"))
					{
						System.out.println("Game has ended for player with ID " + clientID);
						// Get data about scores, and pass into transition method
						int someScore = 0;
						m.transitionTo("EndGame", someScore);
					}
					else if(text.contains("TimerStart"))
					{
						System.out.println("Timer Started");
						// Do stuff here, we have 10 secs till game start message sent.
						m.setTimerStarted();
					}
				}
				else // if the client wants to exit the system.
				{
					sender.stopThread();
					return;
				}
			}
		}
		catch (IOException e) {
			//If there is something wrong... exit cleanly.
			sender.stopThread();
			return;
		}
	}
}
