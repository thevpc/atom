/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.shared.engine.collision;

import net.thevpc.gaming.atom.annotations.AtomSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteCollisionTask(
        sceneEngine = "mainLocal,mainServer",
        kind = "Explosion"
)
public class ExplosionCollisionTask implements SpriteCollisionTask {

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
