package rendering;

import enums.MenuEnum;
import gui.GUIManager;
import gui.MenuOption;
import gui.MenuOptionSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

class PauseMenu extends SubScene
{
	private static Pane view = new Pane();
	boolean opened = false;

	PauseMenu(GUIManager m)
	{
		super(view, Renderer.view.getWidth(), Renderer.view.getHeight());
		view.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);");

		GridPane p = new GridPane();

		p.setAlignment(Pos.CENTER);
		p.setHgap(10);
		p.setVgap(10);
		p.setPadding(new Insets(25, 25, 25, 25));
		p.setPrefWidth(Renderer.view.getWidth());
		p.setPrefHeight(Renderer.view.getHeight());

		Label title = new Label("Paused");
		p.add(title, 0, 0);

		MenuOption[] set = {new MenuOption("Resume", true, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("ActionEvent: " + event);
				Renderer.togglePauseMenu();
			}
		}), new MenuOption("Settings", true, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("ActionEvent: " + event);

			}
		}), new MenuOption("Back to Main Menu", false, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				m.transitionTo(MenuEnum.MainMenu, null);
				System.out.println("ActionEvent: " + event);
			}
		})};

		GridPane buttonSet = MenuOptionSet.optionSetToGridPane(set);
		p.add(buttonSet, 0, 1);

		view.getStylesheets().add("styles/menu.css");
		view.getChildren().addAll(p);

	}
}
