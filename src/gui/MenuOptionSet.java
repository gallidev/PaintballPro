package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MenuOptionSet {
	public static GridPane optionSetToGridPane(MenuOption[] opts) {
		GridPane gp = new GridPane();
				
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));
		
		for (int i = 0; i < opts.length; i++) {
			Button btn = new Button();
			btn.setText(opts[i].getName());
			btn.setOnAction(opts[i].getHandler());
			btn.setMaxWidth(Double.MAX_VALUE);
			gp.add(btn, 0, i + 1);
		}
		
		return gp;
	}
}
