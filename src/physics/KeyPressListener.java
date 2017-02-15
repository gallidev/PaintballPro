package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyPressListener implements EventHandler<KeyEvent>{

	private GeneralPlayer player;

	public KeyPressListener(GeneralPlayer player){
		this.player = player;
	}

	@Override
    public void handle(KeyEvent event) {
		switch (event.getCode()) {
        case UP:    player.setUp(true); break;
        case DOWN:  player.setDown(true); break;
        case LEFT:  player.setLeft(true); break;
        case RIGHT: player.setRight(true); break;
        case W:     player.setUp(true); break;
        case S:     player.setDown(true); break;
        case A:     player.setLeft(true); break;
        case D:     player.setRight(true); break;
		default:
			break;
    }
        event.consume();
    }
}
