package networkingNew;


/**
 * Store message information to send to a client.
 */
public class Message {
	
	private final String text;

	/**
	 * Set message text to be sent to the client.
	 * @param text The text of the message.
	 */
	public Message(String text) {
		this.text = text;
	}
	/**
	 * Get the message text.
	 * @return The message text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * Gets the general contents of the object - the message stored.
	 * @return The message text.
	 */
	public String toString() {
		return text;
	}
}
