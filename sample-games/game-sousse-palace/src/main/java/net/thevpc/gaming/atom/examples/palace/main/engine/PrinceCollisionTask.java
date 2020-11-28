/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.palace.main.engine;

import net.thevpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.thevpc.gaming.atom.engine.collisiontasks.*;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.examples.palace.main.model.Brick;

/**
 *
 * @author Taha Ben Salah
 */
@AtomSpriteCollisionManager(kind = "Prince")
public class PrinceCollisionTask implements SpriteCollisionTask {

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        Sprite other = spriteCollision.getOther();
        if (spriteCollision.isInitiator() && !other.isCrossable()) {
            if(other instanceof Brick){
                //if(other)
            }
        }
        spriteCollision.adjustSpritePosition();
    }

    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        borderCollision.adjustSpritePosition();
    }

    @Override
    public void collideWithTile(TileCollision tileCollision) {
        tileCollision.adjustSpritePosition();
    }
}
