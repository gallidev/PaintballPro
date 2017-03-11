package ai;

import javafx.geometry.Point2D;

public class PointPairs {

    private Point2D fst, snd;

    public PointPairs(double x, double y, double tx, double ty){
        fst = new Point2D((int)x, (int)y);
        snd = new Point2D((int)tx, (int)ty);
    }

    @Override
    public int hashCode() {
        return (int)((fst.getX() * snd.getX()) + (fst.getY() * snd.getY()));
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PointPairs) {
            PointPairs o = (PointPairs) other;

            return (o.fst.getX() == fst.getX()) && (o.snd.getX() == snd.getX()) && (o.fst.getY() == fst.getY()) && (o.snd.getY() == snd.getY());
        }
        return false;
    }
}
