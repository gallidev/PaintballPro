package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyPressListener implements EventHandler<KeyEvent>{
	
	private TestGame game;
	
	public KeyPressListener(TestGame game){
		this.game = game;
	}
	@Override
    public void handle(KeyEvent event) {
		switch (event.getCode()) {
        case UP:    game.up = true; break;
        case DOWN:  game.down = true; break;
        case LEFT:  game.left  = true; break;
        case RIGHT: game.right  = true; break;
        case W:     game.up = true; break;
        case S:     game.down = true; break;
        case A:     game.left  = true; break;
        case D:     game.right  = true; break;
        case SPACE: game.shoot = true; break;
		default:
			break;
    }
        event.consume();
    }
}
