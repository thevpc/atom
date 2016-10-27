/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.tasks;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Use Pathfinder algorithm to move Sprite until reaching a minimum distance
 * toward a given Sprite and than attacks the sprite
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class AttackSpriteTask implements MotionSpriteTask {

    private int victimId;
    private FindPathToSpriteSpriteTask followTask;
    private boolean moving;
    private double speed;

    public AttackSpriteTask(Sprite victim, double speed) {
        this(victim.getId(), speed);
    }

    public AttackSpriteTask(int victimId, double speed) {
        this.victimId = victimId;
        this.speed = speed;
        // minDistance will be calculated according to range
        this.followTask = new FindPathToSpriteSpriteTask(victimId, 0, true);
    }

    public double getSpeed() {
        return speed;
    }

    public int getVictimId() {
        return victimId;
    }

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        Sprite victimId = scene.getSprite(this.victimId);
        if (victimId != null && !victimId.isDead()) {
            if (scene.attack(sprite, victimId) == Sprite.OUT_OF_RANGE) {
                //should be closer
                followTask.nextFrame(scene, sprite);
            }
            moving = true;
            return true;
        }
        return false;
    }

    public boolean isMoving() {
        return moving;
    }

    public ModelPoint[] getMovePath() {
        return followTask.getMovePath();
    }

    @Override
    public String toString() {
        return "Attack";
    }
}
