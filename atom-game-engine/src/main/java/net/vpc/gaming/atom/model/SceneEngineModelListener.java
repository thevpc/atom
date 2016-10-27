/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.beans.PropertyChangeEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineModelListener {

    public void playerAdded(SceneEngineModel model, Player player);

    public void playerRemoved(SceneEngineModel model, Player player);

    public void spriteAdded(SceneEngineModel model, Sprite sprite);

    public void spriteRemoving(SceneEngineModel model, Sprite sprite);

    public void spriteRemoved(SceneEngineModel model, Sprite sprite);

    public void spriteUpdated(SceneEngineModel model, Sprite sprite, PropertyChangeEvent event);

    public void spriteMoved(SceneEngineModel model, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation);
}
