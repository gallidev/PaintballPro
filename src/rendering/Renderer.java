package rendering;

// if you get a "import com.google cannot be resolved" error, make sure gson-2.8.0.jar (in res) is added to Referenced Libraries in build path
import com.google.gson.Gson;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import physics.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Renderer extends Scene
{
	private static Pane view = new Pane();
	private double scale = 1;

	public Renderer()
	{
		super(view, 1024, 576);
		//16:9 aspect ratio
		widthProperty().addListener(observable ->
		{
			scale = getWidth() / 1024;
			view.setScaleX(scale);
			view.setScaleY((getWidth() * 0.5625) / 576);
		});

		Gson gson = new Gson();
		try
		{
			Map map = gson.fromJson(new FileReader("res/maps/elimination.json"), Map.class);
			for(Asset wall : map.getWalls())
			{
				wall.image = new ImageView(new Image("assets/" + wall.material + ".png", 64, 64, true, true));
				wall.image.setX(wall.x * 64);
				wall.image.setY(wall.y * 64);
				view.getChildren().add(wall.image);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		Player player = new Player(0, 0, false, this);
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
				view.setLayoutX(((getWidth() / 2) - player.getImage().getWidth() - player.getLayoutX()) * scale);
				view.setLayoutY(((getHeight() / 2) - player.getImage().getHeight() - player.getLayoutY()) * scale);
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
