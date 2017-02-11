package physics;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import networkingInterfaces.ClientPlayer;

public class MouseListener implements EventHandler<MouseEvent>{
	
	private Player player;
	
	public MouseListener(Player player){
		this.player = player;
	}
	
	@Override
    public void handle(MouseEvent event) {
		double newX = event.getX();
		double newY = event.getY();
		player.setMX(newX);
		player.setMY(newY);
		player.setShoot(event.isPrimaryButtonDown());
    }
}
