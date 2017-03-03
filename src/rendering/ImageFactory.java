package rendering;

import enums.TeamEnum;
import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageFactory
{
	private static final Image redPlayerImage = new Image("assets/characters/player_red.png", 30, 64, true, true);
	private static final Image bluePlayerImage = new Image("assets/characters/player_blue.png", 30, 64, true, true);
	private static final Image redSpawnTile = new Image("assets/materials/red_spawn.png");
	private static final Image blueSpawnTile = new Image("assets/materials/blue_spawn.png");

	private static final HashMap<String, Image> materials = new HashMap<>();

	public static Image getPlayerImage(TeamEnum team)
	{
		return team == TeamEnum.RED ? redPlayerImage : bluePlayerImage;
	}

	static Image getMaterialImage(String material)
	{
		materials.computeIfAbsent(material, m -> new Image("assets/materials/" + material + ".png", 64, 64, true, true));
		return materials.get(material);
	}

	static Image getSpawnTile(TeamEnum team)
	{
		return team == TeamEnum.RED ? redSpawnTile : blueSpawnTile;
	}
}
