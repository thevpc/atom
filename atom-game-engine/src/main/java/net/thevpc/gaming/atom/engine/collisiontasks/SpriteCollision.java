/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;

import java.util.Collection;

/**
 * Object that store Collision info between the sprite and the another Sprite
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpriteCollision extends Collision {

    private Sprite other;
    private int otherCollisionSides;
    private boolean isInitiator;
    private Collection<Tile> collisionTiles;

    public SpriteCollision(SceneEngine engine, Sprite other, int otherCollisionSides, Sprite sprite, int spriteCollisionSides, boolean isInitiator, Collection<Tile> collisionTiles, ModelPoint lastValidPosition, ModelPoint nextValidPosition) {
        super(engine, sprite, spriteCollisionSides, lastValidPosition, nextValidPosition);
        this.other = other;
        this.otherCollisionSides = otherCollisionSides;
        this.isInitiator = isInitiator;
        this.collisionTiles = collisionTiles;
    }

    public Collection<Tile> getCollisionTiles() {
        return collisionTiles;
    }

    public boolean isInitiator() {
        return isInitiator;
    }

    public Sprite getOther() {
        return other;
    }

    public int getOtherCollisionSides() {
        return otherCollisionSides;
    }

    public boolean isOtherCollisionNorth() {
        return (otherCollisionSides & SIDE_NORTH) != 0;
    }

    public boolean isOtherCollisionSouth() {
        return (otherCollisionSides & SIDE_SOUTH) != 0;
    }

    public boolean isOtherCollisionWest() {
        return (otherCollisionSides & SIDE_WEST) != 0;
    }

    public boolean isOtherCollisionEast() {
        return (otherCollisionSides & SIDE_EAST) != 0;
    }

//    public String toString() {
//        return "TileCollision{"  '}';
//    }

    @Override
    public String toString() {
        return "SpriteCollision{" + "sprite=" + getSprite() + ", spriteCollisionSides=" + getSideString(getSpriteCollisionSides()) + ", last=" + getLastValidPosition() + ", current =" + getSprite().getLocation() + ", next=" + getNextValidLocation() + " , bounds=" + getSprite().getBounds()
                + ", other=" + other + ", otherCollisionSides=" + getSideString(otherCollisionSides) + ", otherBounds=" + other.getBounds()
                + ", isInitiator=" + isInitiator + ", collisionTiles=" + collisionTiles + '}';
    }

}
