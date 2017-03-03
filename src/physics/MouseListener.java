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
		if(!Renderer.getPauseMenuState())
		{
				double newX = event.getX();
				double newY = event.getY();
				inputHandler.setMouseX(newX);
				inputHandler.setMouseY(newY);
				inputHandler.setShoot(event.isPrimaryButtonDown());
		}
    }
}
