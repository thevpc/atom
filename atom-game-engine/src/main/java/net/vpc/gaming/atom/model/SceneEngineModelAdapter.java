/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.beans.PropertyChangeEvent;

/**
 * Adapter for SceneModelListener
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SceneEngineModelAdapter implements SceneEngineModelListener {

    @Override
    public void playerAdded(SceneEngineModel model, Player player) {

    }

    @Override
    public void playerRemoved(SceneEngineModel model, Player player) {

    }

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void spriteAdded(SceneEngineModel model, Sprite sprite) {

    }

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void spriteRemoved(SceneEngineModel model, Sprite sprite) {

    }

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void spriteUpdated(SceneEngineModel model, Sprite sprite, PropertyChangeEvent event) {

    }

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void spriteMoved(SceneEngineModel model, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation) {

    }

    @Override
    public void spriteRemoving(SceneEngineModel model, Sprite sprite) {

    }
}
