package rendering;

import enums.Menu;
import gui.GUIManager;
import gui.MenuControls;
import gui.MenuOption;
import gui.MenuOptionSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

class PauseMenu extends SubScene
{
	static GridPane p = new GridPane();
	boolean opened = false;

	PauseMenu(GUIManager m)
	{
		super(p, m.getStage().getWidth(), m.getStage().getHeight());
		p.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);");

		p.setAlignment(Pos.CENTER);
		p.setHgap(10);
		p.setVgap(10);
		p.setPadding(new Insets(25, 25, 25, 25));

		Image i = new Image("assets/paintballlogo.png");
		ImageView iv = new ImageView(i);
		iv.setId("logo");
		iv.setPreserveRatio(true);
		iv.setFitWidth(150);
		p.add(MenuControls.centreInPane(iv), 0, 0);

		Label title = new Label("Paused");
		title.setStyle("-fx-text-alignment: center; -fx-font-size: 32px");
		p.add(MenuControls.centreInPane(title), 0, 1);

		MenuOption[] set = {new MenuOption("Resume", true, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("ActionEvent: " + event);
				GUIManager.renderer.togglePauseMenu();
			}
		}), new MenuOption("Settings", true, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("ActionEvent: " + event);
				GUIManager.renderer.toggleSettingsMenu();
			}
		}), new MenuOption("Back to Main Menu", false, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				m.exitClient();
				m.transitionTo(Menu.MainMenu);
				System.out.println("ActionEvent: " + event);
			}
		})};

		GridPane buttonSet = MenuOptionSet.optionSetToGridPane(set);
		p.add(buttonSet, 0, 2);

		p.getStylesheets().add("styles/menu.css");
	}
}
