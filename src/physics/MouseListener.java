package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseListener implements EventHandler<MouseEvent>{

	private InputHandler inputHandler;

	public MouseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	@Override
    public void handle(MouseEvent event) {

		if(!GUIManager.renderer.getPauseMenuState()&& !GUIManager.renderer.getSettingsMenuState())
		{
				int newX = (int) event.getX();
				int newY = (int) event.getY();
				inputHandler.setMouseX(newX);
				inputHandler.setMouseY(newY);
				inputHandler.setShoot(event.isPrimaryButtonDown());
		}
    }
}
