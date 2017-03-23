package logic;

/**
 * Abstraction of a game object(this can be a player. a bullet, flags or
 * power-ups.
 *
 */
public interface GameObject {
	
	void tick();

	void interact();
}
