/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.CollisionSides;
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
    private CollisionSides otherCollisionSides;
    private boolean isInitiator;
    private Collection<Tile> collisionTiles;
    private ModelPoint otherLastValidPosition;
    private ModelPoint otherNextValidPosition;

    public SpriteCollision(SceneEngine engine, Sprite other, CollisionSides otherCollisionSides, Sprite sprite, CollisionSides spriteCollisionSides, boolean isInitiator, Collection<Tile> collisionTiles
            , ModelPoint lastValidPosition, ModelPoint nextValidPosition
            , ModelPoint otherLastValidPosition, ModelPoint otherNextValidPosition
    ) {
        super(engine, sprite, spriteCollisionSides, lastValidPosition, nextValidPosition);
        this.other = other;
        this.otherCollisionSides = otherCollisionSides;
        this.isInitiator = isInitiator;
        this.collisionTiles = collisionTiles;
        this.otherLastValidPosition = otherLastValidPosition;
        this.otherNextValidPosition = otherNextValidPosition;
    }

    public ModelPoint getOtherLastValidPosition() {
        return otherLastValidPosition;
    }

    public ModelPoint getOtherNextValidPosition() {
        return otherNextValidPosition;
    }

    public CollisionSides getOtherCollisionSides() {
        return otherCollisionSides;
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

//    public String toString() {
//        return "TileCollision{"  '}';
//    }

    @Override
    public String toString() {
        return "SpriteCollision{" + "sprite=" + getSprite() + ", spriteCollisionSides=" + getSpriteCollisionSides() + ", last=" + getLastValidPosition() + ", current =" + getSprite().getLocation() + ", next=" + getNextValidLocation() + " , bounds=" + getSprite().getBounds()
                + ", other=" + other + ", otherCollisionSides=" + otherCollisionSides + ", otherBounds=" + other.getBounds()
                + ", isInitiator=" + isInitiator + ", collisionTiles=" + collisionTiles + '}';
    }

}
