/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;

import java.util.List;

/**
 * SceneCollisionManager is responsible of Collision Detection
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneCollisionManager {

    /**
     * Anticipates Sprite move to check if any collisiontasks will be fired when sprite location changes to <code>newLocation</code>.
     * This method does not update Sprite location. It does only check for collisiontasks using the defined Collision Manager.
     *
     * @param engine          current sceneEngine
     * @param sprite          sprite to move
     * @param borderCollision check borderCollision
     * @param tileCollision   check tile borderCollision
     * @param spriteCollision check Sprite Collision
     * @return Detected Collisions or empty array (not null)
     */
    List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, boolean borderCollision, boolean tileCollision, boolean spriteCollision);

    void nextFrame(SceneEngine engine);
}
