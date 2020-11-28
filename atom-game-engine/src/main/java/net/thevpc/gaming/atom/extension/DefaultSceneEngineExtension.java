/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension;

import net.thevpc.gaming.atom.engine.SceneEngine;

/**
 * Base abstract SceneEngineExtension implementation
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class DefaultSceneEngineExtension implements SceneEngineExtension {
    private String extensionId;

    /**
     * Simple Constructor
     *
     * @param extensionId extension ID. When null uses full class name
     */
    protected DefaultSceneEngineExtension(String extensionId) {
        this.extensionId = extensionId == null ? getClass().getName() : extensionId;
    }

    /**
     * Default Constructor.
     * Calls <code>this(null);</code>
     */
    protected DefaultSceneEngineExtension() {
        this(null);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Does nothing
     */
    @Override
    public String getExtensionId() {
        return extensionId;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Does nothing
     */
    @Override
    public void install(SceneEngine engine) {

    }

    /**
     * {@inheritDoc}
     * <p/>
     * Does nothing
     */
    @Override
    public void uninstall(SceneEngine engine) {

    }

}
