/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.tasks;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Use Pathfinder algorithm to move Sprite until reaching a minimum distance toward a given Sprite
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FindPathToSpriteSpriteTask implements MotionSpriteTask {

    private int targetSpriteId;
    private double minDistance;
    private boolean center;
    private FindPathToPointSpriteTask moveTask;
    private boolean moving;

    public FindPathToSpriteSpriteTask(Sprite targetSprite, double minDistance, boolean center) {
        this(targetSprite.getId(), minDistance, center);
    }

    public FindPathToSpriteSpriteTask(int targetSpriteId, double minDistance, boolean center) {
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
                moveTask = new FindPathToPointSpriteTask(null, minDistance, center);
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
