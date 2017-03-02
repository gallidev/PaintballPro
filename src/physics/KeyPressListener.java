package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
<<<<<<< HEAD


public class KeyPressListener implements EventHandler<KeyEvent>{

	private InputHandler inputHandler;


	public KeyPressListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	@Override
    public void handle(KeyEvent event) {
		switch (event.getCode()) {
        case UP:    inputHandler.setUp(true); break;
        case DOWN:  inputHandler.setDown(true); break;
        case LEFT:  inputHandler.setLeft(true); break;
        case RIGHT: inputHandler.setRight(true); break;
        case W:     inputHandler.setUp(true); break;
        case S:     inputHandler.setDown(true); break;
        case A:     inputHandler.setLeft(true); break;
        case D:     inputHandler.setRight(true); break;
		default:
			break;
    }
        event.consume();
    }
=======
import players.GeneralPlayer;
import rendering.Renderer;

public class KeyPressListener implements EventHandler<KeyEvent>
{
	private GeneralPlayer player;

	public KeyPressListener(GeneralPlayer player)
	{
		this.player = player;
	}

	@Override
	public void handle(KeyEvent event)
	{
		if(Renderer.getPauseMenuState())
		{
			switch(event.getCode())
			{
				case ESCAPE:
					Renderer.togglePauseMenu();
					break;
				default:
					break;
			}
		}
		else
		{
			switch(event.getCode())
			{
				case UP:
					player.setUp(true);
					break;
				case DOWN:
					player.setDown(true);
					break;
				case LEFT:
					player.setLeft(true);
					break;
				case RIGHT:
					player.setRight(true);
					break;
				case W:
					player.setUp(true);
					break;
				case S:
					player.setDown(true);
					break;
				case A:
					player.setLeft(true);
					break;
				case D:
					player.setRight(true);
					break;
				case P:
					System.out.println(Math.round(player.getLayoutX() / 64) + ", " + Math.round(player.getLayoutY() / 64));
					break;
				case ESCAPE:
					Renderer.togglePauseMenu();
					break;
				default:
					break;
			}
		}
		event.consume();
	}
>>>>>>> f6887eef8fffa5fe0a94cae7fc5ce8145b9556b4
}
