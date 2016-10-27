/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.collision;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

import java.util.List;

/**
 * SceneCollisionManager is responsible of Collision Detection
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneCollisionManager {

    /**
     * Anticipates Sprite move to check if any collision will be fired when sprite location changes to <code>newLocation</code>.
     * This method does not update Sprite location. It does only check for collision using the defined Collision Manager.
     *
     * @param engine          current sceneEngine
     * @param sprite          sprite to move
     * @param newLocation     new sprite location
     * @param borderCollision check borderCollision
     * @param tileCollision   check tile borderCollision
     * @param spriteCollision check Sprite Collision
     * @return Detected Collisions or empty array (not null)
     */
    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation, boolean borderCollision, boolean tileCollision, boolean spriteCollision);

    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, boolean borderCollision, boolean tileCollision, boolean spriteCollision);

    public void nextFrame(SceneEngine engine);
}
