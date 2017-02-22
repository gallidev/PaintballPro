package players;

public class ServerBasicPlayer {
	private int ID;
	private int allocatedLobby;
	private String username;

	public ServerBasicPlayer(int id) {
		this.ID = id;
		// Set default values.
		this.allocatedLobby = -1;
		this.username = "USER" + id;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getAllocatedLobby() {
		return allocatedLobby;
	}

	public void setAllocatedLobby(int allocatedLobby) {
		this.allocatedLobby = allocatedLobby;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
