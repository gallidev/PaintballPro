package physics;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DisplayFX extends Application
{
	private Player player;
	
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
		
		player = new Player(100, 100);
		
		KeyPressListener kp = new KeyPressListener(player);
		KeyReleaseListener kl = new KeyReleaseListener(player);
		MouseListener ml = new MouseListener(player);
		
		scene.setOnKeyPressed(kp);
		scene.setOnKeyReleased(kl);
		scene.setOnMouseMoved(ml);
		scene.setOnMouseDragged(ml);
		scene.setOnMousePressed(ml);
		scene.setOnMouseReleased(ml);
		Pane pane = new Pane();
		
		
		AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
            	GraphicsContext gc = canvas.getGraphicsContext2D();
        		player.tick();
        		
        		HBox box = new HBox();
        		box.getChildren().add(player);
        		pane.getChildren().clear();
        		pane.getChildren().add(box);
        		
        		gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, 1024, 1024); //TEMP
                
        		ArrayList<Bullet> bullets = (ArrayList<Bullet>) player.getBullets();
        		for(int i=0; i<bullets.size(); i++){
        			gc.setFill(Color.RED);
        			gc.fillOval((int)bullets.get(i).getX(), (int)bullets.get(i).getY(), 4, 4);
        		}
            }

        };
        
        gameLoop.start();
        
        root.getChildren().add(pane);
        stage.setScene(scene);
		
		stage.show();
	}
}
