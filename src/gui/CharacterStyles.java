package gui;

public class CharacterStyles {
	// TODO: add static values for each character style
	public static final String[] characters = {"assets/characters/1.png", "assets/characters/2.png"};
	
	public static String getFirst() {
		return CharacterStyles.characters[0];
	}
	
	public static String getNext(String current) {
		for (int i = 0; i < CharacterStyles.characters.length - 1; i++) {
			if (CharacterStyles.characters[i].equals(current)) {
				return CharacterStyles.characters[i + 1];
			}
		}
		return CharacterStyles.getFirst();
	}
}
