package rendering;

import enums.PowerupType;
import enums.TeamEnum;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * A factory class for all in-game images that are to be displayed. Utilising a single <code>Image</code> amongst all <code>ImageView</code> objects boosts performance.
 *
 * @author Artur Komoter
 */
public class ImageFactory {
	//player images
	private static final Image RED_PLAYER = new Image("assets/characters/player_red.png", 30, 64, true, true);
	private static final Image BLUE_PLAYER = new Image("assets/characters/player_blue.png", 30, 64, true, true);

	//flag images
	private static final Image RED_PLAYER_FLAG = new Image("assets/characters/player_red_flag.png", 30, 64, true, true);
	private static final Image HUD_BLUE_FLAG = new Image("assets/objects/blue_flag.png");
	private static final Image HUD_RED_FLAG = new Image("assets/objects/red_flag.png");
	private static final Image BLUE_PLAYER_FLAG = new Image("assets/characters/player_blue_flag.png", 30, 64, true, true);
	private static final Image FLAG = new Image("assets/objects/flag.png", 48, 48, true, true);

	//powerup images
	private static final Image POWERUP_SHIELD = new Image("assets/objects/powerup_shield.png");
	private static final Image POWERUP_SPEED = new Image("assets/objects/powerup_speed.png");

	private static final HashMap<String, Image> MATERIALS = new HashMap<>();

	/**
	 * Get an image of a player from a certain team.
	 *
	 * @param team The team that the player belongs to
	 * @return A player image respective to its team
	 */
	public static Image getPlayerImage(TeamEnum team) {
		return team == TeamEnum.RED ? RED_PLAYER : BLUE_PLAYER;
	}

	/**
	 * Get an image of a player from a certain team carrying a flag.
	 *
	 * @param team The team that the player belongs to
	 * @return A flag-carrying player image respective to its team
	 */
	public static Image getPlayerFlagImage(TeamEnum team) {
		return team == TeamEnum.RED ? RED_PLAYER_FLAG : BLUE_PLAYER_FLAG;
	}

	/**
	 * Get an image of a team's flag icon that is displayed on the head up display.
	 *
	 * @param team The team that the flag icon belongs to
	 * @return A flag icon image respective to its team
	 */
	static Image getHudFlagImage(TeamEnum team) {
		return team == TeamEnum.RED ? HUD_RED_FLAG : HUD_BLUE_FLAG;
	}

	/**
	 * Get an image of a game objective displayed on the map.
	 *
	 * @param objective Type of a game objective
	 * @return A game objective image
	 */
	public static Image getObjectiveImage(ObjectType objective) {
		switch(objective) {
			case FLAG:
				return FLAG;
			default:
				throw new NoSuchElementException("Objective type not found!");
		}
	}

	/**
	 * Get an image of a powerup that is displayed on the map.
	 *
	 * @param powerup Type of a powerup
	 * @return A powerup image
	 */
	public static Image getPowerupImage(PowerupType powerup) {
		switch(powerup) {
			case SHIELD:
				return POWERUP_SHIELD;
			case SPEED:
				return POWERUP_SPEED;
			default:
				throw new NoSuchElementException("Powerup type not found!");
		}
	}

	/**
	 * Get an image of the material that a map asset is composed of.
	 *
	 * @param material Name of the material image file
	 * @return An image of the material of a map asset
	 */
	static Image getMaterialImage(String material) {
		MATERIALS.computeIfAbsent(material, m -> new Image("assets/materials/" + material + ".png"));
		return MATERIALS.get(material);
	}
}
