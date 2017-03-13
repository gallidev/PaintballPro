import gui.GUIManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// Ensure res is part of Build Path folder and gson-2.8.0.jar is part of References Libraries before running.

public class GameClient extends Application {
	GUIManager m = new GUIManager();

	public static void main(String[] args) {
		launch(args);
	}
//always run the server first!!
	//for testing use ports  9878 first and then 9879 and make sure you close the server first

	@Override
	public void start(Stage stage) throws Exception{
		m.udpPortNumber = 9878;
		Font.loadFont(getClass().getResourceAsStream("styles/fonts/roboto-slab/RobotoSlab-Regular.ttf"), 16);
		Font.loadFont(getClass().getResourceAsStream("styles/fonts/roboto-slab/RobotoSlab-Regular.ttf"), 32);
		stage.getIcons().addAll(new Image("assets/icon_dock.png"), new Image("assets/icon_32.png"), new Image("assets/icon_16.png"));
		m.setStage(stage);
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> System.exit(0));
	}
}