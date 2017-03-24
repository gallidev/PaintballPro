package rendering;

import enums.GameMode;
import enums.Menu;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static gui.GUIManager.renderer;

public class HeadUpDisplay extends SubScene
{
	static BorderPane view = new BorderPane();
	private final Label timer = new Label("0:00"),
			redScore = new Label("0"),
			blueScore = new Label("0");
	private final GUIManager guiManager;

	HeadUpDisplay(GUIManager guiManager, GameMode gameMode, TeamEnum playerTeam)
	{
		super(view, guiManager.width, guiManager.height);
		this.guiManager = guiManager;
		view.setStyle("-fx-background-color: transparent");
		view.getStylesheets().add("styles/menu.css");
		setScaleX(1024 / guiManager.width);
		setScaleY(576 / guiManager.height);

		Circle redTeamCircle = null, blueTeamCircle = null;
		ImageView redTeamFlag = null, blueTeamFlag = null;
		if(gameMode == GameMode.ELIMINATION)
		{
			redTeamCircle = new Circle(view.getWidth() / 64, Color.RED);
			blueTeamCircle = new Circle(view.getWidth() / 64, Color.BLUE);

			redTeamCircle.setStroke(Color.WHITE);
			redTeamCircle.setStrokeWidth(3);
			blueTeamCircle.setStroke(Color.WHITE);
			blueTeamCircle.setStrokeWidth(3);
		}
		else
		{
			redTeamFlag = new ImageView(ImageFactory.getHudFlagImage(TeamEnum.RED));
			redTeamFlag.setFitWidth(view.getWidth() / 32);
			redTeamFlag.setFitHeight(view.getWidth() / 32);
			DropShadow glow = new DropShadow(view.getWidth() / 64, Color.RED);
			redTeamFlag.setEffect(glow);
			blueTeamFlag = new ImageView(ImageFactory.getHudFlagImage(TeamEnum.BLUE));
			blueTeamFlag.setFitWidth(view.getWidth() / 32);
			blueTeamFlag.setFitHeight(view.getWidth() / 32);
		}

		String textStyle = "-fx-font-size: 16pt; -fx-text-fill: white";
		timer.setStyle(textStyle);
		redScore.setStyle(textStyle);
		blueScore.setStyle(textStyle);

		HBox statusBar;
		if(playerTeam == TeamEnum.RED)
		{
			if(gameMode == GameMode.ELIMINATION)
				statusBar = new HBox(view.getWidth() / 32, redScore, redTeamCircle, timer, blueTeamCircle, blueScore);
			else
				statusBar = new HBox(view.getWidth() / 32, redScore, redTeamFlag, timer, blueTeamFlag, blueScore);
		}
		else
		{
			if(gameMode == GameMode.ELIMINATION)
				statusBar = new HBox(view.getWidth() / 32, blueScore, blueTeamCircle, timer, redTeamCircle, redScore);
			else
				statusBar = new HBox(view.getWidth() / 32, blueScore, blueTeamFlag, timer, redTeamFlag, redScore);
		}
		statusBar.setPrefHeight(view.getHeight() / 8);
		statusBar.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75)");
		statusBar.setAlignment(Pos.CENTER);

		view.setBottom(statusBar);
	}

	void setScore(TeamEnum team, int score)
	{
		if (Integer.parseInt((team == TeamEnum.RED ? redScore : blueScore).getText()) < score) {
			//guiManager.getAudioManager().playSFX(guiManager.getAudioManager().sfx.splat, (float) 0.6);
		}

		(team == TeamEnum.RED ? redScore : blueScore).setText(String.valueOf(score));
	}

	public void tick(int time)
	{
		if(time < 0)
			return;
		int minutes = (time % 3600) / 60, seconds = time % 60;
		timer.setText(String.format("%02d:%02d", minutes, seconds));

	}

	public void endGame(int red, int blue)
	{
		guiManager.transitionTo(Menu.EndGame, red + "," + blue, (renderer.cPlayer == null ? renderer.player.getTeam() : renderer.cPlayer.getTeam()));

	}
}
