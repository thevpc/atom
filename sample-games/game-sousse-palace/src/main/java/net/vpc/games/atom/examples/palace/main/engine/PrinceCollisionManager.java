/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.palace.main.engine;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.BorderCollision;
import net.vpc.gaming.atom.engine.collision.DefaultSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollision;
import net.vpc.gaming.atom.engine.collision.TileCollision;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.games.atom.examples.palace.main.model.Brick;

/**
 *
 * @author Taha Ben Salah
 */
@AtomSpriteCollisionManager(kind = "Prince")
public class PrinceCollisionManager extends DefaultSpriteCollisionManager {

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
