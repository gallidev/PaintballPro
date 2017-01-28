package physics;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseListener implements EventHandler<MouseEvent>{
	
	private Player player;
	
	public MouseListener(Player player){
		this.player = player;
	}
	@Override
    public void handle(MouseEvent event) {
		player.setMX(event.getX());
		player.setMY(event.getY());
		player.setShoot(event.isPrimaryButtonDown());
    }
}
