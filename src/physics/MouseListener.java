package physics;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import networkingInterfaces.ClientPlayerOld;
import players.GeneralPlayer;
import players.PhysicsClientPlayer;

public class MouseListener implements EventHandler<MouseEvent>{

	private InputHandler inputHandler;

	public MouseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	@Override
    public void handle(MouseEvent event) {
		double newX = event.getX();
		double newY = event.getY();
		inputHandler.setMouseX(newX);
		inputHandler.setMouseY(newY);
		inputHandler.setShoot(event.isPrimaryButtonDown());
    }
}
