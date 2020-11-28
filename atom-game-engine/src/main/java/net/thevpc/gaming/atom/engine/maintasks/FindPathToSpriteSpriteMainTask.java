/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Use Pathfinder algorithm to move Sprite until reaching a minimum distance toward a given Sprite
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FindPathToSpriteSpriteMainTask implements MotionSpriteMainTask {

    private int targetSpriteId;
    private double minDistance;
    private boolean center;
    private FindPathToPointSpriteMainTask moveTask;
    private boolean moving;

    public FindPathToSpriteSpriteMainTask(Sprite targetSprite, double minDistance, boolean center) {
        this(targetSprite.getId(), minDistance, center);
    }

    public FindPathToSpriteSpriteMainTask(int targetSpriteId, double minDistance, boolean center) {
        this.targetSpriteId = targetSpriteId;
        this.center = center;
        this.minDistance = minDistance;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public int getTargetSprite() {
        return targetSpriteId;
    }

    public void setTargetSprite(int targetSprite) {
        this.targetSpriteId = targetSprite;
    }

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        Sprite targetSprite = scene.getSprite(targetSpriteId);
        if (targetSprite != null) {
            if (moveTask == null) {
                moveTask = new FindPathToPointSpriteMainTask(null, minDistance, center);
            }
            moveTask.setCenter(center);
            moveTask.setMinDistance(minDistance);
            moveTask.setTarget(targetSprite.getLocation());
            if (moveTask.nextFrame(scene, sprite)) {
                moving = sprite != null && sprite.getSceneEngine().getSprite(targetSpriteId) != null;
                return true;
            }
        }
        moveTask = null;
        moving = false;
        return false;
    }

    @Override
    public boolean isMoving() {
        return moving;
    }


    public ModelPoint[] getMovePath() {
        if (moveTask == null) {
            return null;
        }
        return moveTask.getMovePath();
    }

    @Override
    public String toString() {
        return "Get Closer To " + targetSpriteId;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
        if (moveTask != null) {
            moveTask.setCenter(center);
        }
    }

}
