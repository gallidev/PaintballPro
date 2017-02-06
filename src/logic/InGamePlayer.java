package logic;

/**
 * Class to represent the client version of a player in the game. Stores just
 * general information about the player, which is necessary for the client
 *  
 * @author Alexandra Paduraru
 */
public class InGamePlayer extends GameObject {

	private String nickname;

	/**
	 * Initialises a player with a nickname and its initial coordinates.
	 * @param nickname The nickname chose by the player.
	 * @param xCoord The x coordinate fo the player.
	 * @param yCoord xCoord The x coordinate fo the player.
	 */
	public InGamePlayer(String nickname, int xCoord, int yCoord) {
		super(xCoord, yCoord);
		this.nickname = nickname;
	}

	/**
	 * Returns the nickname of the player.
	 * @return The player's chosen nickname.
	 */
	public String getNickname() {
		return nickname;
	}

}
