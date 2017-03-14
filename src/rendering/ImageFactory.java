package rendering;

import enums.TeamEnum;
import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageFactory
{
	private static final Image RED_PLAYER_IMAGE = new Image("assets/characters/player_red.png", 30, 64, true, true);
	private static final Image BLUE_PLAYER_IMAGE = new Image("assets/characters/player_blue.png", 30, 64, true, true);
	private static final Image FLAG_IMAGE = new Image("assets/flag.png");
	private static final HashMap<String, Image> materials = new HashMap<>();

	public static Image getPlayerImage(TeamEnum team)
	{
		return team == TeamEnum.RED ? RED_PLAYER_IMAGE : BLUE_PLAYER_IMAGE;
	}

	public static Image getObjectiveImage(ObjectiveType objective)
	{
		switch(objective)
		{
			case FLAG:
				return FLAG_IMAGE;
			default:
				throw new NoSuchFieldError("Objective type not found!");
		}
	}

	static Image getMaterialImage(String material)
	{
		materials.computeIfAbsent(material, m -> new Image("assets/materials/" + material + ".png"));
		return materials.get(material);
	}
}
