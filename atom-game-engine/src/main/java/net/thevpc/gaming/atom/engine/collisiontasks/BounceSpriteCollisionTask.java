/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.DirectionTransform;

/**
 * Simple CollisionManager implementation that bounces a sprite when it collides with
 * game borders, walls or other crossable sprites
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BounceSpriteCollisionTask implements SpriteCollisionTask {
    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        borderCollision.adjustSpritePosition();
        borderCollision.getSprite().setDirection(DirectionTransform.BACKWARD);
    }

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        spriteCollision.adjustSpritePosition();
        spriteCollision.getSprite().setDirection(DirectionTransform.BACKWARD);
    }

    @Override
    public void collideWithTile(TileCollision tileCollision) {
        tileCollision.adjustSpritePosition();
    }
}
