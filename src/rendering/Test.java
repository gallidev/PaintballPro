package rendering;

import enums.Menu;
import gui.GUIManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Random;

public class Test extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	private static void sprayTest(Stage stage)
	{
		WritableImage wall = new WritableImage(64, 64);
		wall.getPixelWriter().setPixels(0, 0, 64, 64, ImageFactory.getMaterialImage("sand_brick").getPixelReader(), 0, 0);
		ImageView iv = new ImageView(wall);

		Random random = new Random();
		double probability = 0.25;
		for(int i = 0; i < 64; i++)
		{
			for(int j = 0; j < 64; j++)
			{
				if(random.nextDouble() < probability)
					wall.getPixelWriter().setArgb(j, 63 - i, Color.BLUE.getRGB());
			}
			probability -= 0.003;
		}

		Pane view = new Pane(iv);
		Scene scene = new Scene(view, 256, 256);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		GUIManager guiManager = new GUIManager();
		guiManager.setStage(stage);
		guiManager.transitionTo(Menu.CTFSingle);
//		sprayTest(stage);
	}
}