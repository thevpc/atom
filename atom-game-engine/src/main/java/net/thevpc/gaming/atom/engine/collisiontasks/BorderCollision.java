/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Object that store Collision info between the sprite and the Game Borders
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BorderCollision extends Collision {

    private int borderCollisionSides;

    public BorderCollision(SceneEngine engine, int borderCollisionSides, Sprite sprite, int spriteCollisionSides, ModelPoint lastValidPosition, ModelPoint nextValidPosition) {
        super(engine, sprite, spriteCollisionSides, lastValidPosition, nextValidPosition);
        this.borderCollisionSides = borderCollisionSides;
    }

    public int getBorderCollisionSides() {
        return borderCollisionSides;
    }

    public boolean isBorderCollisionNorth() {
        return (borderCollisionSides & SIDE_NORTH) != 0;
    }

    public boolean isBorderCollisionSouth() {
        return (borderCollisionSides & SIDE_SOUTH) != 0;
    }

    public boolean isBorderCollisionWest() {
        return (borderCollisionSides & SIDE_WEST) != 0;
    }

    public boolean isBorderCollisionEast() {
        return (borderCollisionSides & SIDE_EAST) != 0;
    }

    @Override
    public String toString() {
        return "BorderCollision{" + "sprite=" + getSprite() + ", spriteCollisionSides=" + getSideString(getSpriteCollisionSides()) + ", last=" + getLastValidPosition() + ", next=" + getNextValidLocation() + "borderCollisionSides=" + getSideString(getBorderCollisionSides()) + '}';
    }
}
