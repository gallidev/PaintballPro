import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.stage.Stage;
import rendering.Renderer;

public class Game extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setScene(new Renderer());
		stage.setTitle("Paintball Pro");
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
