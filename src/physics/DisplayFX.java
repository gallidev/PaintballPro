package physics;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DisplayFX extends Application
{
	private TestGame game;
	private Player player;
	private Image playerImage;
	private double lastAngle = 0.0;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		Canvas canvas = new Canvas(1024, 1024);
		Group root = new Group(canvas);
		Scene scene = new Scene(root, 640, 480, Color.WHITE);
		scene.setCursor(Cursor.CROSSHAIR);
		stage.setTitle("Paintball Pro");
		
		game = new TestGame();
		player = game.getPlayer();
		playerImage = player.getImage();
		
		scene.setOnKeyPressed(new KeyPressListener(game));
		scene.setOnKeyReleased(new KeyReleaseListener(game));
		scene.setOnMouseMoved(new MouseListener(game));
		scene.setOnMouseDragged(new MouseListener(game));
		scene.setOnMousePressed(new MouseListener(game));
		Pane pane = new Pane();
		
		
		AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
            	GraphicsContext gc = canvas.getGraphicsContext2D();
        		player.tick();
        		double deltax = (player.getMX() - 10) - player.x;
        		double deltay = player.y - (player.getMY() - 10) ;
        		double deltaz = Math.sqrt(deltax * deltax + deltay * deltay);
        		double angle = lastAngle;
        		if (deltaz > 5){
        			angle = Math.atan2(deltax, deltay);
        		}
        		
        		player.setAngle(angle);
        		lastAngle = angle;
        		
        		ImageView iv = new ImageView(playerImage);
        		iv.setRotate(Math.toDegrees(angle));
        		iv.setSmooth(true);
        		iv.setTranslateX(player.x);
        		iv.setTranslateY(player.y);
        		HBox box = new HBox();
        		box.getChildren().add(iv);
        		pane.getChildren().clear();
        		pane.getChildren().add(box);
        		
        		gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, 1024, 1024); //TEMP
        		ArrayList<Bullet> bullets = (ArrayList<Bullet>) player.getBullets();
        		for(int i=0; i<bullets.size(); i++){
        			gc.setFill(Color.RED);
        			gc.fillOval((int)bullets.get(i).x, (int)bullets.get(i).y, 4, 4);
        		}
            }

        };
        gameLoop.start();
        
        root.getChildren().add(pane);
        stage.setScene(scene);
		
		stage.show();
	}
}
