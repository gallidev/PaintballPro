package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.shared.Message;
import networking.shared.MessageQueue;

/**
 * Test class to test Messages and Message Queues.
 * Tests - Message.java, MessageQueue.java
 * 
 * @author Matthew Walters
 */
public class TestMessages {

	Message message;
	MessageQueue queue;
	
	@Before
	public void setUp() throws Exception {
		message = new Message("test message");
		queue = new MessageQueue();
	}

	@After
	public void tearDown() throws Exception {
		message = null;
		queue = null;
	}

	//-----------
	// Message
	//-----------
	@Test
	public void testMessage() {
		assertNotNull(message);
	}

	@Test
	public void testGetText() {
		assertEquals(message.getText(),"test message");
	}
	
	//-----------
	//MessageQueue
	//-----------
	@Test
	public void testMessageQueue() {
		assertNotNull(queue);
	}
	
	@Test
	public void testOfferTake() {
		queue.offer(message);
		Message m = queue.take();
		assertEquals(m.getText(),"test message");
	}
}