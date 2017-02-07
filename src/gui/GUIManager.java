package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUIManager extends Application {
	private Stage s;
	private UserSettings user = UserSettingsManager.loadSettings();
	public final int width = 800;
	public final int height = 600;
	
	public static void main(String[] args) {
		// TODO: Remove this method
		launch(args);
	}
	
	public UserSettings getUserSettings() {
		return user;
	}

	public void transitionTo(String menu, Object o) {
		switch (menu) {
			case "Main":
				s.setScene(MainMenu.getScene(this));
				break;
			case "Settings":
				s.setScene(SettingsMenu.getScene(this));
				break;
		}
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		s = primaryStage;
		s.setTitle("Paintball Pro");
		s.setScene(MainMenu.getScene(this));
		s.show();
	}

}
