package physics;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import players.GeneralPlayer;
import rendering.Renderer;

public class MouseListener implements EventHandler<MouseEvent>{

	private InputHandler inputHandler;

	public MouseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	@Override
    public void handle(MouseEvent event) {
<<<<<<< HEAD
		double newX = event.getX();
		double newY = event.getY();
		inputHandler.setMouseX(newX);
		inputHandler.setMouseY(newY);
		inputHandler.setShoot(event.isPrimaryButtonDown());
=======
		if(!Renderer.getPauseMenuState())
		{
			double newX = event.getX();
			double newY = event.getY();
			player.setMX(newX);
			player.setMY(newY);
			player.setShoot(event.isPrimaryButtonDown());
		}
>>>>>>> f6887eef8fffa5fe0a94cae7fc5ce8145b9556b4
    }
}
