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
		GridPane gp = new GridPane();
				
		// Setup the grid pane styling
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));
		
		for (int i = 0; i < opts.length; i++) {
			// Create the button, set the text and event handler,
			// maximise the width of the button to all be the same,
			// and add to the grid pane
			Button btn = new Button();
			btn.setText(opts[i].getName());
			btn.setOnAction(opts[i].getHandler());
			btn.setMaxWidth(Double.MAX_VALUE);
			gp.add(btn, 0, i + 1);
		}
		
		
		// Return the grid pane
		return gp;
	}
}
