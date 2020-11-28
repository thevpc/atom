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
public class MoveSpriteMainTask implements MotionSpriteMainTask {
//    private double validDirection = Double.NaN;
//    private double lastDirection = Double.NaN;

    private boolean moving;

    public MoveSpriteMainTask() {
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public boolean nextFrame(SceneEngine sceneEngine, Sprite sprite) {
        double speed = sprite.getSpeed();
        moving = speed != 0;
        if (speed == 0) {
            return true;
        }
        ModelPoint currentPoint = GeometryUtils.nextLinePoint(sprite.getLocation(),
                sprite.getDirection(), speed);
        sprite.setLocation(currentPoint);
        return true;
    }
}
