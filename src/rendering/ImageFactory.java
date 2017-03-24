package rendering;

import enums.TeamEnum;
import javafx.scene.image.Image;
import physics.PowerupType;

import java.util.HashMap;

public class ImageFactory
{
	private static final Image RED_PLAYER = new Image("assets/characters/player_red.png", 30, 64, true, true);
	private static final Image BLUE_PLAYER = new Image("assets/characters/player_blue.png", 30, 64, true, true);
	private static final Image RED_PLAYER_FLAG = new Image("assets/characters/player_red_flag.png", 30, 64, true, true);
	private static final Image HUD_BLUE_FLAG = new Image("assets/blue_flag.png");
	private static final Image HUD_RED_FLAG = new Image("assets/red_flag.png");
	private static final Image BLUE_PLAYER_FLAG = new Image("assets/characters/player_blue_flag.png", 30, 64, true, true);
	private static final Image FLAG = new Image("assets/flag.png", 48, 48, true, true);

	private static final Image POWERUP_SHIELD = new Image("assets/powerup_shield.png");
	private static final Image POWERUP_SPEED = new Image("assets/powerup_speed.png");

	private static final HashMap<String, Image> MATERIALS = new HashMap<>();

	public static Image getPlayerImage(TeamEnum team)
	{
		return team == TeamEnum.RED ? RED_PLAYER : BLUE_PLAYER;
	}

	public static Image getPlayerFlagImage(TeamEnum team)
	{
		return team == TeamEnum.RED ? RED_PLAYER_FLAG : BLUE_PLAYER_FLAG;
	}

	static Image getHudFlagImage(TeamEnum team)
	{
		return team == TeamEnum.RED ? HUD_RED_FLAG : HUD_BLUE_FLAG;
	}

	public static Image getObjectiveImage(ObjectType objective)
	{
		switch(objective)
		{
			case FLAG:
				return FLAG;
			default:
				throw new NoSuchFieldError("Objective type not found!");
		}
	}

	public static Image getPowerupImage(PowerupType powerup)
	{
		switch(powerup)
		{
			case SHIELD:
				return POWERUP_SHIELD;
			case SPEED:
				return POWERUP_SPEED;
			default:
				throw new NoSuchFieldError("Powerup type not found!");
		}
	}

	static Image getMaterialImage(String material)
	{
		MATERIALS.computeIfAbsent(material, m -> new Image("assets/materials/" + material + ".png"));
		return MATERIALS.get(material);
	}
}
