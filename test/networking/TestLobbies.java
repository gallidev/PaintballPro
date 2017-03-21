package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.server.Lobby;
import networking.server.LobbyTable;
import players.ServerBasicPlayer;

/**
 * Test class to test lobby related classes.
 * Tests - Lobby.java, LobbyTable.java
 * 
 * @author Matthew Walters
 */
public class TestLobbies {
	
	Lobby lobby;
	LobbyTable lobbytable;
	ServerBasicPlayer player;

	@Before
	public void setUp() throws Exception {
		lobby = new Lobby(1, 1,true);
		lobbytable = new LobbyTable();
		lobbytable.testEnv = true;
		player = new ServerBasicPlayer(1);
	}

	@After
	public void tearDown() throws Exception {
		lobby = null;
		lobbytable = null;
		player = null;
	}

	// -----------
	// Lobby Tests
	// -----------
	@Test
	public void testLobby() {
		assertNotNull(lobby);
	}

	@Test
	public void testGetID() {
		assertEquals(lobby.getID(),1);
	}

	@Test
	public void testGetInGameStatus() {
		assertFalse(lobby.getInGameStatus());
	}

	@Test
	public void testSwitchGameStatus() {
		assertFalse(lobby.getInGameStatus());
		lobby.switchGameStatus();
		assertTrue(lobby.getInGameStatus());
	}

	@Test
	public void testGetGameType() {
		assertEquals(lobby.getGameType(),1);
	}

	@Test
	public void testIsMaxPlayersReached() {
		assertFalse(lobby.isMaxPlayersReached());
	}

	@Test
	public void testGetCurrPlayerTotal() {
		assertEquals(lobby.getCurrPlayerTotal(),0);
	}

	@Test
	public void testAddPlayer() {
		assertEquals(lobby.getCurrPlayerTotal(),0);
		lobby.addPlayer(player, 0);
		assertEquals(lobby.getCurrPlayerTotal(),1);
		assertEquals(lobby.getPlayers().length,1);
	}
	
	@Test
	public void testGetTeam() {
		lobby.addPlayer(player, 0);
		assertEquals(lobby.getTeam(2),"USER1");
		assertEquals(lobby.getTeam(1),"");
	}
	
//	@Test
//	public void testGetRedTeam() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBlueTeam() {
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void testSwitchTeam() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetPlayers() {
		lobby.addPlayer(player, 0);
		assertEquals(lobby.getPlayers().length,1);
		assertEquals(lobby.getPlayers()[0].getID(),1);
	}
	
	@Test
	public void testRemovePlayer() {
		lobby.addPlayer(player, 0);
		assertEquals(lobby.getPlayers().length,1);
		lobby.removePlayer(player);
		assertEquals(lobby.getPlayers().length,0);
		assertEquals(lobby.getCurrPlayerTotal(),0);
	}

//	@Test
//	public void testPlayGame() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testTimerStart() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetWinner() {
//		fail("Not yet implemented");
//	}
	
	// ---------------
	// LobbyTable Tests
	// ---------------
	@Test
	public void testLobbyTable() {
		assertNotNull(lobbytable);
	}
	
	@Test
	public void testAddPlayerToLobby() {
		ServerBasicPlayer player2 = new ServerBasicPlayer(2);
		assertEquals(player2.getAllocatedLobby(),-1);
		lobbytable.addPlayerToLobby(player2, 1, null, null);
		assertEquals(player2.getAllocatedLobby(),1);
	}

	@Test
	public void testGetLobby() {
		lobbytable.addPlayerToLobby(player, 1, null, null);
		assertEquals(lobbytable.getLobby(1).getPlayers().length,1);
	}
		
	@Test
	public void testSwitchTeams() {
		lobbytable.addPlayerToLobby(player, 1, null, null);
		assertEquals(lobbytable.getLobby(1).getTeam(2),"USER1");
		lobbytable.switchTeams(player, null);
		assertEquals(lobbytable.getLobby(1).getTeam(1),"USER1");
		assertEquals(lobby.getTeam(2),"");
	}
	
	@Test
	public void testRemovePlayer2() {
		lobbytable.addPlayerToLobby(player, 1, null, null);
		assertEquals(player.getAllocatedLobby(),1);
		assertEquals(lobbytable.removePlayer(player),1);
		assertEquals(player.getAllocatedLobby(),-1);
	}
	
	@Test
	public void testRemoveLobby() {
		lobbytable.addPlayerToLobby(player, 1, null, null);
		assertNotNull(lobbytable.getLobby(1));
		lobbytable.removeLobby(1);
		assertNull(lobbytable.getLobby(1));
	}
}