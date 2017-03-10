package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import enums.TeamEnum;
import networking.game.UDPServer;
import physics.Bullet;
import players.EssentialPlayer;

/**
 * Sends user inputs(client-sided) to the server.
 * @author Alexandra Paduraru
 *
 */
public class ServerGameStateSender {

	private int lobbyId;
	private UDPServer udpServer;
	private ArrayList<EssentialPlayer> players;
	private int frames = 0;
	/* Dealing with sending the information */
	private long delayMilliseconds = 33;

	private ServerGameSimulation gameLoop;

	public ServerGameStateSender(UDPServer udpServer, ArrayList<EssentialPlayer> players, int lobbyId){
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
		    	   sendBullets();
		    	   updateScore();

		    	   sendRemainingTime();

		    	   if(gameLoop.getGame().isGameFinished()){

		    		   udpServer.sendToAll("5", lobbyId);
		    		   sendWinner();
		    	   }
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

	private void sendRemainingTime() {
		//Protocol: 6:<remaining seconds>
		String toBeSent = "6:" + gameLoop.getGame().getRemainingTime();

		udpServer.sendToAll(toBeSent, lobbyId);
	}

	protected void sendBullets() {
		// Protocol: "4:<id>:<bulletX>:<bulletY>:<angle>:...

		for(EssentialPlayer p : players){

				String toBeSent = "4:" + p.getPlayerId();

				boolean haveBullets = false;
				for(Bullet bullet : p.getBullets())
				{
					if(bullet.isActive())
					{
						haveBullets = true;
						toBeSent += ":" + bullet.getBulletId() + ":" + bullet.getX() + ":" + bullet.getY() ;
					}
				}
				//System.out.println("Bullet msg sent from server " + toBeSent);
				if (haveBullets)
					udpServer.sendToAll(toBeSent, lobbyId);
		}
	}

	private void sendClient() {
		//Protocol: "1:<id>:<x>:<y>:<angle>:<visiblity>"

		for(EssentialPlayer p : players){
			String toBeSent = "1:" + p.getPlayerId();

			toBeSent += ":" + p.getLayoutX();
			toBeSent += ":" + p.getLayoutY();
			toBeSent += ":" + p.getAngleDegrees();
			toBeSent += ":" + p.isVisible();

			udpServer.sendToAll(toBeSent, lobbyId);
		}

	}

	public void updateScore(){
		//Protocol: "3:<redTeamScore>:<blueTeamScore>
		String toBeSent = "3:" +  gameLoop.getGame().getRedTeam().getScore() + ":" + gameLoop.getGame().getBlueTeam().getScore();

		udpServer.sendToAll(toBeSent, lobbyId);
	}

	public void sendWinner(){
		//Protocol: "2:<winner>"
		String toBeSent = "2:" + (gameLoop.getGame().whoWon().getColour() == TeamEnum.RED ? "Red" : "Blue") ;

		udpServer.sendToAll(toBeSent, lobbyId);
	}

	public void setGameLoop(ServerGameSimulation sim){
		gameLoop = sim;
	}


}
