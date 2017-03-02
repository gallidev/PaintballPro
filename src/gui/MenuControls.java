package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Class containing helper methods for configuring menu controls
 */
public class MenuControls {
	
	/**
	 * Method to centre a node in a GridPane cell
	 * @param n node to centre
	 * @return GridPane that should be placed where the node needs to be
	 */
	public static GridPane centreInPane(Node n) {
		// Create a new grid pane
		GridPane g = new GridPane();
		// Centre the grid pane in its parent
		g.setAlignment(Pos.CENTER);
		// Add padding
		g.setHgap(10);
		g.setVgap(10);
		g.setPadding(new Insets(5, 5, 5, 5));
		// Add the node into the grid pane
		g.add(n, 0, 0);
		// Return the grid pane
		return g;
	}
	
}
