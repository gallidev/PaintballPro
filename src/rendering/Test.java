package rendering;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Test extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Image sand = new Image("assets/materials/sand.png", 64, 64, true, true);
		WritableImage floor = new WritableImage(128, 128);
		floor.getPixelWriter().setPixels(0, 0, 64, 64, sand.getPixelReader(), 0, 0);
		floor.getPixelWriter().setPixels(64, 0, 64, 64, sand.getPixelReader(), 0, 0);
		floor.getPixelWriter().setPixels(0, 64, 64, 64, sand.getPixelReader(), 0, 0);
		floor.getPixelWriter().setPixels(64, 64, 64, 64, sand.getPixelReader(), 0, 0);
		Pane view = new Pane();
		view.getChildren().add(new ImageView(floor));
		Scene scene = new Scene(view, 1024, 576);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
