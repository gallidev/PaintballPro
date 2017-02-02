package rendering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import physics.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Renderer extends Scene
{
	private static Group view = new Group();

	public Renderer()
	{
		super(view, 800, 600);

		Gson gson = new Gson();
		try
		{
			Map map = gson.fromJson(new FileReader("res/maps/elimination.json"), Map.class);
			for(Asset wall : map.getWalls())
				view.getChildren().add(wall);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		Player player = new Player(0, 0, false);
		view.getChildren().add(player);

		KeyPressListener keyPressListener = new KeyPressListener(player);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(player);
		MouseListener mouseListener = new MouseListener(player);

		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				player.tick();
				view.setLayoutX((getWidth() / 2) - player.getImage().getWidth() - player.getLayoutX());
				view.setLayoutY((getHeight() / 2) - player.getImage().getHeight() - player.getLayoutY());
				for(Bullet pellet : player.getBullets())
				{
					if(!view.getChildren().contains(pellet))
						view.getChildren().add(pellet);
					else
					{
						pellet.setX(pellet.getX());
						pellet.setY(pellet.getY());
					}
				}
			}
		}.start();
	}
}
