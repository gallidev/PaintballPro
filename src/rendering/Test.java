package rendering;
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
		GUIManager guiManager = new GUIManager();
		guiManager.setStage(stage);
		stage.setScene(new Renderer("ctf", guiManager));
		stage.show();
	}
}