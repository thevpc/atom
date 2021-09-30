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
 * Object that store Collision info between the sprite and the Game Borders
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BorderCollision extends Collision {

    private CollisionSides borderCollisionSides;

    public BorderCollision(SceneEngine engine, CollisionSides borderCollisionSides, Sprite sprite, CollisionSides spriteCollisionSides, ModelPoint lastValidPosition, ModelPoint nextValidPosition) {
        super(engine, sprite, spriteCollisionSides, lastValidPosition, nextValidPosition);
        this.borderCollisionSides = borderCollisionSides;
    }

    public CollisionSides getBorderCollisionSides() {
        return borderCollisionSides;
    }

    @Override
    public String toString() {
        return "BorderCollision{" + "sprite=" + getSprite() + ", spriteCollisionSides=" + getSpriteCollisionSides() + ", last=" + getLastValidPosition() + ", next=" + getNextValidLocation() + "borderCollisionSides=" + getBorderCollisionSides() + '}';
    }
}
