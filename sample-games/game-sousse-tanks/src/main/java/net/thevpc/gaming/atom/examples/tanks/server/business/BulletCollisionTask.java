/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.server.business;

import net.thevpc.gaming.atom.engine.collisiontasks.*;
import net.thevpc.gaming.atom.model.Sprite;

/**
 *
 * @author Taha Ben Salah
 */
public class BulletCollisionTask implements SpriteCollisionTask {

    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        borderCollision.getSprite().die();
    }

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        final Sprite other = spriteCollision.getOther();
        if(other.getKind().equals("Tank") && other.getPlayerId()!=other.getPlayerId()){
           other.addLife(-1); 
        }
    }

    @Override
    public void collideWithTile(TileCollision tileCollision) {
        tileCollision.getSprite().die();
    }
    
}
