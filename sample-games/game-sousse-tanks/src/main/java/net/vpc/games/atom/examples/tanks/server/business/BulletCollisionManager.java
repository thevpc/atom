/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.server.business;

import net.vpc.gaming.atom.engine.collision.BorderCollision;
import net.vpc.gaming.atom.engine.collision.DefaultSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollision;
import net.vpc.gaming.atom.engine.collision.TileCollision;
import net.vpc.gaming.atom.model.Sprite;

/**
 *
 * @author Taha Ben Salah
 */
public class BulletCollisionManager extends DefaultSpriteCollisionManager{

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
