/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.util.GeometryUtils;

/**
 * Moves to Sprite linearly until it reaches the given point
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MoveToPointSpriteMainTask implements MotionSpriteMainTask {

    private ModelPoint target;
    private boolean center;
    private double speed;
    private ModelPoint[] path;

    public MoveToPointSpriteMainTask(ModelPoint point) {
        this(point, Double.NaN, false);
    }

    public MoveToPointSpriteMainTask(ModelPoint point, double speed) {
        this(point, speed, false);
    }

    public MoveToPointSpriteMainTask(ModelPoint point, double speed, boolean center) {
        //stick target to grid, and point the center of the cell (thas (0.5,0.5) of the cell)
//        point = new ModelPoint(((int) point.getX()) + 0.5, ((int) point.getY()) + 0.5);
        point = new ModelPoint(((int) point.getX()), ((int) point.getY()));
        this.target = point;
        this.center = center;
        this.speed = speed;
    }

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        ModelPoint targetPoint = getTargetPoint(sprite);
        final ModelPoint sp = sprite.getLocation();
        if (!sp.equals(targetPoint)) {
            if (!Double.isNaN(speed)) {
                sprite.setSpeed(speed);
            }
            ModelPoint stepPoint = GeometryUtils.nextLinePoint(sp, targetPoint, sprite.getSpeed());
            sprite.setLocation(stepPoint);
            final double velAngle = sp.getAngleTo(targetPoint);
            if (stepPoint.equals(targetPoint)) {
                return false;
            } else {
                sprite.setDirection(velAngle);
            }
            path = new ModelPoint[]{sprite.getLocation(), getTargetPoint(sprite)};
            return true;
        }
        path = null;
        return false;
    }

    @Override
    public boolean isMoving() {
        return path != null;
    }

    private ModelPoint getTargetPoint(Sprite sprite) {
        if (!center) {
            return target;
        } else {
            return new ModelPoint(target.getX() - sprite.getWidth() / 2, target.getY() - sprite.getHeight() / 2);
        }
    }

    @Override
    public ModelPoint[] getMovePath() {
        return path;
    }

    @Override
    public String toString() {
        return "LinearMoveTo";
//        return center? ("GOTO_CENTER(" + target + ")"):("GOTO(" + target + ")");
    }
}
