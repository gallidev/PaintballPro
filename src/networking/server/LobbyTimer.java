package networking.server;

import logic.RoundTimer;
import networking.game.UDPServer;

public class LobbyTimer extends Thread {
	
	public boolean m_running = true;
	
	private int lobbyTime;
	private ServerReceiver receiver;
	private UDPServer udpServer;
	private int gameMode;
	private Lobby lobby;
	
	public LobbyTimer(int lobbyTime, ServerReceiver receiver, UDPServer udpServer, int gameMode, Lobby lobby) {
		this.lobbyTime = lobbyTime;
		this.receiver = receiver;
		this.udpServer = udpServer;
		this.gameMode = gameMode;
		this.lobby = lobby;
	}
	
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
