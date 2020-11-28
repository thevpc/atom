/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.weapons.gun;

import net.thevpc.gaming.atom.engine.collisiontasks.*;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BulletCollisionTask implements SpriteCollisionTask {

    public void collideWithBorder(BorderCollision borderCollision) {
        borderCollision.getSprite().die();
    }

    public void collideWithSprite(SpriteCollision spriteCollision) {
        Sprite other = spriteCollision.getOther();
        if (other.getPlayerId() != spriteCollision.getSprite().getPlayerId()) {
            Sprite sprite = spriteCollision.getSprite();
            spriteCollision.getSceneEngine().processShot(sprite, other, ((Bullet) sprite).getDamage());
            sprite.die();
        }
    }

    public void collideWithTile(TileCollision tileCollision) {
        tileCollision.getSprite().die();
    }
}
