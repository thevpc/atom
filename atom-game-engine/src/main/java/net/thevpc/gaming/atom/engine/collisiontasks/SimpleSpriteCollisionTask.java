/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.Sprite;

/**
 * Simple CollisionManager implementation that stops a sprite when it collides with
 * game borders, walls or other crossable sprites
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SimpleSpriteCollisionTask implements SpriteCollisionTask {

    private boolean stopOnBorder;
    private boolean stopOnWall;
    private boolean stopOnSprite;
    private boolean processing;

    /**
     * Create simple CollisionManager that stops when collides with game borders, walls or other crossable sprites
     * calls <code>this(true,true,true)</code>
     */
    public SimpleSpriteCollisionTask() {
        this(true, true, true);
    }

    /**
     * @param stopOnBorder when true, this sprite cannot walk outside game area
     * @param stopOnWall   when true, this sprite cannot walk over walls
     * @param stopOnSprite when true, this sprite cannot walk over non crossable sprites
     */
    public SimpleSpriteCollisionTask(boolean stopOnBorder, boolean stopOnWall, boolean stopOnSprite) {
        this.stopOnBorder = stopOnBorder;
        this.stopOnWall = stopOnWall;
        this.stopOnSprite = stopOnSprite;
    }

    /**
     * {@inheritDoc}
     * this implementation handles nested collisiontasks detected
     */
    public final void collideWithBorder(BorderCollision borderCollision) {
        if (!stopOnBorder) {
            return;
        }
        if (processing) {
            return;
        }
        processing = true;
        try {
            collideWithBorderImpl(borderCollision);
        } finally {
            processing = false;
        }
    }


    /**
     * {@inheritDoc}
     * this implementation handles nested collisiontasks detected
     */
    public final void collideWithSprite(SpriteCollision spriteCollision) {
        if (!stopOnSprite) {
            return;
        }
        if (processing) {
            return;
        }
        processing = true;
        try {
            collideWithSpriteImpl(spriteCollision);
        } finally {
            processing = false;
        }
    }


    /**
     * {@inheritDoc}
     * this implementation handles nested collisiontasks detected
     */
    public final void collideWithTile(TileCollision tileCollision) {
        if (!stopOnWall) {
            return;
        }
        if (processing) {
            return;
        }
        processing = true;
        try {
            //halt position
            collideWithTileImpl(tileCollision);
        } finally {
            processing = false;
        }
    }


    protected void collideWithTileImpl(TileCollision tileCollision) {
        tileCollision.adjustSpritePosition();
    }

    protected void collideWithSpriteImpl(SpriteCollision spriteCollision) {
        Sprite other = spriteCollision.getOther();
        if (spriteCollision.isInitiator() && !other.isCrossable()) {
            spriteCollision.adjustSpritePosition();
        }
    }

    protected void collideWithBorderImpl(BorderCollision borderCollision) {
        borderCollision.adjustSpritePosition();
    }
}
