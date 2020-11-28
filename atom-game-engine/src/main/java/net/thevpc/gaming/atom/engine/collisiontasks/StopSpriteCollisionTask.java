/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

/**
 * Simple CollisionManager implementation that stops a sprite when it collides with
 * game borders, walls or other crossable sprites
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class StopSpriteCollisionTask implements SpriteCollisionTask {
    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        borderCollision.adjustSpritePosition();
    }

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        spriteCollision.adjustSpritePosition();
    }

    @Override
    public void collideWithTile(TileCollision tileCollision) {
        tileCollision.adjustSpritePosition();
    }
}
