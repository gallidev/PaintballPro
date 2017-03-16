package rendering;

import enums.Menu;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HeadUpDisplay extends SubScene
{
	static BorderPane view = new BorderPane();
	private final Label timer = new Label("3:00"),
			redScore = new Label("0"),
			blueScore = new Label("0");
	private final GUIManager guiManager;

	HeadUpDisplay(GUIManager guiManager, TeamEnum playerTeam)
	{
		super(view, guiManager.getStage().getWidth(), guiManager.getStage().getHeight());
		this.guiManager = guiManager;
		view.setStyle("-fx-background-color: transparent");
		view.getStylesheets().add("styles/menu.css");

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
		if(playerTeam == TeamEnum.RED)
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

	public void setScore(TeamEnum team, int score)
	{
		(team == TeamEnum.RED ? redScore : blueScore).setText(String.valueOf(score));
	}

	public void tick(int time)
	{
		if(time < 0)
			return;
		int minutes = (time % 3600) / 60, seconds = time % 60;
		timer.setText(String.format("%02d:%02d", minutes, seconds));

	}
	
	public void setWinner(int red, int blue){
		guiManager.transitionTo(Menu.EndGame, red + "," + blue, (GUIManager.renderer.cPlayer == null ? GUIManager.renderer.player.getTeam() : GUIManager.renderer.cPlayer.getTeam()));

	}
}
