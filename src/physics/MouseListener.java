package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MouseListener implements EventHandler<MouseEvent>{
	
	private TestGame game;
	
	public MouseListener(TestGame game){
		this.game = game;
	}
	@Override
    public void handle(MouseEvent event) {
		game.mx = event.getX();
		game.my = event.getY();
		game.shoot = event.isPrimaryButtonDown();
    }
}
