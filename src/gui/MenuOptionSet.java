package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Helper class for working with a collection of menu options
 */
public class MenuOptionSet {
	
	/**
	 * Helper method to turn a collection of menu options into a single grid pane
	 * @param opts array of menu options
	 * @return grid pane
	 */
	public static GridPane optionSetToGridPane(MenuOption[] opts) {
		// Create a new grid pane
		GridPane gridPane = new GridPane();
				
		// Setup the grid pane styling
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(25, 25, 25, 25));
		
		for (int i = 0; i < opts.length; i++) {
			// Create the button, set the text and event handler,
			// maximise the width of the button to all be the same,
			// and add to the grid pane
			Button button = new Button();
			button.setText(opts[i].getName());
			button.setOnAction(opts[i].getHandler());
			button.setMaxWidth(Double.MAX_VALUE);
			if (opts[i].isPrimary()) {
				button.setId("primary");
			}
			gridPane.add(button, 0, i + 1);
		}
		
		
		// Return the grid pane
		return gridPane;
	}
}
