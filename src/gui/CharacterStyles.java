package gui;

/**
 * Class to contain all of the character styles, with helper methods
 */
public class CharacterStyles {
	// List of character resources
	public static final String[] characters = {"assets/characters/1.png", "assets/characters/2.png"};
	
	/**
	 * Get the first character resource
	 * @return first character resource location
	 */
	public static String getFirst() {
		return CharacterStyles.characters[0];
	}
	
	/**
	 * Get the next character style, given the current character style
	 * @param current current character style location
	 * @return next character style
	 */
	public static String getNext(String current) {
		for (int i = 0; i < CharacterStyles.characters.length - 1; i++) {
			if (CharacterStyles.characters[i].equals(current)) {
				return CharacterStyles.characters[i + 1];
			}
		}
		return CharacterStyles.getFirst();
	}
}
