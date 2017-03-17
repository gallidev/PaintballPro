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
import serverLogic.CaptureTheFlagMode;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

/**
 * Sends user inputs(client-sided) to the server.
 *
 * @author Alexandra Paduraru
 * @author Filippo Galli
 *
 */
public class ServerGameStateSender {

	private int lobbyId;
	private UDPServer udpServer;
	private ArrayList<EssentialPlayer> players;
	private int frames = 0;
	private ServerGameSimulation gameLoop;
	private ScheduledExecutorService scheduler;

	/* Dealing with sending the information */
	private long delayMilliseconds = 17;

	/**
	 * Initialises a new Server game state sender with the server, players involved in the game and the id of the lobby
	 * corresponding to that game.
	 * @param udpServer The server used to send information to all clients involved in a game.
	 * @param players A list of players currently involved in a game.
	 * @param lobbyId The id of the lobby corresponding to the current game.
	 */
	public ServerGameStateSender(UDPServer udpServer, ArrayList<EssentialPlayer> players, int lobbyId){
		this.udpServer = udpServer;
		this.players = players;
		this.lobbyId = lobbyId;
	}

	/**
	 * Starts sending client inputs to the server at a rate of 30 frames per second.
	 */
	public void startSending(){

		scheduler = Executors.newScheduledThreadPool(1);
		Runnable sender = new Runnable() {
		       public void run() {
		    	   frames ++;
		    	   sendClient();
		    	   sendBullets();

		    	   sendHitWall();

		    	   sendRemainingTime();

		    	   if(gameLoop.getGame().isGameFinished()){

		    		   udpServer.sendToAll("5", lobbyId);
		    		   sendWinner();
		    		   //scheduler.shutdown();
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

	/**
	 * Stops the server from sending information when the game finishes.
	 */
	public void stopSending(){
		scheduler.shutdown();
	}


	/**
	 * Sends the remaining game time to clients.
	 */
	private void sendRemainingTime() {
		//Protocol: 6:<remaining seconds>
		if (gameLoop.getGame().getRemainingTime() != 0 ){
			String toBeSent = "6:" + gameLoop.getGame().getRemainingTime();

			udpServer.sendToAll(toBeSent, lobbyId);
		}

	}

	/**
	 * Send the active bullets of each player to the client, according to the protocol.
	 */
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

	/**
	 * Sends the clients the new location,angle and visibility of each player, according to the protocol.
	 * This method also sends the server if a player has captured the flag, according to the protocol message.
	 * Both of these things are done in a single method in order to increase efficiency, as it has to go through all players in
	 * the current game.
	 */
	private void sendClient() {
		//Protocol: "1:<id>:<x>:<y>:<angle>:<visiblity>"

		for(EssentialPlayer p : players){
			String toBeSent = "1:" + p.getPlayerId();

			toBeSent += ":" + p.getLayoutX();
			toBeSent += ":" + p.getLayoutY();
			toBeSent += ":" + p.getAngleDegrees();
			toBeSent += ":" + p.isVisible();

			udpServer.sendToAll(toBeSent, lobbyId);

//			if (gameLoop.getGame() instanceof CaptureTheFlagMode){
//				if (p.hasFlag()){
//					udpServer.sendToAll("7:" + p.getPlayerId(), lobbyId);
//				}
//			}

			if (p.getScoreChanged()){
				updateScore();
				p.setScoreChanged(false);
			}

			if (p.getCollisionsHandler().isFlagCaptured()){
				System.out.println("flag captured");
				sendFlagCaptured();
				updateScore();

				p.getCollisionsHandler().setFlagCaptured(false);
			}

			if (p.getCollisionsHandler().isFlagDropped()){
				updateScore();
				sendFlagLost();
				p.getCollisionsHandler().setFlagDropped(false);
			}

			if (p.getCollisionsHandler().isFlagRespawned()){
				updateScore();
				sendBaseFlag();
				p.getCollisionsHandler().setRespawned(false);
			}
		}
	}

	/**
	 * Sends the clients the new score of each team.
	 */
	public void updateScore(){
		//Protocol: "3:<redTeamScore>:<blueTeamScore>
		String toBeSent = "3:" +  gameLoop.getGame().getRedTeam().getScore() + ":" + gameLoop.getGame().getBlueTeam().getScore();

		udpServer.sendToAll(toBeSent, lobbyId);
		System.out.println("send updated score");
	}

	/**
	 * Sends the clients the game winner when the game finishes.
	 */
	public void sendWinner(){
		//Protocol: "2:<winner>:RedScore:BlueScore"
		Team winner = gameLoop.getGame().whoWon();
		if (winner != null){
			String toBeSent = "2:" + (winner.getColour() == TeamEnum.RED ? "Red" : "Blue")  + ":"  + gameLoop.getGame().getRedTeam().getScore() + ":" + gameLoop.getGame().getBlueTeam().getScore();
			udpServer.sendToAll(toBeSent, lobbyId);

			stopSending();
		}

	}

	private void sendFlagCaptured(){

			String toBeSent = "8:" + players.get(0).getCollisionsHandler().getPlayerWithFlagId() + ":";

//			toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutX() + ":";
//			toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutY() + ":";
//
//			toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().isVisible();

			udpServer.sendToAll(toBeSent, lobbyId);
//			udpServer.sendToAll(toBeSent, lobbyId);
//			udpServer.sendToAll(toBeSent, lobbyId);

		//}
	}

	private void sendFlagLost(){
		String toBeSent = "7:" + + players.get(0).getCollisionsHandler().getPlayerWithFlagId() + ":";

		udpServer.sendToAll(toBeSent, lobbyId);
		udpServer.sendToAll(toBeSent, lobbyId);
		udpServer.sendToAll(toBeSent, lobbyId);

	}

	private void sendBaseFlag(){
		String toBeSent = "!:" + players.get(0).getCollisionsHandler().getPlayerWithFlagId() + ":";

		toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutX() + ":";
		toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutY();

		udpServer.sendToAll(toBeSent, lobbyId);
		udpServer.sendToAll(toBeSent, lobbyId);
		udpServer.sendToAll(toBeSent, lobbyId);

	}

	public void sendHitWall(){

		if(players.get(0).getCollisionsHandler().isWallHit()){
			String toBeSent = "@:";

			toBeSent += players.get(0).getCollisionsHandler().getHitWallX() + ":";
			toBeSent += players.get(0).getCollisionsHandler().getHitWallY() + ":";
			toBeSent += (players.get(0).getCollisionsHandler().getSplashColour() == TeamEnum.RED ? "Red" : "Blue" ) + ":";

			players.get(0).getCollisionsHandler().setWallHit(false);

			udpServer.sendToAll(toBeSent, lobbyId);
			udpServer.sendToAll(toBeSent, lobbyId);
			udpServer.sendToAll(toBeSent, lobbyId);
		}

	}


	/*
	 * Sets the game simulation.
	 * @param sim The simulation of the game, which runs on the server.
	 */
	public void setGameLoop(ServerGameSimulation sim){
		gameLoop = sim;
	}


}
