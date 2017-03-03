import gui.GUIManager;
// Ensure res is part of Build Path folder and gson-2.8.0.jar is part of References Libraries before running.
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameClient extends Application {
	GUIManager m = new GUIManager();
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.getIcons().addAll(new Image("assets/icon_dock.png"), new Image("assets/icon_32.png"), new Image("assets/icon_16.png"));
		m.setStage(stage);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
//		stage.setScene(new Renderer("elimination"));
//		stage.setTitle("Paintball Pro");
//		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}