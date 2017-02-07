package gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class MenuOption {
	String name;
	EventHandler<ActionEvent> handler;
	
	public MenuOption(String name, EventHandler<ActionEvent> handler) {
		super();
		this.name = name;
		this.handler = handler;
	}

	public String getName() {
		return name;
	}

	public EventHandler<ActionEvent> getHandler() {
		return handler;
	}

}
