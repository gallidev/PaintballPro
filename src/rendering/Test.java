package rendering;

import gui.GUIManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setScene(new Renderer("elimination", (new GUIManager()).getAudioManager(), null));
		stage.setTitle("Paintball Pro");
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
