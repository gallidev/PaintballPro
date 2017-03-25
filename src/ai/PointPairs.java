package ai;

import javafx.geometry.Point2D;

/**
 * The class representing a pair of Point2Ds
 * @author Sivarjuen Ravichandran
 */
public class PointPairs {

    private Point2D fst, snd;

    /**
     * Instantiate the pair with two Point2Ds of the given coordinates
     * @param x x-coordinate of the first point
     * @param y y-coorinate of the first point
     * @param tx x-coordinate of the second point
     * @param ty y-coordinate of the second point
     */
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
