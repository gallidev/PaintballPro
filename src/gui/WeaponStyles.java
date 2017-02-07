package gui;

public class WeaponStyles {
	// TODO: add static types for each weapon style
	public static final String[] weapons = {"assets/weapons/1.png", "assets/weapons/2.png"};
	
	public static String getFirst() {
		return WeaponStyles.weapons[0];
	}
	
	public static String getNext(String current) {
		for (int i = 0; i < WeaponStyles.weapons.length - 1; i++) {
			if (WeaponStyles.weapons[i].equals(current)) {
				return WeaponStyles.weapons[i + 1];
			}
		}
		return WeaponStyles.getFirst();
	}
}
