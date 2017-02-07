package gui;

/**
 * Class to contain all of the weapon styles, with helper methods
 */
public class WeaponStyles {
	// List of weapon resources
	public static final String[] weapons = {"assets/weapons/1.png", "assets/weapons/2.png"};
	
	/**
	 * Get the first weapon resource
	 * @return first weapon resource location
	 */
	public static String getFirst() {
		return WeaponStyles.weapons[0];
	}
	
	/**
	 * Get the next weapon style, given the current weapon style
	 * @param current current weapon style location
	 * @return next weapon style
	 */
	public static String getNext(String current) {
		for (int i = 0; i < WeaponStyles.weapons.length - 1; i++) {
			if (WeaponStyles.weapons[i].equals(current)) {
				return WeaponStyles.weapons[i + 1];
			}
		}
		return WeaponStyles.getFirst();
	}
}
