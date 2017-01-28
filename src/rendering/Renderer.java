package rendering;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Random;

public class Renderer extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Canvas canvas = new Canvas(1024, 1024);
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		Group root = new Group(canvas);
		Scene scene = new Scene(root, 640, 480);
		scene.setCursor(Cursor.CROSSHAIR);
		primaryStage.setScene(scene);

		Random random = new Random();
		for(int i = 0; i < 15; i++)
		{
			int x = random.nextInt(15), y = random.nextInt(15);
			Asset gravel = new Asset("assets/gravel.png", AssetType.Floor, x * 64, y * 64);
			graphics.drawImage(gravel, x * 64, y * 64);
		}

		primaryStage.setTitle("Paintball Pro");
		primaryStage.show();

		new CameraControl(scene, root);
	}
}
