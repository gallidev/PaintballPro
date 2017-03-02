package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
<<<<<<< HEAD

public class KeyReleaseListener implements EventHandler<KeyEvent>{

	private InputHandler inputHandler;

	public KeyReleaseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}


	@Override
    public void handle(KeyEvent event) {
		switch (event.getCode()) {
        case UP:    inputHandler.setUp(false); break;
        case DOWN:  inputHandler.setDown(false); break;
        case LEFT:  inputHandler.setLeft(false); break;
        case RIGHT: inputHandler.setRight(false); break;
        case W:     inputHandler.setUp(false); break;
        case S:     inputHandler.setDown(false); break;
        case A:     inputHandler.setLeft(false); break;
        case D:     inputHandler.setRight(false); break;
		default:
			break;
    }
        event.consume();
    }
=======
import networking.interfaces.ClientPlayerOld;
import players.GeneralPlayer;
import rendering.Renderer;

public class KeyReleaseListener implements EventHandler<KeyEvent>
{
	private GeneralPlayer player;

	public KeyReleaseListener(GeneralPlayer player)
	{
		this.player = player;
	}

	@Override
	public void handle(KeyEvent event)
	{
		if(!Renderer.getPauseMenuState())
			switch(event.getCode())
			{
				case UP:
					player.setUp(false);
					break;
				case DOWN:
					player.setDown(false);
					break;
				case LEFT:
					player.setLeft(false);
					break;
				case RIGHT:
					player.setRight(false);
					break;
				case W:
					player.setUp(false);
					break;
				case S:
					player.setDown(false);
					break;
				case A:
					player.setLeft(false);
					break;
				case D:
					player.setRight(false);
					break;
				default:
					break;
			}
		event.consume();
	}
>>>>>>> f6887eef8fffa5fe0a94cae7fc5ce8145b9556b4
}
