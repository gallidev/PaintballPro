package integration.server;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import enums.TeamEnum;
import logic.server.Team;
import networking.game.UDPServer;
import physics.PowerupType;
import players.EssentialPlayer;

/**
 * Sends user inputs(client-sided) to the server.
 *
 * @author Alexandra Paduraru
 * @author Filippo Galli
 *
 */
public class ServerGameStateSender implements CollisionsHandlerListener {

	/* Dealing with sending the information */
	private static final long delayMilliseconds = 25;
	private int lobbyId;
	private UDPServer udpServer;
	private ArrayList<EssentialPlayer> players;
//	private int frames = 0;
	private ServerGameSimulation gameLoop;
	private ScheduledExecutorService scheduler;

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

		Executors.newSingleThreadScheduledExecutor(r ->
		{
		    Thread t = Executors.defaultThreadFactory().newThread(r);
		    t.setPriority(Thread.MIN_PRIORITY);
		    return t;
		});

		Runnable sender = () ->
		{
			sendClient();
			sendBullets();
			sendRemainingTime();

			//sendEliminatedPlayers();

			if(gameLoop.getGame().isGameFinished()){

				udpServer.sendToAll("5", lobbyId);
				sendWinner();
				//scheduler.shutdown();
			}
		};

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

			if(p.hasShot()){
				p.setHasShot(false);
				toBeSent += ":shot";
				udpServer.sendToAll(toBeSent, lobbyId);
			}

		}

	}

	/**
	 * Sends the clients the new location,angle and visibility of each player, according to the protocol.
	 * This method also sends the server if a player has captured the flag, according to the protocol message.
	 * Both of these things are done in a single method in order to increase efficiency, as it has to go through all players in
	 * the current game.
	 */
	private void sendClient() {
		//Protocol: "1:<id>:<x>:<y>:<angle>:<visiblity>:<eliminated>"

		for(EssentialPlayer p : players){
			String toBeSent = "1:" + p.getPlayerId();

			toBeSent += ":" + p.getLayoutX();
			toBeSent += ":" + p.getLayoutY();
			toBeSent += ":" + p.getAngle();
			toBeSent += ":" + p.isVisible();
			toBeSent += ":" + p.isEliminated();

			udpServer.sendToAll(toBeSent, lobbyId);

			if (p.getScoreChanged()){
				updateScore();
				p.setScoreChanged(false);
			}

			if (p.getShieldRemoved()){
				sendShieldRemoved(p);
				p.setShieldRemoved(false);
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
		//System.out.println("send updated score");
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


	private void sendShieldRemoved(EssentialPlayer p){
		udpServer.sendToAll("%:" + p.getPlayerId(), lobbyId);
//		udpServer.sendToAll("%:" + p.getPlayerId(), lobbyId);
//		udpServer.sendToAll("%:" + p.getPlayerId(), lobbyId);

	}

	/*
	 * Sets the game simulation.
	 * @param sim The simulation of the game, which runs on the server.
	 */
	public void setGameLoop(ServerGameSimulation sim){
		gameLoop = sim;
	}


	public void onFlagCaptured(int player)
	{
		String toBeSent = "8:" + player + ":";
		udpServer.sendToAll(toBeSent, lobbyId);
		updateScore();
	}

	public void onFlagDropped(int player)
	{
		String toBeSent = "7:" + + player + ":";
		udpServer.sendToAll(toBeSent, lobbyId);
		updateScore();
	}

	public void onFlagRespawned(int player)
	{
		String toBeSent = "!:" + player + ":";

		toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutX() + ":";
		toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutY();

		udpServer.sendToAll(toBeSent, lobbyId);
		updateScore();
	}

	public void onPowerupAction(PowerupType type, int player)
	{
		String toBeSent = "";
		switch(type)
		{
			case SHIELD: toBeSent = "$:0:" + player;
				break;
			case SPEED: toBeSent = "$:1:" + player;
				break;
		}
		udpServer.sendToAll(toBeSent, lobbyId);
	}

	public void onPowerupRespawn(PowerupType type, int location)
	{
		String toBeSent = "";
		switch(type)
		{
			case SHIELD:
				toBeSent += "P:0:";
				break;
			case SPEED:
				toBeSent += "P:1:";
				break;
		}
		toBeSent += location;
		udpServer.sendToAll(toBeSent, lobbyId);
	}
}
