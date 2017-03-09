package rendering;

import enums.Menu;
import enums.Team;
import gui.GUIManager;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class HeadUpDisplay extends SubScene
{
	static BorderPane view = new BorderPane();
	private final Label timer = new Label("3:00"),
			redScore = new Label("0"),
			blueScore = new Label("0");
	private final GUIManager guiManager;

	HeadUpDisplay(GUIManager guiManager, Team playerTeam)
	{
		super(view, Renderer.view.getWidth(), Renderer.view.getHeight());
		this.guiManager = guiManager;
		view.setStyle("-fx-background-color: transparent");
		view.getStylesheets().add("styles/menu.css");
//		view.setScaleX(Renderer.view.getScaleX());
//		view.setScaleY(Renderer.view.getScaleY());

		Circle redTeam = new Circle(view.getWidth() / 64, Color.RED),
				blueTeam = new Circle(view.getWidth() / 64, Color.BLUE);
		redTeam.setStroke(Color.WHITE);
		redTeam.setStrokeWidth(3);
		blueTeam.setStroke(Color.WHITE);
		blueTeam.setStrokeWidth(3);

		String textStyle = "-fx-font-size: 16pt; -fx-text-fill: white";
		timer.setStyle(textStyle);
		redScore.setStyle(textStyle);
		blueScore.setStyle(textStyle);

		HBox statusBar;
		if(playerTeam == Team.RED)
			statusBar = new HBox(view.getWidth() / 32, redScore, redTeam, timer, blueTeam, blueScore);
		else
			statusBar = new HBox(view.getWidth() / 32, blueScore, blueTeam, timer, redTeam, redScore);
		statusBar.setPrefHeight(view.getHeight() / 8);
		statusBar.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75)");
		statusBar.setAlignment(Pos.CENTER);
		statusBar.setCache(true);
		statusBar.setCacheHint(CacheHint.SCALE);

		view.setBottom(statusBar);
	}

	void incrementScore(Team team, int amount)
	{
		(team == Team.RED ? redScore : blueScore).setText(String.valueOf(Integer.parseInt((team == Team.RED ? redScore : blueScore).getText()) + amount));
	}

	void tick()
	{
		if(timer.getText().equals("BOOM!"))
			return;
		if(timer.getText().equals("0:00"))
		{
			guiManager.transitionTo(Menu.EndGame, redScore.getText() + "," + blueScore.getText(), Renderer.player.getTeam());
			return;
		}
		String[] timeParse = timer.getText().split(":");
		if(timeParse[1].equals("00"))
			timer.setText(String.valueOf(Integer.parseInt(timeParse[0]) - 1) + ":59");
		else
			timer.setText(String.format("%s:%02d", timeParse[0], Integer.parseInt(timeParse[1]) - 1));

	}
}
