package rendering;

import com.google.gson.Gson;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static rendering.Renderer.view;

class Map
{
	String name;
	@SuppressWarnings("MismatchedReadAndWriteOfArray")
	private Wall[] walls;

	static void load(String url)
	{
		Gson gson = new Gson();
		try
		{
			Map map = gson.fromJson(new FileReader(url), Map.class);
			for(Wall wall : map.walls)
			{
				wall.blocks = new Group();
				for(int i = 0; i < wall.length; i++)
				{
					ImageView block = new ImageView(new Image("assets/" + wall.material + ".png", 64, 64, true, true));
					block.setX(wall.orientation ? (i + wall.x) * 64 : wall.x * 64);
					block.setY(wall.orientation ? wall.y * 64 : (i + wall.y) * 64);
					wall.blocks.getChildren().add(block);
				}
				view.getChildren().add(wall.blocks);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
