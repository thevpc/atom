/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

/**
 * SceneEngine State enumeration
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public enum SceneEngineState {

    /**
     * Initial state. Engine is not yet initialized
     */
    UNINITIALIZED,

    /**
     * Engine is initializing : set() method is called but not yet terminated.
     */
    INITIALIZING,

    /**
     * Engine is initialized
     */
    INITIALIZED,

    /**
     * Engine is activating : activate() method is called but not yet terminated.
     */
    ACTIVATING,

    /**
     * Engine is activated
     */
    ACTIVATED,

    /**
     * Engine is deactivating : activate() method is called but not yet terminated.
     */
    DEACTIVATING,

    /**
     * Engine is deactivated
     */
    DEACTIVATED,

    /**
     * Engine is starting.
     */
    STARTING,


    /**
     * Engine is pausing : pause() method is called but not yet terminated.
     */
    PAUSING,

    /**
     * Engine is paused.
     */
    PAUSED,

    /**
     * Engine is resuming : resume() method is called but not yet terminated.
     */
    RESUMING,

    /**
     * Engine is stopping : stop() method is called but not yet terminated.
     */
    STOPPING,
    /**
     * Engine is stopped.
     */
    STOPPED,

    /**
     * Engine is disposing : dispose() method is called but not yet terminated.
     */
    DISPOSING,

    /**
     * Engine is disposed.
     */
    DISPOSED

}
