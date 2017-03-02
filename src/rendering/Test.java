package rendering;
import gui.GUIManager;
import javafx.application.Application;
import javafx.stage.Stage;
public class Test extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setScene(new Renderer("ctf", new GUIManager().getAudioManager()));
		stage.show();
	}
	public static void main(String[] args)
	{
		launch(args);
	}
}