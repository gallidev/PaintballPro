package rendering;

import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class HeadUpDisplay extends SubScene
{
	private static BorderPane view = new BorderPane();

	HeadUpDisplay()
	{
		super(view, 1024, 576);
		view.setStyle("-fx-background-color: transparent");

		Circle redTeam = new Circle(view.getWidth() / 64, Color.RED),
				blueTeam = new Circle(view.getWidth() / 64, Color.BLUE);
		redTeam.setStroke(Color.WHITE);
		redTeam.setStrokeWidth(3);
		blueTeam.setStroke(Color.WHITE);
		blueTeam.setStrokeWidth(3);

		Label timer = new Label("3:00"),
				redScore = new Label("0"),
				blueScore = new Label("0");

		String textStyle = "-fx-font-size: 16pt; -fx-text-fill: white";
		timer.setStyle(textStyle);
		redScore.setStyle(textStyle);
		blueScore.setStyle(textStyle);

		HBox statusBar = new HBox(view.getWidth() / 32, redScore, redTeam, timer, blueTeam, blueScore);
		statusBar.setPrefHeight(view.getHeight() / 8);
		statusBar.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75)");
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setCache(true);

		view.setBottom(statusBar);
	}
}
