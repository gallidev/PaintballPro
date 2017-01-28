package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyReleaseListener implements EventHandler<KeyEvent>{
	
	private Player player;
	
	public KeyReleaseListener(Player player){
		this.player = player;
	}
	@Override
    public void handle(KeyEvent event) {
		switch (event.getCode()) {
        case UP:    player.setUp(false); break;
        case DOWN:  player.setDown(false); break;
        case LEFT:  player.setLeft(false); break;
        case RIGHT: player.setRight(false); break;
        case W:     player.setUp(false); break;
        case S:     player.setDown(false); break;
        case A:     player.setLeft(false); break;
        case D:     player.setRight(false); break;
		default:
			break;
    }
        event.consume();
    }
}
