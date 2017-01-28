package rendering;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Renderer extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		Group root = new Group();
		Scene scene = new Scene(root, 640, 480);
		scene.setCursor(Cursor.CROSSHAIR);
		stage.setScene(scene);

		Group wall = new Group();
		for(int i = 1; i < 4; i++)
		{
			Asset gravel = new Asset("assets/gravel.png", AssetType.Floor, i * 64, 64);
			gravel.setTranslateX(gravel.x);
			gravel.setTranslateY(gravel.y);
			wall.getChildren().add(gravel);
		}
		wall.setEffect(new DropShadow(15, 0, 5, Color.BLACK));
		root.getChildren().add(wall);

		stage.setTitle("Paintball Pro");
		stage.show();

		new CameraControl(scene, root);
	}
}
