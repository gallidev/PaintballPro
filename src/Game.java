import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.stage.Stage;
import rendering.Renderer;

// Ensure res is part of Build Path folder and gson-2.8.0.jar is part of References Libraries before running.

public class Game extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setScene(new Renderer());
		stage.setTitle("Paintball Pro");
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
