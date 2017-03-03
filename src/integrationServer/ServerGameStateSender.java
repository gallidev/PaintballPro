package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import networking.server.ServerReceiver;
import networking.server.ServerSender;
import players.ServerMinimumPlayer;

/**
 * Sends user inputs(client-sided) to the server.
 * @author Alexandra Paduraru
 *
 */
public class ServerGameStateSender {

	private ServerReceiver toClient;
	private ArrayList<ServerMinimumPlayer> players;
	int frames = 0;
	/* Dealing with sending the information */
	private long delayMilliseconds = 33;

	public ServerGameStateSender(ServerReceiver toClient, ArrayList<ServerMinimumPlayer> players){
		this.toClient = toClient;
		this.players = players;
	}

	public void startSending(){

		ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		Runnable sender = new Runnable() {
		       public void run() {
		    	   frames ++;
		    	   sendClient();

		       }
		     };

		ScheduledFuture<?> senderHandler =
				scheduler.scheduleAtFixedRate(sender, 0, delayMilliseconds, TimeUnit.MILLISECONDS);

		//for testing purposes:

//		Runnable frameCounter = new Runnable() {
//		       public void run() {
//		    	   System.out.println("server Sending frames " + frames);
//		    	   frames = 0;
//
//		       }
//		     };
//
//		ScheduledFuture<?> frameCounterHandle =
//				scheduler.scheduleAtFixedRate(frameCounter, 0, 1, TimeUnit.SECONDS);

	}

	private void sendClient() {
		//Protocol: "1:<id>:<x>:<y>:<angle>:<visiblity>"
		
		for(ServerMinimumPlayer p : players){
			String toBeSent = "1:" + p.getPlayerId();
			
			toBeSent += ":" + p.getX();
			toBeSent += ":" + p.getY();
			toBeSent += ":" + p.getAngle();
			toBeSent += ":" + p.isVisible();
			
			toClient.sendToAll(toBeSent);
		}

	}



}
