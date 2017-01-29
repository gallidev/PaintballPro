package rendering;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;

class CameraControl
{
	private boolean up = false, down = false, left = false, right = false;

	CameraControl(Scene scene, Group root)
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

		new AnimationTimer() {
			@Override
			public void handle(long now)
			{
				if(up)
					root.setTranslateY(root.getTranslateY() + 4);
				if(down)
					root.setTranslateY(root.getTranslateY() - 4);
				if(left)
					root.setTranslateX(root.getTranslateX() + 4);
				if(right)
					root.setTranslateX(root.getTranslateX() - 4);
			}
		}.start();
	}
}
