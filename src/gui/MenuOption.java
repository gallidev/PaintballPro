package gui;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * Class to hold data about a menu option (e.g. button)
 */
public class MenuOption {
	String name;
	EventHandler<ActionEvent> handler;
	boolean primary;
	
	/**
	 * Create a menu option, with a given name and event handler
	 * @param name
	 * @param handler
	 */
	public MenuOption(String name, boolean primary, EventHandler<ActionEvent> handler) {
		super();
		this.name = name;
		this.handler = handler;
		this.primary = primary;
	}

	/**
	 * Get the option's name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the option's event handler
	 * @return an event handler
	 */
	public EventHandler<ActionEvent> getHandler() {
		return handler;
	}

	public boolean isPrimary() {
		return primary;
	}
}
