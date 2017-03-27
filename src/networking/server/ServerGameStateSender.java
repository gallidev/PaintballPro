package networking.server;

import enums.PowerupType;
import enums.TeamEnum;
import logic.server.Team;
import networking.game.GameUpdateListener;
import networking.game.ServerGameSimulation;
import networking.game.UDPServer;
import players.EssentialPlayer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Sends the Game State packets to the relevant clients and the updates
 *
 * @author Alexandra Paduraru
 * @author Filippo Galli
 *
 */
public class ServerGameStateSender implements GameUpdateListener
{

	/* Dealing with sending the information */
	private static final long DELAY_MILLISECONDS = 25;

	private ServerGameSimulation gameLoop;
	private int lobbyId;
	private ArrayList<EssentialPlayer> players;
	private UDPServer udpServer;
	private ScheduledExecutorService scheduler;

	/**
	 * Initialises a new Server game state sender with the server, players
	 * involved in the game and the id of the lobby corresponding to that game.
	 *
	 * @param udpServer
	 *            The server used to send information to all clients involved in
	 *            a game.
	 * @param players
	 *            A list of players currently involved in a game.
	 * @param lobbyId
	 *            The id of the lobby corresponding to the current game.
	 */
	public ServerGameStateSender(UDPServer udpServer, ArrayList<EssentialPlayer> players, int lobbyId) {
		this.udpServer = udpServer;
		this.players = players;
		this.lobbyId = lobbyId;
		udpServer.activePlayers = true;
	}

	/**
	 * Starts sending client inputs to the server at a rate of 30 frames per
	 * second.
	 */
	public void startSending() {

		scheduler = Executors.newScheduledThreadPool(1);

		Runnable sender = () -> {
			sendClient();

			if (gameLoop.getGame().isGameFinished()) {

				sendWinner();
				// scheduler.shutdown();
			}

			if (!udpServer.activePlayers){
				//scheduler.shutdown();
				gameLoop.stopGameLoop();
			}
		};

		scheduler.scheduleAtFixedRate(sender, 0, DELAY_MILLISECONDS, TimeUnit.MILLISECONDS);

		//Sending the remaining time every second
		Runnable timeSender = new Runnable() { public void run() {
			sendRemainingTime();
		} };

		scheduler.scheduleAtFixedRate(timeSender, 0, 1, TimeUnit.SECONDS);

	}

	/**
	 * Stops the server from sending information when the game finishes.
	 */
	public void stopSending() {
		scheduler.shutdown();
	}

	/**
	 * Sends the remaining game time to clients.
	 */
	private void sendRemainingTime() {
		// Protocol: 6:<remaining seconds>
		if (gameLoop.getGame().getRemainingTime() != 0) {
			String toBeSent;
			toBeSent = "6:" + gameLoop.getGame().getRemainingTime();
			udpServer.sendToAll(toBeSent, lobbyId);
		}

	}

	/**
	 * A player has shot and generate a bullet
	 *
	 * @param playerId
	 *            The id of the player that has shot.
	 * @param bulletId
	 *            The id of the bullet that has been shot.
	 * @param originX
	 *            The origin coordinate x of the bullet
	 * @param originY
	 *            The origin coordinate y of the bullet
	 * @param angle
	 *            The angle of the bullet is moving towards
	 */
	public void onShotBullet(int playerId, int bulletId, double originX, double originY, double angle) {
		// Protocol: "4:<id>:<bulletX>:<bulletY>:<angle>:...

		String toBeSent;
		toBeSent = "4:" + playerId;
		toBeSent += ":" + bulletId;
		toBeSent += ":" + originX;
		toBeSent += ":" + originY;
		toBeSent += ":" + angle;

		udpServer.sendToAll(toBeSent, lobbyId);
	}

	/**
	 * Kill a player when ia bullett has touched a player.
	 *
	 * @param playerId
	 *            The id of the player to be killed.
	 * @param bulletId
	 *            The id of the bullet that be killed.
	 */
	@Override
	public void onBulletKills(int playerId, int bulletId) {
		String toBeSent = "5:" + playerId;
		toBeSent += ":" + bulletId;

		udpServer.sendToAll(toBeSent, lobbyId);

	}

	/**
	 * Sends the clients the new location,angle and visibility of each player,
	 * according to the protocol. This method also sends the server if a player
	 * has captured the flag, according to the protocol message. Both of these
	 * things are done in a single method in order to increase efficiency, as it
	 * has to go through all players in the current game.
	 */
	private void sendClient() {
		// Protocol: "1:<id>:<counterFrame>:<x>:<y>:<angle>:<visiblity>:<eliminated>"

		for (EssentialPlayer p : players) {

			String toBeSent;
			toBeSent = "1:" + p.getPlayerId();
			toBeSent += ":" + p.getCounterFrame();
			toBeSent += ":" + p.getLayoutX();
			toBeSent += ":" + p.getLayoutY();
			toBeSent += ":" + p.getAngle();
			toBeSent += ":" + p.isVisible();
			toBeSent += ":" + p.isEliminated();

			udpServer.sendToAll(toBeSent, lobbyId);

			if (p.getScoreChanged()) {
				updateScore();
				p.setScoreChanged(false);
			}

			if (p.getShieldPopped()) {
				sendShieldRemoved(p.getPlayerId());
				p.setShieldPopped(false);
			}
		}
	}

	/**
	 * Sends the clients the new score of each team.
	 */
	public void updateScore() {
		// Protocol: "3:<redTeamScore>:<blueTeamScore>
		String toBeSent;
		toBeSent = "3:" + gameLoop.getGame().getRedTeam().getScore() + ":"
				+ gameLoop.getGame().getBlueTeam().getScore();

		udpServer.sendToAll(toBeSent, lobbyId);
		// System.out.println("send updated score");
	}

	/**
	 * Sends the clients the game winner when the game finishes.
	 */
	public void sendWinner() {
		// Protocol: "2:<winner>:RedScore:BlueScore"
		Team winner = gameLoop.getGame().whoWon();
		if (winner != null) {
			String toBeSent;
			toBeSent = "2:" + (winner.getColour() == TeamEnum.RED ? "Red" : "Blue") + ":"
					+ gameLoop.getGame().getRedTeam().getScore() + ":" + gameLoop.getGame().getBlueTeam().getScore();
			udpServer.sendToAll(toBeSent, lobbyId);

			stopSending();
		}

	}

	/**
	 * Send clients when a player does not have the shield power-up anymore.
	 *
	 * @param p
	 *            The player which has lost the shield power-up.
	 */
	public void sendShieldRemoved(int playerId) {
		udpServer.sendToAll("%:" + playerId, lobbyId);
		// udpServer.sendToAll("%:" + p.getPlayerId(), lobbyId);
		// udpServer.sendToAll("%:" + p.getPlayerId(), lobbyId);

	}

	/**
	 * Sends clients that a player has captured the flag.
	 *
	 * @param id
	 *            The id of the player which has captured the flag.
	 */
	public void onFlagCaptured(int player) {
		String toBeSent;
		toBeSent = "8:" + player + ":";
		udpServer.sendToAll(toBeSent, lobbyId);
		updateScore();
	}

	/**
	 * Sends clients that a player has dropped the flag.
	 *
	 * @param id
	 *            The id of the player which has dropped the flag.
	 */
	public void onFlagDropped(int player) {
		String toBeSent;
		toBeSent = "7:" + +player + ":";
		udpServer.sendToAll(toBeSent, lobbyId);
		updateScore();
	}

	/**
	 * Sends clients that a player has brought the flag back to its base.
	 *
	 * @param id
	 *            The id of the player.
	 */
	public void onFlagRespawned(int player) {
		String toBeSent;
		toBeSent = "!:" + player + ":";

		toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutX()
				+ ":";
		toBeSent += gameLoop.getGame().getRedTeam().getMembers().get(0).getCollisionsHandler().getFlag().getLayoutY();

		udpServer.sendToAll(toBeSent, lobbyId);
		updateScore();
	}

	public void onPowerupAction(PowerupType type, int player) {
		String toBeSent = "";
		switch (type) {
		case SHIELD:
			toBeSent = "$:0:" + player;
			break;
		case SPEED:
			toBeSent = "$:1:" + player;
			break;
		default :
			break;
		}
		udpServer.sendToAll(toBeSent, lobbyId);
	}

	public void onPowerupRespawn(PowerupType type, int location) {
		String toBeSent = "";
		switch (type) {
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

	/*
	 * Sets the game simulation.
	 *
	 * @param sim The simulation of the game, which runs on the server.
	 */
	public void setGameLoop(ServerGameSimulation sim) {
		gameLoop = sim;
	}

}
