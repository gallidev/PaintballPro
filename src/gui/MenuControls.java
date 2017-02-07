package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MenuControls {
	public static GridPane centreInPane(Node n) {
		GridPane g = new GridPane();
		g.setAlignment(Pos.CENTER);
		g.setHgap(10);
		g.setVgap(10);
		g.setPadding(new Insets(5, 5, 5, 5));
		g.add(n, 0, 0);
		return g;
	}
}
