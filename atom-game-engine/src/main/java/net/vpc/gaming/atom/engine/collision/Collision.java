/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.collision;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class Collision {
    public static final int SIDE_NORTH = 1;
    public static final int SIDE_EAST = 2;
    public static final int SIDE_SOUTH = 4;
    public static final int SIDE_WEST = 8;

    private SceneEngine sceneEngine;
    private Sprite sprite;
    private int spriteCollisionSides;
    private ModelPoint lastValidPosition;
    private ModelPoint nextValidPosition;

    public Collision(SceneEngine sceneEngine, Sprite sprite, int spriteCollisionSides, ModelPoint lastValidPosition, ModelPoint nextValidPosition) {
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

    public boolean isSpriteCollisionNorth() {
        return (spriteCollisionSides & SIDE_NORTH) != 0;
    }

    public boolean isSpriteCollisionSouth() {
        return (spriteCollisionSides & SIDE_SOUTH) != 0;
    }

    public boolean isSpriteCollisionWest() {
        return (spriteCollisionSides & SIDE_WEST) != 0;
    }

    public boolean isSpriteCollisionEast() {
        return (spriteCollisionSides & SIDE_EAST) != 0;
    }

    public int getSpriteCollisionSides() {
        return spriteCollisionSides;
    }

    public ModelPoint getNextValidLocation() {
        return nextValidPosition;
    }

    public static boolean isCollideNorth(int side) {
        return (side & SIDE_NORTH) != 0;
    }
    public static boolean isCollideSouth(int side) {
        return (side & SIDE_SOUTH) != 0;
    }
    public static boolean isCollideEast(int side) {
        return (side & SIDE_EAST) != 0;
    }
    public static boolean isCollideWest(int side) {
        return (side & SIDE_WEST) != 0;
    }
    public static String getSideString(int side) {
        if (side == 0) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        if ((side & SIDE_NORTH) != 0) {
            s.append('N');
        }
        if ((side & SIDE_EAST) != 0) {
            s.append('E');
        }
        if ((side & SIDE_SOUTH) != 0) {
            s.append('S');
        }
        if ((side & SIDE_WEST) != 0) {
            s.append('W');
        }
        side &= ~(SIDE_EAST | SIDE_WEST | SIDE_NORTH | SIDE_SOUTH);
        if (side != 0) {
            s.append(side);
        }
        return s.toString();
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
