package physics;

import javafx.scene.image.ImageView;
import logic.GameObject;

public class Powerup extends ImageView implements GameObject {

    private boolean taken = false;
    private int duration;

    public Powerup(){
        //setImage
    }

    public boolean isTaken(){
        return taken;
    }

    public void setTaken(boolean b){
        taken = b;
    }

    @Override
    public void tick(){

    }

    @Override
    public void interact()
    {

    }
}
