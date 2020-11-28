/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Simple SpriteCollisionTask implementation that does nothing.
 * This is kind of Adapter implementation
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSpriteCollisionTask implements SpriteCollisionTask {

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
