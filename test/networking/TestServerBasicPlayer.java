package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import players.ServerBasicPlayer;

/**
 * Test class to test Server Player class, used in Lobbies.
 * Tests - ServerBasicPlayer.java
 * 
 * @author Matthew Walters
 */
public class TestServerBasicPlayer {
	
	ServerBasicPlayer player;

	@Before
	public void setUp() throws Exception {
		player = new ServerBasicPlayer(1);
	}

	@After
	public void tearDown() throws Exception {
		player = null;
	}

	@Test
	public void testServerBasicPlayer() {
		assertNotNull(player);
	}

	@Test
	public void testGetID() {
		assertEquals(player.getID(), 1);
	}

	@Test
	public void testSetID() {
		player.setID(2);
		assertEquals(player.getID(),2);
	}

	@Test
	public void testGetAllocatedLobby() {
		assertEquals(player.getAllocatedLobby(),-1);
	}

	@Test
	public void testSetAllocatedLobby() {
		player.setAllocatedLobby(3);
		assertEquals(player.getAllocatedLobby(),3);
	}

	@Test
	public void testGetUsername() {
		assertEquals(player.getUsername(),"USER1");
	}

	@Test
	public void testSetUsername() {
		player.setUsername("TEST");
		assertEquals(player.getUsername(),"TEST");
	}
}
