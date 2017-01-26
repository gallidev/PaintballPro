package rendering;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Renderer extends Application
{
	private ClassLoader loader = getClass().getClassLoader();

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Canvas canvas = new Canvas(1024, 480);
		GraphicsContext graphics = canvas.getGraphicsContext2D();

		ScrollPane view = new ScrollPane(canvas);
		view.setPrefWidth(640);
		view.setPrefHeight(480);
		view.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		view.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		Scene scene = new Scene(view);
		primaryStage.setScene(scene);

		Asset gravel = new Asset(loader.getResourceAsStream("assets/gravel.png"), AssetType.Floor);
		for(int i = 0; i < 15; i++)
		{
			graphics.drawImage(gravel, i * 64, 256);
		}

		primaryStage.setTitle("Paintball Pro");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
