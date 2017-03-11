package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.server.ClientTable;
import networking.shared.MessageQueue;

/**
 * Test class to test Client Tables.
 * Tests - ClientTable.java
 * 
 * @author Matthew Walters
 */
public class TestClientTable {
	
	ClientTable table;

	@Before
	public void setUp() throws Exception {
		table = new ClientTable();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClientTable() {
		assertNotNull(table);
	}
	
	@Test
	public void testAdd() {
		assertEquals(table.add("test"),1);
		assertNotNull(table.getQueue(1));
		assertNotNull(table.getPlayer(1));
		assertEquals(table.getPlayer(1).getUsername(),"test");
		assertEquals(table.add("test2"),2);
	}

	@Test
	public void testAddUDPQueue() {
		table.addUDPQueue("127.0.0.1");
		assertNotNull(table.getUDPQueueWithIP("127.0.0.1"));
	}

	@Test
	public void testGetUDPQueueWithIP() {
		table.addUDPQueue("127.0.0.1");
		MessageQueue m = table.getUDPQueueWithIP("127.0.0.1");
		assertNotNull(m);
	}

	@Test
	public void testAddNewIP() {
		table.addNewIP("127.0.0.1", 1);
		assertEquals(table.getID("127.0.0.1"),1);
		assertEquals(table.getIP(1),"127.0.0.1");
	}
	
	@Test
	public void testRemoveClient() {
		int id = table.add("test");
		assertNotNull(table.getPlayer(id));
		table.removeClient(id);
		assertNull(table.getPlayer(id));
	}

}
