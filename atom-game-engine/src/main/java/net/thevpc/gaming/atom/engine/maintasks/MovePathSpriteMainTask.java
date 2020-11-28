/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.util.GeometryUtils;

/**
 * move the sprite according to its own direction and speed
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MovePathSpriteMainTask implements MotionSpriteMainTask {
//    private double validDirection = Double.NaN;
//    private double lastDirection = Double.NaN;
    private boolean moving;
    private ModelPoint[] path;
    private boolean stopOnNoSpeed = false;

    public MovePathSpriteMainTask() {
        this(false);
    }

    public MovePathSpriteMainTask(boolean stopOnNoSpeed) {
        this.stopOnNoSpeed = stopOnNoSpeed;
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    public boolean nextFrame(SceneEngine sceneEngine, Sprite sprite) {
        double speed = sprite.getSpeed();
        moving = speed != 0;
        if (speed == 0) {
            path = null;
            if (stopOnNoSpeed) {
                return false;
            }
            return true;
        }
        ModelPoint currentPoint = GeometryUtils.nextLinePoint(sprite.getLocation(), sprite.getDirection(), speed);
        sprite.setLocation(currentPoint);
        ModelPoint pathPoint = GeometryUtils.nextLinePoint(currentPoint, sprite.getDirection(), 5 * sprite.getSpeed());
        path = new ModelPoint[]{currentPoint, pathPoint};
        return true;
    }

    public ModelPoint[] getMovePath() {
        return path;
    }

}
