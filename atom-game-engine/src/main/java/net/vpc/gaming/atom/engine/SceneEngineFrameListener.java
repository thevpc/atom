/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.model.SceneEngineModel;

/**
 * SceneEngine Callback to be notified of model update
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineFrameListener {

    /**
     * Called each frame after all updates have bean made to model.
     * Should not update model
     *
     * @param sceneEngine current sceneEngine
     * @param model       current model
     */
    void modelUpdated(SceneEngine sceneEngine, SceneEngineModel model);

}
