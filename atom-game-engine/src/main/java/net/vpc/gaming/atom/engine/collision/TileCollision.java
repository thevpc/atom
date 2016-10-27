/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.collision;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;

/**
 * Object that store Collision info between the sprite and a Wall Tile (getWall()!=Tile.NO_WALLS)
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TileCollision extends Collision {

    private Tile tile;
    private int tileCollisionSides;

    public TileCollision(SceneEngine engine, Tile tile, int tileCollisionSides, Sprite sprite, int spriteCollisionSides, ModelPoint lastValidPosition, ModelPoint nextValidPosition) {
        super(engine, sprite, spriteCollisionSides, lastValidPosition, nextValidPosition);
        this.tile = tile;
        this.tileCollisionSides = tileCollisionSides;
    }

    public Tile getTile() {
        return tile;
    }

    public int getTileCollisionSides() {
        return tileCollisionSides;
    }

    public boolean isTileCollisionNorth() {
        return (tileCollisionSides & SIDE_NORTH) != 0;
    }

    public boolean isTileCollisionSouth() {
        return (tileCollisionSides & SIDE_SOUTH) != 0;
    }

    public boolean isTileCollisionWest() {
        return (tileCollisionSides & SIDE_WEST) != 0;
    }

    public boolean isTileCollisionEast() {
        return (tileCollisionSides & SIDE_EAST) != 0;
    }

    @Override
    public String toString() {
        return "TileCollision{" + "sprite=" + getSprite() + "::" + getSideString(getSpriteCollisionSides())
                + ", location=" + getLastValidPosition()
                + " => " + getSprite().getLocation()
                + " => " + getNextValidLocation()
                + ", tile=" + tile + "::" + getSideString(tileCollisionSides) + '}';
    }
}
