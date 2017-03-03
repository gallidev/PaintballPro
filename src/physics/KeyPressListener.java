package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
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
		if(Renderer.getSettingsMenuState())
		{
			switch(event.getCode())
			{
				case ESCAPE:
					Renderer.toggleSettingsMenu();
					break;
				default:
					break;
			}
		}
		else if(Renderer.getPauseMenuState())
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
}
