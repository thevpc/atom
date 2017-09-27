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

     void playerAdded(SceneEngineModel model, Player player);

     void playerRemoved(SceneEngineModel model, Player player);

     void spriteAdded(SceneEngineModel model, Sprite sprite);

     void spriteRemoving(SceneEngineModel model, Sprite sprite);

     void spriteRemoved(SceneEngineModel model, Sprite sprite);

     void spriteUpdated(SceneEngineModel model, Sprite sprite, PropertyChangeEvent event);

     void spriteMoved(SceneEngineModel model, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation);
}
