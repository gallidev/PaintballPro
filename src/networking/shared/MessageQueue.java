package networking.shared;
import java.util.concurrent.*;
/**
 * Class to store messages pending for a particular client.
 * 
 * @author MattW
 */
public class MessageQueue {
	
	private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	/**
	 * Inserts the specified message into this queue.
	 * 
	 * @param m Message to add to the queue.
	 */
	public void offer(Message m) {
		queue.offer(m);
	}
	/**
	 * Retrieve messages from the queue.
	 * 
	 * @return Next message in the queue.
	 */
	public Message take() {
		while (true) {
			try {
				return (queue.take());
			} catch (InterruptedException e) {
				// This can in principle be triggered by queue.take().
				// But this would only happen if we had interrupt() in our code,
				// which we don't for this thread.
				// In any case, if it could happen, we should do nothing here
				// and try again until we succeed without interruption.
			}
		}
	}
}