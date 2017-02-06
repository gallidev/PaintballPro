package networkingServer;

public class Player {
	private int ID;
	private int allocatedLobby;
	
	public Player(int id)
	{
		this.ID = id;
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
	
}
