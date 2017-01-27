package rendering;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

class CameraControl
{
	private boolean up = false, down = false, left = false, right = false;

	CameraControl(Scene scene, ScrollPane view)
	{
		scene.setOnKeyPressed(event ->
		{
			switch(event.getCode())
			{
				case W:
					up = true;
					break;
				case S:
					down = true;
					break;
				case A:
					left = true;
					break;
				case D:
					right = true;
					break;
			}
		});

		scene.setOnKeyReleased(event ->
		{
			switch(event.getCode())
			{
				case W:
					up = false;
					break;
				case S:
					down = false;
					break;
				case A:
					left = false;
					break;
				case D:
					right = false;
					break;
			}
		});

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now)
			{
				if(up)
					view.setVvalue(view.getVvalue() - 0.01);
				if(down)
					view.setVvalue(view.getVvalue() + 0.01);
				if(left)
					view.setHvalue(view.getHvalue() - 0.01);
				if(right)
					view.setHvalue(view.getHvalue() + 0.01);
			}
		};
		timer.start();
	}
}
