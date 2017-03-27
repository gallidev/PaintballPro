package rendering;

import enums.GameMode;
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

/**
 * A class that extends a <code>SubScene</code> to implement a head up display for the game. A head up display consists of a dark gray horizontal bar at the bottom of the view. A game timer is displayed in the middle. On both sides of the timer are red and blue team score counters. The team that the user is on will have its score counter displayed to the left of the timer.<br><br>
 * The score counters have different behaviour and look based on the gamemode being played. In a team match, the counters increment based on amount of eliminations during the game.<br><br>
 * For Capture the Flag, the counters increment for the amount of flags returned to the team base. The status of the flag (on the ground or carried by a player) is indicated by a glowing effect of the flag icon of a team that is currently carrying the flag.
 *
 * @author Artur Komoter
 */
public class HeadUpDisplay extends SubScene {
	static BorderPane VIEW = new BorderPane();
	private final Label timer = new Label("0:00"),
			redScore = new Label("0"),
			blueScore = new Label("0");
	private final GUIManager guiManager;
	private ImageView redTeamFlag, blueTeamFlag;
	private DropShadow flagGlow = new DropShadow(VIEW.getWidth() / 64, Color.WHITE);

	/**
	 * Creates a head up display that is displayed during a game.
	 *
	 * @param guiManager GUI Manager singleton
	 * @param gameMode   Gamemode of the game instance to be played
	 * @param playerTeam Team that the user is on
	 */
	HeadUpDisplay(GUIManager guiManager, GameMode gameMode, TeamEnum playerTeam) {
		super(VIEW, guiManager.width, guiManager.height);
		this.guiManager = guiManager;
		VIEW.setStyle("-fx-background-color: transparent");
		VIEW.getStylesheets().add("styles/menu.css");
		setScaleX(1024 / guiManager.width);
		setScaleY(576 / guiManager.height);

		Circle redTeamCircle = null, blueTeamCircle = null;
		if (gameMode == GameMode.TEAM_MATCH) {
			redTeamCircle = new Circle(VIEW.getWidth() / 64, Color.RED);
			blueTeamCircle = new Circle(VIEW.getWidth() / 64, Color.BLUE);

			redTeamCircle.setStroke(Color.WHITE);
			redTeamCircle.setStrokeWidth(3);
			blueTeamCircle.setStroke(Color.WHITE);
			blueTeamCircle.setStrokeWidth(3);
		} else {
			redTeamFlag = new ImageView(ImageFactory.getHudFlagImage(TeamEnum.RED));
			redTeamFlag.setFitWidth(VIEW.getWidth() / 32);
			redTeamFlag.setFitHeight(VIEW.getWidth() / 32);
			blueTeamFlag = new ImageView(ImageFactory.getHudFlagImage(TeamEnum.BLUE));
			blueTeamFlag.setFitWidth(VIEW.getWidth() / 32);
			blueTeamFlag.setFitHeight(VIEW.getWidth() / 32);
			flagGlow.setSpread(0.25);
		}

		String textStyle = "-fx-font-size: 16pt; -fx-text-fill: white";
		timer.setStyle(textStyle);
		redScore.setStyle(textStyle);
		blueScore.setStyle(textStyle);

		HBox statusBar;
		if (playerTeam == TeamEnum.RED) {
			if (gameMode == GameMode.TEAM_MATCH)
				statusBar = new HBox(VIEW.getWidth() / 32, redScore, redTeamCircle, timer, blueTeamCircle, blueScore);
			else
				statusBar = new HBox(VIEW.getWidth() / 32, redScore, redTeamFlag, timer, blueTeamFlag, blueScore);
		} else if (gameMode == GameMode.TEAM_MATCH)
			statusBar = new HBox(VIEW.getWidth() / 32, blueScore, blueTeamCircle, timer, redTeamCircle, redScore);
		else
			statusBar = new HBox(VIEW.getWidth() / 32, blueScore, blueTeamFlag, timer, redTeamFlag, redScore);
		statusBar.setPrefHeight(VIEW.getHeight() / 8);
		statusBar.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75)");
		statusBar.setAlignment(Pos.CENTER);

		VIEW.setBottom(statusBar);
	}

	/**
	 * Update the score counters of both teams.
	 *
	 * @param redScore  New score of the red team
	 * @param blueScore New score of the blue team
	 */
	void setScore(int redScore, int blueScore) {
		if (Integer.parseInt(this.redScore.getText()) < redScore || Integer.parseInt(this.blueScore.getText()) < blueScore)
			guiManager.getAudioManager().playSFX(guiManager.getAudioManager().sfx.splat, (float) 0.6);

		this.redScore.setText(String.valueOf(redScore));
		this.blueScore.setText(String.valueOf(blueScore));
	}

	/**
	 * Toggle the flag status of a team in Capture the Flag gamemode between carrying a flag and dropping a flag.
	 *
	 * @param team Team that should have its flag status toggled
	 */
	public void toggleFlagStatus(TeamEnum team) {
		ImageView flag = (team == TeamEnum.RED ? redTeamFlag : blueTeamFlag);
		flag.setEffect(flag.getEffect() == null ? flagGlow : null);
	}

	/**
	 * Update game timer.
	 *
	 * @param time New game time in seconds
	 */
	public void tick(int time) {
		if (time < 0)
			return;
		int minutes = (time % 3600) / 60, seconds = time % 60;
		timer.setText(String.format("%02d:%02d", minutes, seconds));

	}
}
