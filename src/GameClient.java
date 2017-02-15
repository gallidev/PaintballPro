import gui.GUIManager;
// Ensure res is part of Build Path folder and gson-2.8.0.jar is part of References Libraries before running.
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import rendering.Renderer;

import javax.swing.*;

public class GameClient extends Application {
	GUIManager m = new GUIManager();
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.getIcons().addAll(new Image("assets/icon_16.png"), new Image("assets/icon_16.png"), new Image("assets/icon_16.png"));
		m.setStage(stage);
//		stage.setScene(new Renderer("elimination"));
//		stage.setTitle("Paintball Pro");
//		stage.show();
	}

	public static void main(String[] args) {
		if (System.getProperty("os.name").contains("Mac")) {
			java.awt.Image image = new ImageIcon("res/assets/icon_dock.png").getImage();
			com.apple.eawt.Application.getApplication().setDockIconImage(image);
		}
		launch(args);
	}
}
