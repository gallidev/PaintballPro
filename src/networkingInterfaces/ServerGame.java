package networkingInterfaces;

import com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList;

import logic.CaptureTheFlagMode;
import logic.EscortMode;
import logic.GameMode;
import logic.KingOfTheHillMode;
import logic.Team;
import logic.TeamMatchMode;

/**
 * Server side integration to play a game in a specific game mode. Will be
 * instantiated in the lobby.
 * 
 * @author Alexandra Paduraru
 *
 */
public class ServerGame {

	private GameMode game;

	/**
	 * The server will run a specific game mode, given as an integer argument in
	 * this constructor and then translated into the corresponding game mode
	 * according to the protocol.
	 * 
	 * @param game
	 *            The game mode that will be started.
	 */
	public ServerGame(int gameMode, Team red, Team blue) {
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

	}

	public GameMode getGame() {
		return game;
	}
	
	//public ArrayList<ClientPlayer> getRedTeamPlayers(){
		//return game.getFirstTeam().getMembers()
	//}
}
