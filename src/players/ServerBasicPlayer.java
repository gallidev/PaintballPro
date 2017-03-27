package players;


/**
 * The Class represents a player with basic information on the server, for lobby and game creation porposues
 */
public class ServerBasicPlayer {

	/** The id. */
	private int ID;

	/** The allocated lobby. */
	private int allocatedLobby;

	/** The username. */
	private String username;

	/**
	 * Instantiates a new server basic player.
	 *
	 * @param id the id of the player
	 */
	public ServerBasicPlayer(int id) {
		this.ID = id;
		// Set default values.
		this.allocatedLobby = -1;
		this.username = "USER" + id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets the id.
	 *
	 * @param iD the new id
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * Gets the allocated lobby.
	 *
	 * @return the allocated lobby
	 */
	public int getAllocatedLobby() {
		return allocatedLobby;
	}

	/**
	 * Sets the allocated lobby.
	 *
	 * @param allocatedLobby the new allocated lobby
	 */
	public void setAllocatedLobby(int allocatedLobby) {
		this.allocatedLobby = allocatedLobby;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
