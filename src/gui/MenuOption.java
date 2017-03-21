package gui;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * Class to hold data about a menu option (e.g. button)
 *
 * @author Jack Hughes
 */
public class MenuOption {

	private String name;
	private EventHandler<ActionEvent> handler;
	private boolean primary;

	/**
	 * Create a menu option, with a given name and event handler
	 *
	 * @param name    name of menu option
	 * @param primary is the option a primary action for the menu
	 * @param handler event handler for the menu option
	 */
	public MenuOption(String name, boolean primary, EventHandler<ActionEvent> handler) {
		super();
		this.name = name;
		this.handler = handler;
		this.primary = primary;
	}

	/**
	 * Get the option's name
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the option's event handler
	 *
	 * @return an event handler
	 */
	public EventHandler<ActionEvent> getHandler() {
		return handler;
	}

	/**
	 * Method to return true if the option is a primary action for the menu
	 *
	 * @return true if primary action
	 */
	public boolean isPrimary() {
		return primary;
	}
}
