package physics;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import players.GeneralPlayer;
import rendering.Renderer;

public class MouseListener implements EventHandler<MouseEvent>{

	private GeneralPlayer player;

	public MouseListener(GeneralPlayer player){
		this.player = player;
	}

	@Override
    public void handle(MouseEvent event) {
		if(!Renderer.getPauseMenuState())
		{
			double newX = event.getX();
			double newY = event.getY();
			player.setMX(newX);
			player.setMY(newY);
			player.setShoot(event.isPrimaryButtonDown());
		}
    }
}
