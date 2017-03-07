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
//		WritableImage wall = new WritableImage(64, 64);
//		wall.getPixelWriter().setPixels(0, 0, 64, 64, ImageFactory.getMaterialImage("sand_brick").getPixelReader(), 0, 0);
//
//		Random random = new Random();
//		double probability = 0.25;
//		for(int i = 0; i < 64; i++)
//		{
//			for(int j = 0; j < 64; j++)
//			{
//				if(random.nextDouble() < probability)
//					wall.getPixelWriter().setArgb(i, j, Color.BLUE.getRGB());
//			}
//			probability -= 0.003;
//		}
//
//		Pane view = new Pane(new ImageView(wall));
//		Scene scene = new Scene(view, 256, 256);
//		stage.setScene(scene);
		stage.show();
	}
}