/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.shared.engine.collision;

import net.vpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.DefaultSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollision;
import net.vpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteCollisionManager(
        engine = "mainLocal,mainServer",
        kind = "Explosion"
)
public class ExplosionCollisionManager extends DefaultSpriteCollisionManager {

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        Sprite explosion = spriteCollision.getSprite();
        Sprite other = spriteCollision.getOther();
        if (!spriteCollision.getOther().isDead()) {
            explosion.die();
            if (other.getKind().equals("Person")) {
                other.addLife(-1);
            }
        }
    }
}
