/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.collision;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Simple SpriteCollisionManager implementation that does nothing.
 * This is kind of Adapter implementation
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSpriteCollisionManager implements SpriteCollisionManager {

    @Override
    public void install(SceneEngine sceneEngine, Sprite sprite) {

    }

    @Override
    public void uninstall(SceneEngine sceneEngine, Sprite sprite) {

    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation does nothing
     */
    public void collideWithBorder(BorderCollision borderCollision) {
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation does nothing
     */
    public void collideWithSprite(SpriteCollision spriteCollision) {
        //
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation does nothing
     */
    public void collideWithTile(TileCollision tileCollision) {
        //
    }

}
