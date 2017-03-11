package physics;

import javafx.scene.image.ImageView;
import logic.GameObject;

public class Powerup extends ImageView implements GameObject {

    private boolean taken = false;
    private int duration;

    public Powerup(){
        //setImage
    }

    public void setTaken(boolean b){
        taken = b;
    }

    public boolean isTaken(){
        return taken;
    }

    @Override
    public void tick(){

    }
}
