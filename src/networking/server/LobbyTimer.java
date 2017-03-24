package networking.server;

import logic.RoundTimer;
import networking.game.UDPServer;

/**
 * Class to simulate Lobby Timer.
 * @author Matthew Walters
 */
public class LobbyTimer extends Thread {
	
	public boolean m_running = true;
	
	private int gameMode;
	private int lobbyTime;
	private Lobby lobby;
	private ServerReceiver receiver;
	private UDPServer udpServer;
	
	/**
	 * Constructor for Lobby Timer.
	 * @param lobbyTime Time to run timer for.
	 * @param receiver Server Receiver to send messages.
	 * @param udpServer UDP Game server to send messages.
	 * @param gameMode Mode that the game is being played.
	 * @param lobby Lobby to run timer in.
	 */
	public LobbyTimer(int lobbyTime, ServerReceiver receiver, UDPServer udpServer, int gameMode, Lobby lobby) {
		this.lobbyTime = lobbyTime;
		this.receiver = receiver;
		this.udpServer = udpServer;
		this.gameMode = gameMode;
		this.lobby = lobby;
	}
	
	/**
	 * Main thread method to run timer.
	 */
	public void run() {
		RoundTimer timer = new RoundTimer(lobbyTime);
		timer.startTimer();
		long lastTime = -1;
		while (!timer.isTimeElapsed() && m_running) {
			try {
				if (lastTime != timer.getTimeLeft()) {
					lastTime = timer.getTimeLeft();
					receiver.sendToAll("LTime:" + timer.getTimeLeft());
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
		if(m_running) {
			lobby.playGame(receiver, udpServer, gameMode);
			lobby.startGameLoop(udpServer, gameMode);
		}
	}
}
