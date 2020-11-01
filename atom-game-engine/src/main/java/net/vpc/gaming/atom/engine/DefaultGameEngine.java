/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.ioc.AtomIoCContainer;
import net.vpc.gaming.atom.ioc.GameEngineIoCContainer;
import net.vpc.gaming.atom.model.DefaultGameEngineProperties;

import java.util.*;
import net.vpc.gaming.atom.model.GameEngineProperties;

/**
 * Default GameEngine implementation.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultGameEngine implements GameEngine {

    private Map<String, SceneEngine> scenes = new HashMap<String, SceneEngine>();
    private String activeSceneEngineId;
    private String defaultActiveSceneEngineId;
    private List<GameEngineStateListener> stateListeners = new ArrayList<GameEngineStateListener>();
    private List<GameEngineChangeListener> changeListeners = new ArrayList<GameEngineChangeListener>();
    private GameEngineState engineState = GameEngineState.UNINITIALIZED;
    private GameEngineProperties properties;
    private AtomIoCContainer atomIoCContainer;

    /**
     * default constructor
     */
    public DefaultGameEngine() {
        properties = new DefaultGameEngineProperties();
    }

    public GameEngineProperties getProperties() {
        return properties;
    }

    public void setProperties(GameEngineProperties properties) {
        this.properties = properties;
    }
    

    /**
     * {@inheritDoc }
     */
    public void addStateListener(GameEngineStateListener engineListener) {
        stateListeners.add(engineListener);
    }

    /**
     * {@inheritDoc }
     */
    public void removeStateListener(GameEngineStateListener engineListener) {
        stateListeners.remove(engineListener);
    }

    /**
     * {@inheritDoc }
     */
    public void addChangeListener(GameEngineChangeListener engineListener) {
        changeListeners.add(engineListener);
    }

    /**
     * {@inheritDoc }
     */
    public void removeChangeListener(GameEngineChangeListener engineListener) {
        changeListeners.remove(engineListener);
    }

    /**
     * {@inheritDoc }
     */
    public void addScene(SceneEngine sceneEngine) {
        String sceneId = sceneEngine.getId();
        SceneEngine s = scenes.get(sceneId);
        if (s != null) {
            throw new IllegalArgumentException("Scene already registered");
        }
        scenes.put(sceneId, sceneEngine);
        sceneEngine.init(this);
        for (GameEngineChangeListener listener : changeListeners) {
            listener.sceneAdded(sceneEngine);
        }
        if (activeSceneEngineId == null) {
            setActiveSceneEngine(sceneId);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeScene(Class<? extends SceneEngine> sceneType) {
        removeScene(sceneType.getName());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setActiveSceneEngine(Class<? extends SceneEngine> sceneType) {
        setActiveSceneEngine(sceneType.getName());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeScene(String sceneId) {
        if (sceneId == null) {
            throw new NoSuchElementException("Scene not found");
        }
        SceneEngine s = scenes.get(sceneId);
        if (s == null) {
            throw new NoSuchElementException("Scene not found");
        }
        if (activeSceneEngineId != null && activeSceneEngineId.equals(sceneId)) {
            setActiveSceneEngine((String) null);
        }
        scenes.remove(sceneId);
        s.dispose();
        for (GameEngineChangeListener listener : changeListeners) {
            listener.sceneRemoved(s);
        }
    }

    public boolean containsScene(String sceneId){
        return scenes.containsKey(sceneId);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SceneEngine getScene(String id) {
        SceneEngine newScene = scenes.get(id);
        if (newScene == null) {
            throw new NoSuchElementException("SceneEngine not found : "+id);
        }
        return newScene;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<SceneEngine> getScenes() {
        return new ArrayList<>(scenes.values());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SceneEngine getActiveScene() {
        if (activeSceneEngineId == null) {
            return null;
        }
        return getScene(activeSceneEngineId);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T extends SceneEngine> T getScene(Class<T> sceneType) {
        return (T) getScene(sceneType.getName());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setActiveSceneEngine(String name) {
        if ((activeSceneEngineId == null && name == null) || (activeSceneEngineId != null && activeSceneEngineId.equals(name))) {
            return;
        }


        SceneEngine newScene = null;
        if (name != null) {
            newScene = scenes.get(name);
            if (newScene == null) {
                throw new NoSuchElementException("Scene '" + name + "' not found");
            }
        }

        SceneEngine oldScene = null;
        if (activeSceneEngineId != null) {
            oldScene = scenes.get(activeSceneEngineId);
            oldScene.deactivate();
        }

        if (newScene != null) {
            activeSceneEngineId = name;
            tryActivateScene(newScene);
        }
        for (GameEngineChangeListener listener : changeListeners) {
            listener.activeSceneChanged(oldScene, newScene);
        }
    }

    /**
     * if scene is not started will start it before activating it
     */
    private void tryActivateScene(SceneEngine scene) {
        switch (getState()) {
            case UNINITIALIZED: {
                //do nothing
                break;
            }
            case STARTING: {
                //do nothing
                break;
            }
            case STARTED: {
                switch (scene.getSceneEngineState()) {
                    case STOPPED:
                    case INITIALIZED: {
                        scene.start();
                        scene.activate();
                        break;
                    }
                    case DEACTIVATED: {
                        scene.activate();
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unexpected state " + scene.getSceneEngineState());
                    }
                }
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected state " + scene.getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void start() {
        switch (engineState) {
            case STOPPED:
            case UNINITIALIZED: {
                engineState = GameEngineState.STARTING;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameStarting(this);
                }

                ArrayList<SceneEngine> _scenes = new ArrayList<SceneEngine>(scenes.values());
                for (SceneEngine sceneEngine : _scenes) {
                    sceneEngine.start();
                }
                if(_scenes.isEmpty()){
                    throw new IllegalStateException("There are no registered scenes.");
                }

                if(getDefaultSceneId()!=null){
                    setActiveSceneEngine(getDefaultSceneId());
                }
                SceneEngine scene = getActiveScene();
                if(scene==null){
                    if(getDefaultSceneId()==null){
                        throw new IllegalStateException("Default Scene could not be resolved");
                    }
                    throw new IllegalStateException("Scene "+getDefaultSceneId()+" could not be resolved. Have you marked scenes with @AtomScene annotation?");
                }

                engineState = GameEngineState.STARTED;

                tryActivateScene(scene);
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameStarted(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Start Game while " + engineState);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void stop() {
        switch (engineState) {
            case STARTED: {
                engineState = GameEngineState.STOPPING;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameStopping(this);
                }
                SceneEngine a = getActiveScene();
                if (a != null) {
                    switch (a.getSceneEngineState()) {
                        case ACTIVATED: {
                            a.deactivate();
                            break;
                        }
                    }
                }
                ArrayList<SceneEngine> _scenes = new ArrayList<SceneEngine>(scenes.values());
                for (SceneEngine sceneEngine : _scenes) {
                    sceneEngine.stop();
                }

                engineState = GameEngineState.STOPPED;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameStopped(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Stop Game while " + engineState);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void pause() {
        switch (engineState) {
            case STARTED: {
                engineState = GameEngineState.PAUSING;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gamePausing(this);
                }
                SceneEngine activeSceneEngine = getActiveScene();
                if (activeSceneEngine != null) {
                    activeSceneEngine.pause();
                }
                engineState = GameEngineState.PAUSED;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gamePaused(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Pause Game while " + engineState);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void resume() {
        switch (engineState) {
            case PAUSED: {
                engineState = GameEngineState.RESUMING;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameResuming(this);
                }

                SceneEngine activeSceneEngine = getActiveScene();
                if (activeSceneEngine != null) {
                    activeSceneEngine.resume();
                }

                engineState = GameEngineState.STARTED;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameResumed(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Resume Game while " + engineState);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void dispose() {
        switch (engineState) {
            case UNINITIALIZED:
            case PAUSED:
            case STOPPED:
            case STARTED: {
                engineState = GameEngineState.DISPOSING;
                SceneEngine a = getActiveScene();

                ArrayList<SceneEngine> _scenes = new ArrayList<SceneEngine>(scenes.values());
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameDisposing(this);
                }
                if (a != null) {
                    switch (a.getSceneEngineState()) {
                        case ACTIVATED: {
                            a.deactivate();
                            break;
                        }
                    }
                }

                for (SceneEngine sceneEngine : _scenes) {
                    sceneEngine.stop();
                }

                for (SceneEngine sceneEngine : _scenes) {
                    sceneEngine.dispose();
                }
                engineState = GameEngineState.DISPOSED;
                for (GameEngineStateListener listener : stateListeners) {
                    listener.gameDisposed(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Dispose Game while " + engineState);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public GameEngineState getState() {
        return engineState;
    }

    @Override
    public SceneEngine createScene(String sceneID) {
        DefaultSceneEngine defaultSceneEngine = new DefaultSceneEngine();
        defaultSceneEngine.setId(sceneID);
        return defaultSceneEngine;
    }

    @Override
    public SceneEngine addScene(String sceneID) {
        SceneEngine sceneEngine = createScene(sceneID);
        addScene(sceneEngine);
        return sceneEngine;
    }

    @Override
    public AtomIoCContainer getContainer() {
        if(atomIoCContainer==null){
            atomIoCContainer=new GameEngineIoCContainer(this);
            atomIoCContainer.start();
        }
        return atomIoCContainer;
    }

    @Override
    public void setContainer(AtomIoCContainer container) {
        this.atomIoCContainer=container;
    }

    public String getDefaultSceneId() {
        return defaultActiveSceneEngineId;
    }

    public void setDefaultSceneId(String defaultActiveSceneEngineId) {
        this.defaultActiveSceneEngineId = defaultActiveSceneEngineId;
    }

}
