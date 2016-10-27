/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension;

import net.vpc.gaming.atom.engine.SceneEngine;

/**
 * SceneEngineExtension is a callback that can be used to add custom logic behavior to the Scene Engine.
 * For each frame step, SceneEngine calls all SceneEngineFrameListener instances (no particular order must be assumed)
 * The only guarantee is that these listeners are called after SpriteTasks.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineExtension {

    /**
     * Unique ID used to install, uninstall and find Extensions
     *
     * @return non null Extension Id;
     */
    public String getExtensionId();

    /**
     * Called once when installing the sceneEngine using the <code>SceneEngine.installExtension<code> method
     *
     * @param engine current scene sceneEngine
     */
    public void install(SceneEngine engine);

    /**
     * Called once when uninstalling the sceneEngine using the <code>SceneEngine.uninstallExtension<code> method
     *
     * @param engine current scene sceneEngine
     */
    public void uninstall(SceneEngine engine);

}
