package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Class containing helper methods for configuring menu controls
 *
 * @author Jack Hughes
 */
public class MenuControls {

	/**
	 * Method to centre a node in a GridPane cell
	 *
	 * @param n node to centre
	 * @return GridPane that should be placed where the node needs to be
	 */
	public static GridPane centreInPane(Node n) {
		return centreInPane(n, MenuControls.scaleByResolution(5));
	}

	/**
	 * Method to centre a node in a GridPane cell
	 *
	 * @param node    node to centre
	 * @param padding padding for pane
	 * @return GridPane that should be placed where the node needs to be
	 */
	public static GridPane centreInPane(Node node, Insets padding) {
		// Create a new grid pane
		GridPane gridPane = new GridPane();
		// Centre the grid pane in its parent
		gridPane.setAlignment(Pos.CENTER);
		// Add padding
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(padding);
		// Add the node into the grid pane
		gridPane.add(node, 0, 0);
		// Return the grid pane
		return gridPane;
	}

	/**
	 * Method to scale padding using the user's chosen resolution
	 * @param padding padding for the default resolution
	 * @return insets for the scaled resolution padding
	 */
	public static Insets scaleByResolution(int padding) {
		int defaultWidth = Integer.parseInt(UserSettings.possibleResolutions[2].split("x")[0]);
		int chosenWidth = Integer.parseInt(GUIManager.getUserSettings().getResolution().split("x")[0]);
		double scaledPadding = (double)padding * (((double)chosenWidth - (double)defaultWidth) / (double)defaultWidth);
		if (scaledPadding < 0)
			scaledPadding = 0;
		if (scaledPadding > (padding * 2))
			scaledPadding = padding * 2;
		return new Insets(scaledPadding);
	}

}
