/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.CollisionSides;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class Collision {
    private SceneEngine sceneEngine;
    private Sprite sprite;
    private CollisionSides spriteCollisionSides;
    private ModelPoint lastValidPosition;
    private ModelPoint nextValidPosition;

    public Collision(SceneEngine sceneEngine, Sprite sprite, CollisionSides spriteCollisionSides, ModelPoint lastValidPosition, ModelPoint nextValidPosition) {
        this.sceneEngine = sceneEngine;
        this.sprite = sprite;
        this.spriteCollisionSides = spriteCollisionSides;
        this.lastValidPosition = lastValidPosition;
        this.nextValidPosition = nextValidPosition;
    }

    public SceneEngine getSceneEngine() {
        return sceneEngine;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public ModelPoint getLastValidPosition() {
        return lastValidPosition;
    }


    public CollisionSides getSpriteCollisionSides() {
        return spriteCollisionSides;
    }

    public ModelPoint getNextValidLocation() {
        return nextValidPosition;
    }

    public String toString() {
        return "Collision{" + "sprite=" + getSprite() + ", spriteCollisionSides=" + getSpriteCollisionSides() + ", last=" + getLastValidPosition() + ", next=" + getNextValidLocation() + '}';
    }

    public void adjustSpritePosition(){
        ModelPoint nextValidLocation = getNextValidLocation();
        if (nextValidLocation != null) {
            getSprite().setLocation(nextValidLocation);
        }
    }
}
