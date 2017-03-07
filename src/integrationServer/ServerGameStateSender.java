package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import networking.game.UDPServer;
import networking.server.ServerReceiver;
import networking.server.ServerSender;
import players.ServerMinimumPlayer;

/**
 * Sends user inputs(client-sided) to the server.
 * @author Alexandra Paduraru
 *
 */
public class ServerGameStateSender {

	private int lobbyId;
	private UDPServer udpServer;
	private ArrayList<ServerMinimumPlayer> players;
	private int frames = 0;
	/* Dealing with sending the information */
	private long delayMilliseconds = 33;
	
	private ServerGameSimulation gameLoop;

	public ServerGameStateSender(UDPServer udpServer, ArrayList<ServerMinimumPlayer> players, int lobbyId){
		this.udpServer = udpServer;
		this.players = players;
		this.lobbyId = lobbyId;
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

		
		//sending just twice per second for less important actions
		Runnable rareSender = new Runnable() {
		       public void run() {
		    	   frames ++;
		    	   updateScore();
		       }
		     };

		ScheduledFuture<?> rareSenderHandler =
				scheduler.scheduleAtFixedRate(sender, 0, 50, TimeUnit.MILLISECONDS);
		
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

			udpServer.sendToAll(toBeSent, lobbyId);
		}

	}

	public void updateScore(){
		//Protocol: "3:<redTeamScore>:<blueTeamScore>
		String toBeSent = "3:" +  gameLoop.getGame().getFirstTeam().getScore() + ":" + gameLoop.getGame().getSecondTeam().getScore();
			
		udpServer.sendToAll(toBeSent, lobbyId);
	}
	
	public void setGameLoop(ServerGameSimulation sim){
		gameLoop = sim;
	}


}
