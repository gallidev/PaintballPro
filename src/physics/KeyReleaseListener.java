package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyReleaseListener implements EventHandler<KeyEvent>{
	
	private TestGame game;
	
	public KeyReleaseListener(TestGame game){
		this.game = game;
	}
	@Override
    public void handle(KeyEvent event) {
		switch (event.getCode()) {
        case UP:    game.up = false; break;
        case DOWN:  game.down = false; break;
        case LEFT:  game.left  = false; break;
        case RIGHT: game.right  = false; break;
        case W:     game.up = false; break;
        case S:     game.down = false; break;
        case A:     game.left  = false; break;
        case D:     game.right  = false; break;
        case SPACE: game.shoot = false; break;
		default:
			break;
    }
        event.consume();
    }
}
