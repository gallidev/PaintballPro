package networking.interfaces;

import java.util.ArrayList;

import enums.TeamEnum;
import logic.GameMode;
import networking.game.UDPServer;
import networking.server.ServerMsgReceiver;
import networking.shared.Message;
import players.ServerPlayer;
import serverLogic.CaptureTheFlagMode;
import serverLogic.EscortMode;
import serverLogic.KingOfTheHillMode;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

/**
 * Server side integration to play a game in a specific game mode. Will be
 * instantiated in the lobby.
 * 
 * @author Alexandra Paduraru
 *
 */
public class ServerGame {

	private GameMode game;
	private UDPServer serverReceiver;
	private int lobbyID;

	/**
	 * The server will run a specific game mode, given as an integer argument in
	 * this constructor and then translated into the corresponding game mode
	 * according to the protocol.
	 * 
	 * @param game
	 *            The game mode that will be started.
	 */
	public ServerGame(int gameMode, Team red, Team blue, UDPServer receiver, int lobbyID) {
		this.lobbyID = lobbyID;
		switch (gameMode) {
		case 1:
			game = new TeamMatchMode(red, blue);
			break;
		case 2:
			game = new KingOfTheHillMode(red, blue);
			break;
		case 3:
			game = new CaptureTheFlagMode(red, blue);
			break;
		case 4:
			game = new EscortMode(red, blue);
			break;
		default:
			game = new TeamMatchMode(red, blue);
			break;
		}
		this.serverReceiver = receiver;

	}
	
	/**
	 * Starts a new game in the given mode and notifies all players involved in the game that the game has started.
	 */
	public void startGame(){
		game.start();
		//String msgToClients = "StartGame";
		//serverReceiver.sendToAll(msgToClients);
	}
	
	/**
	 * Checks to see when a game has ended and sends the appropriate message to all clients.
	 * @param t Enumeration of Team colours - Red and Blue.
 	 */
	public void endGame(TeamEnum t){
		String toBeSent = "EndGame:";
		
		if (t== TeamEnum.RED){
			toBeSent += "Red";
			System.out.println("Winner: Red");
		}
		else{
			toBeSent += "Blue";
			System.out.println("Winner: Blue");
		}
		
		if (game.isGameFinished())
			serverReceiver.sendToAll(toBeSent,lobbyID);
		
		
	}
	
	/**
	 * Send a message to all clients that the game has ended.
	 */
	public void endGame(){
		if (game.isGameFinished())
			serverReceiver.sendToAll("EndGame",lobbyID);
	}
	
	/*Getters and setters*/
	/**
	 * Get the game object.
	 * @return Game object.
	 */
	public GameMode getGame() {
		return game;
	}
	
	/**
	 * Get current players in the Red team.
	 * @return Red team as an arraylist.
	 */
	public ArrayList<ServerPlayer> getRedTeamPlayers(){
		return game.getFirstTeam().getMembers();
	}
	
	/**
	 * Get current players in the Blue team.
	 * @return Blue team as an arraylist.
	 */
	public ArrayList<ServerPlayer> getBlueTeamPlayers(){
		return game.getSecondTeam().getMembers();
	}
	
	/**
	 * Get current players in both the Red and Blue team.
	 * @return Both teams as an arraylist.
	 */
	public ArrayList<ServerPlayer> getAllPlayers(){
		ArrayList<ServerPlayer> redTeam = getRedTeamPlayers();
		ArrayList<ServerPlayer>  blueTeam = getBlueTeamPlayers();
		
		redTeam.addAll(blueTeam);
		
		return redTeam;
	}
}