package physics;

import javafx.scene.image.ImageView;
import logic.GameObject;

public class Flag extends ImageView implements GameObject{

    private boolean captured = false;

    public Flag(){
        //setImage
    }

    public void setCaptured(boolean b){
        captured = b;
    }

    public boolean isCaptured(){
        return captured;
    }

    @Override
    public void tick(){

    }
}
