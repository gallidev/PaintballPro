import enums.Menu;
import gui.GUIManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setOnCloseRequest(event -> System.exit(0));
		GUIManager guiManager = new GUIManager();
		guiManager.setStage(stage);
		guiManager.transitionTo(Menu.CTFSingle);
	}
}