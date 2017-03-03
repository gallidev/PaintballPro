package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
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
		if(!Renderer.getPauseMenuState() && !Renderer.getSettingsMenuState())
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
}
