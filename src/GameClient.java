import gui.GUIManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Class for running a game client
 */
public class GameClient extends Application {
	private GUIManager guiManager = new GUIManager();

	/**
	 * Start the application
	 * @param stage the primary stage to use for the GUI
	 * @throws Exception exception when creating the application
	 */
	@Override
	public void start(Stage stage) throws Exception{
		guiManager.udpPortNumber = 9879;
		Font.loadFont(getClass().getResourceAsStream("styles/fonts/roboto-slab/RobotoSlab-Regular.ttf"), 16);
		Font.loadFont(getClass().getResourceAsStream("styles/fonts/roboto-slab/RobotoSlab-Regular.ttf"), 32);
		stage.getIcons().addAll(new Image("assets/icon_dock.png"), new Image("assets/icon_32.png"), new Image("assets/icon_16.png"));
		guiManager.setStage(stage);
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> System.exit(0));
	}

	/**
	 * Main method for starting the server
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}