/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

/**
 * GameEngine State enumeration
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public enum GameEngineState {
    /**
     * Initial state. Engine is not yet initialized
     */
    UNINITIALIZED,

    /**
     * Engine is starting : start() method is called but not yet terminated.
     */
    STARTING,

    /**
     * Engine is started.
     */
    STARTED,

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
