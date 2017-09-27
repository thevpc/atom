/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.engine.tasks.HoldPositionSpriteTask;
import net.vpc.gaming.atom.extension.SceneEngineExtension;
import net.vpc.gaming.atom.model.armors.InvincibleSpriteArmor;
import net.vpc.gaming.atom.model.armors.InvincibleSpriteArmorAction;
import net.vpc.gaming.atom.model.armors.LevelSpriteArmor;
import net.vpc.gaming.atom.model.armors.LevelSpriteArmorAction;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import net.vpc.gaming.atom.engine.collision.BorderCollision;
import net.vpc.gaming.atom.engine.collision.Collision;
import net.vpc.gaming.atom.engine.collision.DiscreteSceneCollisionManager;
import net.vpc.gaming.atom.engine.collision.SceneCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollision;
import net.vpc.gaming.atom.engine.collision.SpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.TileCollision;
import net.vpc.gaming.atom.model.DamageEffects;
import net.vpc.gaming.atom.model.DefaultSceneEngineModel;
import net.vpc.gaming.atom.model.DefaultSprite;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.SceneEngineModel;
import net.vpc.gaming.atom.model.SceneEngineModelAdapter;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.SpriteArmor;
import net.vpc.gaming.atom.model.SpriteArmorAction;
import net.vpc.gaming.atom.model.SpriteWeapon;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.util.MultiChronometer;

/**
 * Default Scene Engine implementation. Uses a Single Thread Model. Scene Engine
 * Thread runs periodically (period = 1/fps) and invoked method nextFrame. To
 * capture state changes one may override methods <ul>
 * <li><code>sceneInitializing()</code></li>
 * <li><code>sceneInitialized()</code></li>
 * <li><code>sceneStarting()</code></li> <li><code>sceneStarted()</code></li>
 * <li><code>sceneActivating()</code></li>
 * <li><code>sceneActivated()</code></li> <li><code>scenePausing()</code></li>
 * <li><code>scenePaused()</code></li> <li><code>sceneResuming()</code></li>
 * <li><code>sceneResumed()</code></li>
 * <li><code>sceneDeactivating()</code></li>
 * <li><code>sceneDeactivated()</code></li>
 * <li><code>sceneStopping()</code></li> <li><code>sceneStopped()</code></li>
 * <li><code>sceneDisposing()</code></li> <li><code>sceneDisposed()</code></li>
 * </ul>
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneEngine implements SceneEngine {

    private Object companionObject;
    protected boolean frameExecution;
    private String sceneEngineId;
    private SceneEngineState sceneEngineState = SceneEngineState.UNINITIALIZED;
    private int fps = 25;
    private GameEngine gameEngine;
    private SceneEngineModel model;
    private Timer timer;
    private LinkedList<Long> frameExecutionTimes = new LinkedList<Long>();
    private int frameExecutionTimesMaxCount = 100;
    private LinkedList<Runnable> events = new LinkedList<Runnable>();
    private List<SceneEngineTask> engineTasks = new ArrayList<SceneEngineTask>();
    private List<SceneEngineChangeListener> engineUpdateListeners = new ArrayList<SceneEngineChangeListener>();
    private List<SceneEngineStateListener> engineStateListeners = new ArrayList<SceneEngineStateListener>();
    private List<SceneEngineFrameListener> engineFrameListeners = new ArrayList<SceneEngineFrameListener>();
    private Map<String, SceneEngineExtension> engineExtensions = new LinkedHashMap<String, SceneEngineExtension>();
    //private SceneCollisionManager collisionManager = new DefaultSceneCollisionManager();
    private SceneCollisionManager collisionManager = new DiscreteSceneCollisionManager();
    private SpriteExtManager<SpriteCollisionManager> spriteCollisionManagers;
    private SpriteExtManager<SpriteTask> spriteTasks;
    private List<SpriteCollisionManager> globalSpriteCollisionManagers = new ArrayList<SpriteCollisionManager>();
    private HashMap<Integer, ModelPoint> movingSprites = new HashMap<Integer, ModelPoint>();
    private HashMap<Class, SpriteArmorAction> spriteArmorActions = new HashMap<>();
    private ModelTracker modelTracker = new ModelTracker();
    private PlayerFactory playerFactory = new DefaultPlayerFactory();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private PropertyChangeListener modelListenerBridge = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            propertyChangeSupport.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    };

    /**
     * Default constructor
     */
    protected DefaultSceneEngine() {
        setModel0(new DefaultSceneEngineModel(1, 1));
        spriteArmorActions.put(InvincibleSpriteArmor.class, new InvincibleSpriteArmorAction());
        spriteArmorActions.put(LevelSpriteArmor.class, new LevelSpriteArmorAction());
        spriteCollisionManagers=new SpriteExtManager<SpriteCollisionManager>(this);
        spriteTasks=new SpriteExtManager<SpriteTask>(this);
    }

    public DefaultSceneEngine(String id) {
        this();
        this.sceneEngineId = id;
    }

    /**
     * {@inheritDoc}
     */
    public int getFps() {
        return fps;
    }

    public int getEffectiveFps() {
        if (frameExecutionTimes.size() <= 1) {
            return fps;
        }
        Long first = frameExecutionTimes.getFirst();
        Long last = frameExecutionTimes.getLast();
        long size = frameExecutionTimes.size();
        return (int) ((size) * 1000L / (last - first));
    }

    /**
     * {@inheritDoc}
     */
    public void setFps(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS cannot be null or negative");
        }
        int old = this.fps;
        this.fps = fps;
        if (old != fps) {
            for (SceneEngineChangeListener listener : engineUpdateListeners) {
                listener.fpsChanged(this, old, fps);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        if (sceneEngineId == null) {
            return getClass().getName();
        }
        return sceneEngineId;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(String id) {
        switch (getSceneEngineState()) {
            case INITIALIZING:
            case UNINITIALIZED: {
                this.sceneEngineId = id;
                break;
            }
            default: {
                throw new IllegalStateException("Id could not be changed after initialization");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addSceneEngineChangeListener(SceneEngineChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException("SceneEngineChangeListener should not be null");
        }
        engineUpdateListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSceneEngineChangeListener(SceneEngineChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException("SceneEngineChangeListener should not be null");
        }
        engineUpdateListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addSpriteCollisionManager(SpriteCollisionManager manager) {
        if (manager == null) {
            throw new NullPointerException("SpriteCollisionManager should not be null");
        }
        globalSpriteCollisionManagers.add(manager);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSpriteCollisionManager(SpriteCollisionManager manager) {
        if (manager == null) {
            throw new NullPointerException("SpriteCollisionManager should not be null");
        }
        globalSpriteCollisionManagers.remove(manager);
    }

    public void setSpriteCollisionManager(int spriteId, SpriteCollisionManager collisionManager) {
        Sprite sprite = getSprite(spriteId);
        SpriteCollisionManager old = spriteCollisionManagers.getInstanceById(spriteId);
        spriteCollisionManagers.setInstanceById(spriteId,collisionManager);
        if (collisionManager == null) {
            if(old!=null) {
                old.uninstall(this, sprite);
            }
        } else {
            collisionManager.install(this, sprite);
        }
    }

    public void setSpriteCollisionManager(int spriteId, Class<? extends SpriteCollisionManager> collisionManager) {
        SpriteCollisionManager spriteCollisionManager = spriteCollisionManagers.create(collisionManager);
        spriteCollisionManagers.setInstanceById(spriteId,spriteCollisionManager);
    }

    public void setSpriteCollisionManager(String spriteKind, Class<? extends SpriteCollisionManager> collisionManager) {
        spriteCollisionManagers.setBeanByKind(spriteKind,collisionManager);
    }

    @Override
    public void setSpriteCollisionManager(String kind, SpriteCollisionManager collisionManager) {
        spriteCollisionManagers.setInstanceByKind(kind,collisionManager);
    }

    public SpriteCollisionManager getSpriteCollisionManager(int spriteId) {
        return spriteCollisionManagers.get(getSprite(spriteId));
    }


    @Override
    public SpriteCollisionManager getSpriteCollisionManager(String sprite) {
        return getSpriteCollisionManager(findUniqueSpriteByName(sprite));
    }

    @Override
    public void addSceneTask(SceneEngineTask task) {
        if (task == null) {
            throw new NullPointerException("Task should not be null");
        }
        engineTasks.add(task);
    }

    @Override
    public void removeSceneTask(SceneEngineTask task) {
        if (task == null) {
            throw new NullPointerException("Task should not be null");
        }
        engineTasks.remove(task);
    }

    /**
     * {@inheritDoc}
     */
    public void installExtension(SceneEngineExtension extension) {
        String extensionId = extension.getExtensionId();
        SceneEngineExtension old = engineExtensions.get(extensionId);
        if (old != null) {
            throw new IllegalArgumentException("Extension " + extensionId + " is already registered as " + old);
        }
        extension.install(this);
        engineExtensions.put(extensionId, extension);
    }

    /**
     * {@inheritDoc}
     */
    public void uninstallExtension(String extensionId) {
        if (extensionId == null) {
            throw new NullPointerException("Extension ID should not be null");
        }
        SceneEngineExtension e = engineExtensions.remove(extensionId);
        if (e == null) {
            throw new IllegalArgumentException("Extension " + extensionId + " not found");
        }
        e.uninstall(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SceneEngineExtension getExtension(String extensionId) {
        if (extensionId == null) {
            throw new NullPointerException("Extension ID should not be null");
        }
        return engineExtensions.get(extensionId);
    }

    @Override
    public <T extends SceneEngineExtension> T getExtension(Class<? extends T> extensionType) {
        return (T) getExtension(extensionType.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SceneEngineExtension[] getExtensions() {
        return engineExtensions.values().toArray(new SceneEngineExtension[engineExtensions.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public void addStateListener(SceneEngineStateListener listener) {
        engineStateListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeStateListener(SceneEngineStateListener listener) {
        engineStateListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addSceneFrameListener(SceneEngineFrameListener listener) {
        engineFrameListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSceneFrameListener(SceneEngineFrameListener listener) {
        engineFrameListeners.remove(listener);
    }

    /**
     * {@inheritDoc }
     */
    public final void dispose() {
        switch (getSceneEngineState()) {
            case ACTIVATED:
            case DEACTIVATED:
            case PAUSED:
            case STOPPED: {
                setSceneEngineState(SceneEngineState.DISPOSING);
                sceneDisposing();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneDisposing(this);
                }

                setSceneEngineState(SceneEngineState.DISPOSED);
                sceneDisposed();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneDisposed(this);
                }
                break;
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    public <T extends SceneEngineModel> T getModel() {
        return (T) model;
    }

    /**
     * {@inheritDoc }
     */
    public void setModel(SceneEngineModel model) {
        setModel0(model);
    }

    /**
     * {@inheritDoc }
     */
    public SceneCollisionManager getSceneCollisionManager() {
        return collisionManager;
    }

    /**
     * {@inheritDoc }
     */
    public void setSceneCollisionManager(SceneCollisionManager collisionManager) {
        if (!Objects.equals(this.collisionManager, collisionManager)) {
            SceneCollisionManager old = this.collisionManager;
            this.collisionManager = collisionManager;
            for (SceneEngineChangeListener listener : engineUpdateListeners) {
                listener.collisionManagerChanged(this, old, collisionManager);
            }
            propertyChangeSupport.firePropertyChange("collisionManager", old, collisionManager);
        }
    }

    /**
     * {@inheritDoc }
     */
    public List<Collision> detectCollisions(Sprite sprite, ModelPoint newPosition, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
        return collisionManager.detectCollisions(this, sprite, sprite.getLocation(), newPosition, borderCollision, tileCollision, spriteCollision);
    }

    /**
     * {@inheritDoc }
     */
    public <T extends GameEngine> T getGameEngine() {
        return (T) gameEngine;
    }

    /**
     * {@inheritDoc }
     */
    public SceneEngineState getSceneEngineState() {
        return sceneEngineState;
    }

    /**
     * update engineStatus
     *
     * @param sceneEngineState new engineStatus
     */
    protected void setSceneEngineState(SceneEngineState sceneEngineState) {
        SceneEngineState old = this.sceneEngineState;
        if (!Objects.equals(old, sceneEngineState)) {
            this.sceneEngineState = sceneEngineState;
            propertyChangeSupport.firePropertyChange("sceneEngineState", old, sceneEngineState);
        }
    }

    /**
     * {@inheritDoc }
     */
    public final void init(GameEngine gameEngine) {
        switch (getSceneEngineState()) {
            case UNINITIALIZED: {
                this.gameEngine = gameEngine;
                setSceneEngineState(SceneEngineState.INITIALIZING);
                sceneInitializing();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneInitializing(this);
                }

                // do nothing
                setSceneEngineState(SceneEngineState.INITIALIZED);
                sceneInitialized();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneInitialized(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Initialize while " + getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    public final void activate() {
        switch (getSceneEngineState()) {
            case DEACTIVATED: {
                sceneActivating();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneActivating(this);
                }
                if (timer == null) {
                    timer = new Timer(4, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!frameExecution) {
                                frameExecution = true;
                                if (frameExecutionTimes.size() >= frameExecutionTimesMaxCount) {
                                    frameExecutionTimes.removeFirst();
                                }
                                frameExecutionTimes.add(System.currentTimeMillis());
                                try {
                                    nextFrame();
                                } finally {
                                    frameExecution = false;
                                }
                            }
                        }
                    });
                    timer.setRepeats(true);
                    timer.setDelay(1000 / getFps());
                }
                timer.start();
                sceneActivated();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneActivated(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Deactivate while " + getSceneEngineState());
            }

        }
    }

    /**
     * {@inheritDoc }
     */
    public void deactivate() {
        switch (getSceneEngineState()) {
            case DEACTIVATED: {
                setSceneEngineState(SceneEngineState.DEACTIVATING);
                sceneDeactivating();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneDeactivating(this);
                }

                setSceneEngineState(SceneEngineState.DEACTIVATED);
                if (timer != null) {
                    timer.stop();
                }
                sceneDeactivated();

                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneDeactivated(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Deactvate while " + getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void start() {
        switch (getSceneEngineState()) {
            case STOPPED:
            case INITIALIZED: {
                setSceneEngineState(SceneEngineState.STARTING);
                sceneStarting();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneStarting(this);
                }

                setSceneEngineState(SceneEngineState.DEACTIVATED);
                sceneStarted();

                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneStarted(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Start while " + getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void stop() {
        switch (getSceneEngineState()) {
            case UNINITIALIZED:
            case INITIALIZED:
            case ACTIVATED:
            case DEACTIVATED: {
                setSceneEngineState(SceneEngineState.STOPPING);
                sceneStopping();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneStopping(this);
                }

                setSceneEngineState(SceneEngineState.STOPPED);

                if (timer != null) {
                    timer.stop();
                }

                sceneStopped();

                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneStopped(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Stop while " + getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void resume() {
        switch (getSceneEngineState()) {
            case PAUSED: {
                setSceneEngineState(SceneEngineState.RESUMING);
                sceneResuming();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneStopping(this);
                }
                timer.start();
                setSceneEngineState(SceneEngineState.ACTIVATED);

                sceneResumed();

                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.sceneStopped(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Resuming while " + getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void pause() {
        switch (getSceneEngineState()) {
            case ACTIVATED: {
                setSceneEngineState(SceneEngineState.PAUSING);
                scenePausing();
                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.scenePausing(this);
                }

                setSceneEngineState(SceneEngineState.PAUSED);

                timer.stop();

                scenePaused();

                for (SceneEngineStateListener listener : engineStateListeners) {
                    listener.scenePaused(this);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Cannot Pause while " + getSceneEngineState());
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void invokeLater(Runnable runnable) {
        events.add(runnable);
    }

    /**
     * update model. made private to be called in constructor
     *
     * @param model new model
     */
    private void setModel0(SceneEngineModel model) {
        if (!Objects.equals(this.model, model)) {
            SceneEngineModel old = this.model;
            this.model = model;
            if (old != null) {
                old.removeSceneEngineModelListener(modelTracker);
                old.removePropertyChangeListener(modelListenerBridge);
            }
            if (this.model != null) {
                this.model.addSceneEngineModelListener(modelTracker);
                this.model.addPropertyChangeListener(modelListenerBridge);
            }
            for (SceneEngineChangeListener listener : engineUpdateListeners) {
                listener.modelChanged(this, old, model);
            }
            propertyChangeSupport.firePropertyChange("model", old, model);
        }
    }

    /**
     * called when activating. Does nothing. Override it to add custom behavior.
     */
    protected void sceneActivating() {
    }

    /**
     * called when activated.
     * <p/>
     * Does nothing. Override it to add custom behavior.
     */
    protected void sceneActivated() {
    }

    /**
     * called when deactivating. Does nothing. Override it to add custom
     * behavior.
     */
    protected void sceneDeactivating() {
    }

    /**
     * called when deactivated. Does nothing. Override it to add custom
     * behavior.
     */
    protected void sceneDeactivated() {
    }

    /**
     * called when starting. Does nothing. Override it to add custom behavior.
     */
    protected void sceneStarting() {
    }

    /**
     * called when started. Does nothing. Override it to add custom behavior.
     */
    protected void sceneStarted() {
    }

    /**
     * called when stopping. Does nothing. Override it to add custom behavior.
     */
    protected void sceneStopping() {
    }

    /**
     * called when stopped. Does nothing. Override it to add custom behavior.
     */
    protected void sceneStopped() {
    }

    /**
     * called when pausing. Does nothing. Override it to add custom behavior.
     */
    protected void scenePausing() {
    }

    /**
     * called when paused. Does nothing. Override it to add custom behavior.
     */
    protected void scenePaused() {
    }

    /**
     * called when resuming. Does nothing. Override it to add custom behavior.
     */
    protected void sceneResuming() {
    }

    /**
     * called when resumed. Does nothing. Override it to add custom behavior.
     */
    protected void sceneResumed() {
    }

    /**
     * called when initializing. Does nothing. Override it to add custom
     * behavior.
     */
    protected void sceneInitializing() {
    }

    /**
     * called when initialized. Does nothing. Override it to add custom
     * behavior.
     */
    protected void sceneInitialized() {
    }

    /**
     * called when disposing. Does nothing. Override it to add custom behavior.
     */
    protected void sceneDisposing() {
    }

    /**
     * called when disposed.
     * <p/>
     * Does nothing. Override it to add custom behavior.
     */
    protected void sceneDisposed() {
    }

    /**
     * called when before frame steps. Must NOT update model
     * <p/>
     * Does nothing. Override it to add custom behavior. Implementations MUST
     * NOT update Model
     */
    protected void modelUpdating() {
    }

    /**
     * called when model is updated.
     * <p/>
     * Does nothing. Override it to add custom behavior. Implementations MUST
     * NOT update Model
     */
    protected void modelUpdated() {
    }

    /**
     * called when model is to be updated. Does nothing. Override it to add
     * custom behavior. Implementations MAY update Model
     */
    protected void updateModel() {
    }

    /**
     * called when model all model updates are performed. Actually, validate
     * model by removing all Dead Sprites Override to add custom behavior.
     * Implementations MAY update Model
     */
    protected void validateModel() {
        for (Sprite s : model.getSprites()) {
            if (s.isDead()) {
                model.removeSprite(s.getId());
            }
        }
    }

    /**
     * Called periodically (each 1/FPS) by the timer in the Engine Single
     * Thread. calls in that order <ol> <li>updateTime();</li>
     * <li>consumeFrameEvents();</li> <li>updateModel();</li> <li>Update Model
     * by Calling
     * <code>SpriteTask.nextFrame</code> on each Sprite</li>
     * <li>Update Model by Calling
     * <code>SceneEngineTask.nextFrame</code></li>
     * <li>validateModel();</li>
     * <li>modelUpdated();</li> <li>Fire
     * <code>SceneEngineUpdateListener.modelUpdated</code> to notify of the new
     * model value. Implementations MUST NOT update Model</li> </old>
     */
    protected void nextFrame() {
        MultiChronometer chronometer = new MultiChronometer(20);
        //clear moving sprite status set
        movingSprites.clear();
        modelUpdating();
        chronometer.snapshot("modelUpdating");

        //////////////////////////////
        //update model
        updateTime();
        chronometer.snapshot("updateTime");
        //////////////////////////////
//        System.out.println("###### nextFrame starting : "+getModel().getFrame());

        //////////////////////////////
        //consume events
        consumeFrameEvents();
        chronometer.snapshot("consumeFrameEvents");
        //////////////////////////////

        //////////////////////////////
        //update model
        modelTracker.reset();
        updateModel();
        chronometer.snapshot("updateModel");

        for (Sprite sprite : getSprites()) {
            SpriteTask spriteTask = getSpriteTask(sprite.getId());
            if (spriteTask != null) {
                if (!spriteTask.nextFrame(this, sprite)) {
                    setSpriteTask(sprite.getId(), HoldPositionSpriteTask.INSTANCE);
                }
            }
        }

        chronometer.snapshot("spriteTasks.nextFrame");

        for (SceneEngineTask task : new ArrayList<>(engineTasks)) {
            if (!task.nextFrame(this)) {
                removeSceneTask(task);
            }
        }
        chronometer.snapshot("engineTasks.nextFrame");
        //////////////////////////////

        //////////////////////////////
        // Validate Model
        validateModel();
        chronometer.snapshot("engineTasks.validateModel");

        //////////////////////////////
        // fire model updated
        collisionManager.nextFrame(this);
        chronometer.snapshot("collisionManager.nextFrame");
        modelTracker.fireCollisionEvents();
        chronometer.snapshot("modelTracker.fireCollisionEvents");

        modelUpdated();
        chronometer.snapshot("modelUpdated");

        for (SceneEngineFrameListener listener : engineFrameListeners) {
            listener.modelUpdated(this, model);
        }
        chronometer.snapshot("engineFrameListeners.modelUpdated");
        if (chronometer.getTime() > 100L) {
            System.out.println(chronometer);
        }

//        System.out.println("###### nextFrame ending : "+getModel().getFrame());
    }

    public ModelPoint getLastSpritePosition(int s) {
        ModelPoint modelPoint = movingSprites.get(s);
        if (modelPoint == null) {
            Sprite sprite = getSprite(s);
            if (sprite != null) {
                modelPoint = sprite.getLocation();
            }
        }
        return modelPoint;
    }

    /**
     * invokes all events (added by invokeLater) in the Engine Single Thread
     */
    protected void consumeFrameEvents() {
        while (!events.isEmpty()) {
            Runnable r = events.removeFirst();
            r.run();
        }
    }

    /**
     * Update Frame count. Actually just increments frame count
     * <pre>
     * model.setFrame(model.getFrame() + 1);
     * </pre> Implementations that handle differently frame count must override
     * this method. Particularly, in Client/Server Game implementations,
     * ClientSceneEngine must update its model's frame count according to the
     * Server's model frame count
     */
    protected void updateTime() {
        //update time (increment frame index)
        SceneEngineModel m = getModel();
        m.setFrame(m.getFrame() + 1);
    }

    /**
     * find for a position referring to a vacant location where sprite can be
     * put.
     *
     * @param sprite sprite for whom search for a vacant location
     * @return position referring to a vacant location where sprite can be put
     * or null if no place found
     */
    public ModelPoint randomVacantLocation(Sprite sprite) {
        int count = 100;
        Tile[][] matrix = getModel().getTileMatrix();
        while (count > 0) {
            count--;
            int c0 = (int) (Math.random() * getModel().getSize().getWidth());
            int r0 = (int) (Math.random() * getModel().getSize().getHeight());

            if (matrix[r0][c0].getWalls() == Tile.NO_WALLS) {
                Tile hh = matrix[r0][c0];
                List<Tile> tiles = getModel().findTiles(new ModelBox(hh.getLocation(), sprite.getSize()));
                boolean ok = true;
                for (Tile tile : tiles) {
                    if (tile.getWalls() != Tile.NO_WALLS) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    return hh.getLocation();
                }
            }
        }

        for (int r0 = 0; r0 < matrix.length; r0++) {
            Tile[] rtiles = matrix[r0];
            for (int c0 = 0; c0 < rtiles.length; c0++) {
                if (matrix[r0][c0].getWalls() == Tile.NO_WALLS) {
                    Tile hh = matrix[r0][c0];
                    List<Tile> tiles = getModel().findTiles(new ModelBox(hh.getLocation(), sprite.getSize()));
                    boolean ok = true;
                    for (Tile tile : tiles) {
                        if (tile.getWalls() != Tile.NO_WALLS) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        return hh.getLocation();
                    }
                }

            }
        }
        return null;
    }

    @Override
    public boolean hasMovedLastFrame(int spriteId) {
        return movingSprites.containsKey(spriteId);
    }

    public <T extends SpriteTask> T getSpriteTask(int spriteId, Class<T> type) {
        SpriteTask t = getSpriteTask(spriteId);
        if (t != null && type.isInstance(t)) {
            return (T) t;
        }
        return null;
    }

    public <T extends SpriteTask> T getSpriteTask(int spriteId) {
        return (T) spriteTasks.get(getSprite(spriteId));
    }

    private int getSpriteId(Sprite s) {
        return validateSprite(s).getId();
    }

    private Sprite validateSprite(Sprite s) {
        if (s == null) {
            throw new IllegalArgumentException("Null Sprite");
        }
        Sprite s2 = getModel().getSprite(s.getId());
        if (s == null) {
            throw new IllegalArgumentException("Sprite is not yet registered : id =" + s.getId());
        }
        if (s2 != s) {
            throw new IllegalArgumentException("Invalid Sprite. Id clashing with different sprite " + s.getId());
        }
        return s2;
    }

    public void setSpriteTask(Sprite sprite, SpriteTask task) {
        setSpriteTask(getSpriteId(sprite), task);
    }

    @Override
    public void setSpriteTask(String spriteKind, SpriteTask task) {
        spriteTasks.setInstanceByKind(spriteKind,task);
    }

    @Override
    public void setSpriteTask(String spriteKind, Class<? extends SpriteTask> task) {
        spriteTasks.setBeanByKind(spriteKind,task);
    }

    @Override
    public boolean hasMovedLastFrame(String spriteName) {
        Sprite sprite = findUniqueSpriteByName(spriteName);
        return hasMovedLastFrame(sprite.getId());
    }

    @Override
    public <T extends SpriteTask> T getSpriteTask(String name, Class<T> type) {
        return getSpriteTask(findUniqueSpriteByName(name).getId(), type);
    }

    @Override
    public <T extends SpriteTask> T getSpriteTask(String spriteId) {
        return getSpriteTask(findUniqueSpriteByName(spriteId).getId());
    }

    public SpriteCollisionManager getSpriteCollisionManager(Sprite sprite) {
        return getSpriteCollisionManager(getSpriteId(sprite));
    }

    public <T extends SpriteTask> T getSpriteTask(Sprite sprite, Class<T> type) {
        return getSpriteTask(getSpriteId(sprite), type);
    }

    public <T extends SpriteTask> T getSpriteTask(Sprite sprite) {
        return getSpriteTask(getSpriteId(sprite));
    }

    public void setSpriteCollisionManager(Sprite sprite, SpriteCollisionManager collisionManager) {
        setSpriteCollisionManager(getSpriteId(sprite), collisionManager);
    }

    public void setSpriteTask(int spriteId, SpriteTask task) {
        Sprite sp = getSprite(spriteId);
        spriteTasks.setInstanceById(spriteId,task);

        Sprite sprite = getSprite(spriteId);
        SpriteTask old = spriteTasks.getInstanceById(spriteId);
        spriteTasks.setInstanceById(spriteId,task);
        if (task == null) {
            if(old!=null) {
                //old.uninstall(this, sprite);
            }
        } else {
            //task.install(this, sprite);
        }
    }

    public void setSpriteTask(int spriteId, Class<? extends SpriteTask> task) {
        Sprite sp = getSprite(spriteId);
        SpriteTask spriteTask = spriteTasks.create(task);
        setSpriteTask(spriteId,spriteTask);
    }

    @Override
    public long getFrame() {
        return getModel().getFrame();
    }

    @Override
    public Player addPlayer() {
        Player p = getModel().createPlayer();
        addPlayer(p);
        return p;

    }

    @Override
    public int addPlayer(Player player) {
        return getModel().addPlayer(player);
    }

    @Override
    public int addSprite(Sprite sprite) {
        return getModel().addSprite(sprite);
    }

    @Override
    public List<Sprite> findSprites(ModelPoint point) {
        return getModel().findSprites(point);
    }

    @Override
    public List<Sprite> findSprites(ModelPoint point, int layer) {
        return getModel().findSprites(point, layer);
    }

    @Override
    public List<Sprite> findSprites(ModelBox rect) {
        return getModel().findSprites(rect);
    }

    @Override
    public Tile findTile(ModelPoint point) {
        return getModel().findTile(point);
    }

    @Override
    public List<Tile> findTiles(Path2D rect) {
        return getModel().findTiles(rect);
    }

    @Override
    public List<Tile> findTiles(ModelBox rect) {
        return getModel().findTiles(rect);
    }

    @Override
    public int getPlayersCount() {
        return getPlayersCount();
    }

    @Override
    public List<Player> getPlayers() {
        return getModel().getPlayers();
    }

    @Override
    public Player getPlayer(int id) {
        return getModel().getPlayer(id);
    }

    @Override
    public Sprite getSprite(int id) {
        return getModel().getSprite(id);
    }

    @Override
    public List<Sprite> getSprites() {
        return getModel().getSprites();
    }

    @Override
    public void removePlayer(int player) {
        getModel().removePlayer(player);
    }

    @Override
    public void removeSprite(int sprite) {
        getModel().removeSprite(sprite);
    }

    @Override
    public void resetSprites() {
        getModel().resetSprites();
    }

    @Override
    public void resetPlayers() {
        getModel().resetPlayers();
    }

    @Override
    public Tile[] getObstacleTiles() {
        //TODO add cache support
        List<Tile> ot = new ArrayList<>();
        for (Tile tile : getTiles()) {
            if (tile.getWalls() != 0) {
                ot.add(tile);
            }
        }
        return ot.toArray(new Tile[ot.size()]);
    }

    @Override
    public Tile[] getTiles() {
        return getModel().getTiles();
    }

    @Override
    public Tile[][] getTileMatrix() {
        return getModel().getTileMatrix();
    }

    @Override
    public void synchronizeSprites(List<Sprite> sprites) {
        Set<Integer> otherSprites = new HashSet<>();
        for (Sprite otherSprite : sprites) {
            otherSprites.add(otherSprite.getId());
            Sprite localSprite = getSprite(otherSprite.getId());
            if (localSprite != null) {
                localSprite.copyFrom(otherSprite);
            } else {
                addSprite(otherSprite);
            }
        }

        Set<Integer> currentSprites = new HashSet<>();
        for (Sprite sprite : getSprites()) {
            currentSprites.add(sprite.getId());
        }
        currentSprites.removeAll(otherSprites);
        for (Integer p : currentSprites) {
            removeSprite(p);
        }

    }

    @Override
    public void synchronizePlayers(List<Player> players) {
        Set<Integer> otherPlayers = new HashSet<>();
        for (Player otherPlayer : players) {
            otherPlayers.add(otherPlayer.getId());
            Player localPlayer = null;
            try {
                localPlayer = getPlayer(otherPlayer.getId());
            } catch (NoSuchElementException e) {
                //ignore
            }
            if (localPlayer != null) {
                localPlayer.copyFrom(otherPlayer);
            } else {
                addPlayer(otherPlayer);
            }
        }
        Set<Integer> currentPlayers = new HashSet<>();
        for (Player player : getPlayers()) {
            currentPlayers.add(player.getId());
        }
        currentPlayers.removeAll(otherPlayers);
        for (Integer p : currentPlayers) {
            removePlayer(p);
        }
    }

    public Player createPlayer() {
        return getModel().createPlayer();
    }

    public Sprite createSprite(String kind) {
        DefaultSprite defaultSprite = new DefaultSprite();
        defaultSprite.setKind(kind);
        return defaultSprite;
    }

    @Override
    public void setSize(double width, double height) {
        setSize(new ModelDimension(width, height));
    }

    public int attack(Sprite attacker, Sprite victim) {
        boolean inRange = false;
        int allDamage = 0;
        //set agressive
        for (SpriteWeapon weapon : attacker.getWeapons()) {
            if (weapon.isEnabled() && weapon.isActive()) {
                final ModelPoint ennemyPosition = victim.getLocation();
                DamageEffects damageEffects = weapon.fire(attacker, ennemyPosition);
                if (damageEffects == null) {
                    //out of range
                } else {
                    inRange = true;
                    int damageValue = damageEffects.getDamage(ennemyPosition, victim.getPlayerId());
                    if (damageValue > 0) {
                        damageValue = processShot(attacker, victim, damageValue);
                        if (damageValue > 0) {
                            allDamage += damageValue;
                            //set fighting
                            victim.addLife(damageValue);
                            enemyDamaged(attacker, victim, damageValue, weapon);
                        }
                    }
                }
            }
        }
        if (inRange) {
            return allDamage;
        } else {
            return Sprite.OUT_OF_RANGE;
        }
    }

    public int attack(Sprite attacker, ModelPoint victimPosition) {
        boolean inRange = false;
        int allDamage = 0;
        for (SpriteWeapon weapon : attacker.getWeapons()) {
            if (weapon.isEnabled() && weapon.isActive()) {
                DamageEffects damageEffects = weapon.fire(attacker, victimPosition);
                if (damageEffects == null) {
                    //out of range
                } else {
                    inRange = true;
                    for (Sprite other : findSprites(victimPosition)) {
                        if (other.isAttackable()) {
                            int damageValue = damageEffects.getDamage(victimPosition, other.getPlayerId());
                            if (damageValue > 0) {
                                for (SpriteArmor a : other.getArmors()) {
                                    SpriteArmorAction saa = getSpriteArmorAction(a.getClass());
                                    damageValue = saa.hit(a, damageValue, attacker);
                                }
                                if (damageValue > 0) {
                                    allDamage += damageValue;
                                    //set fighting
                                    other.addLife(-damageValue);
                                    enemyDamaged(attacker, other, damageValue, weapon);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (inRange) {
            return allDamage;
        } else {
            return Sprite.OUT_OF_RANGE;
        }
    }

    protected void enemyDamaged(Sprite sprite, Sprite unit, int damage, SpriteWeapon weapon) {
    }

    public int processShot(Sprite attacker, Sprite victim, int damage) {
        for (SpriteArmor unitArmor : victim.getArmors()) {
            SpriteArmorAction saa = getSpriteArmorAction(unitArmor.getClass());
            damage = saa.hit(unitArmor, damage, attacker);
        }
        if (damage > victim.getLife()) {
            damage = victim.getLife();
        }
        victim.setLife(victim.getLife() - damage);
        return damage;
    }

    @Override
    public void setSpriteArmorAction(Class<? extends SpriteArmor> armor, SpriteArmorAction action) {
        if (action != null) {
            spriteArmorActions.put(armor, action);
        } else {
            spriteArmorActions.remove(armor);
        }
    }

    @Override
    public SpriteArmorAction getSpriteArmorAction(Class<? extends SpriteArmor> armor) {
        SpriteArmorAction a = spriteArmorActions.get(armor);
        if (a == null) {
            throw new NoSuchElementException();
        }
        return a;
    }

    public ModelDimension getSize() {
        return getModel().getSize();
    }

    public void setSize(ModelDimension dimension) {
        getModel().setSize(dimension);
    }

    @Override
    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    @Override
    public void setPlayerFactory(PlayerFactory playerFactory) {
        if (!Objects.equals(this.playerFactory, playerFactory)) {
            PlayerFactory old = this.playerFactory;
            this.playerFactory = playerFactory;
            for (SceneEngineChangeListener listener : engineUpdateListeners) {
                listener.playerFactoryChanged(this, old, playerFactory);
            }
            propertyChangeSupport.firePropertyChange("playerFactory", old, playerFactory);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
    }

    public List<Sprite> findSpritesByPlayer(int playerId) {
        List<Sprite> list = new ArrayList<Sprite>();
        for (Sprite sprite : getSprites()) {
            if (sprite.getPlayerId() == playerId) {
                list.add(sprite);
            }
        }
        return list;
    }

    public <T extends Sprite> List<T> findSpritesByName(Class<T> type, String name) {
        if (name == null) {
            List<T> list = new ArrayList<T>();
            for (Sprite sprite : getSprites()) {
                if (sprite.getName() == null) {
                    list.add((T) sprite);
                }
            }
            return list;
        } else {
            List<T> list = new ArrayList<T>();
            for (Sprite sprite : getSprites()) {
                if (name.equals(sprite.getName())) {
                    list.add((T) sprite);
                }
            }
            return list;
        }
    }

    public <T extends Sprite> List<T> findSprites(Class<T> type) {
        List<T> list = new ArrayList<T>();
        for (Sprite sprite : getSprites()) {
            if (type.isAssignableFrom(sprite.getClass())) {
                list.add((T) sprite);
            }
        }
        return list;
    }

    public <T extends Sprite> List<T> findSpritesByPlayer(Class<T> type, int playerId) {
        List<T> list = new ArrayList<T>();
        for (Sprite sprite : getSprites()) {
            if (playerId == sprite.getPlayerId()) {
                if (type.isAssignableFrom(sprite.getClass())) {
                    list.add((T) sprite);
                }
            }
        }
        return list;
    }

    @Override
    public <T extends Sprite> T findSprite(Class<T> type) {
        for (Sprite sprite : getSprites()) {
            if (type.isAssignableFrom(sprite.getClass())) {
                return ((T) sprite);
            }
        }
        return null;
    }

    @Override
    public <T extends Sprite> T findSpriteByKind(String kind) {
        return findSpriteByKind(kind, null, null);
    }

    @Override
    public <T extends Sprite> T findSpriteByName(String name) {
        return findSpriteByName(name, null, null);
    }


    @Override
    public <T extends Sprite> T findUniqueSpriteByName(String name) {
        Sprite ok = findUniqueSpriteByNameOrNull(name);
        if (ok == null) {
            throw new IllegalArgumentException("No Such Sprite " + name);
        }
        return (T) ok;
    }

    @Override
    public List<Sprite> findSpritesByName(String name) {
        List<Sprite> ok = new ArrayList<>();
        for (Sprite sprite : getSprites()) {
            if (name != null && name.equals(sprite.getName())) {
                ok.add(sprite);
            }
        }
        return ok;
    }

    @Override
    public List<Sprite> findSpritesByKind(String name) {
        List<Sprite> ok = new ArrayList<>();
        for (Sprite sprite : getSprites()) {
            if (name != null && name.equals(sprite.getKind())) {
                ok.add(sprite);
            }
        }
        return ok;
    }

    @Override
    public <T extends Sprite> T findUniqueSpriteByNameOrNull(String name) {
        Sprite ok = null;
        for (Sprite sprite : getSprites()) {
            if (name != null && name.equals(sprite.getName())) {
                if (ok != null) {
                    throw new IllegalArgumentException(sprite.getName() + " is not unique. Multiple instances found");
                }
                ok = (sprite);
            }
        }
        return (T) ok;
    }

    @Override
    public <T extends Sprite> T findSpriteByName(String name, Integer playerId, Class<T> type) {
        for (Sprite sprite : getSprites()) {
            if (type == null || type.isAssignableFrom(sprite.getClass())) {
                if ((name == null && sprite.getName() == null) || name != null && name.equals(sprite.getName())) {
                    if (playerId == null || playerId.equals(sprite.getPlayerId())) {
                        return ((T) sprite);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public <T extends Sprite> T findSpriteByKind(String kind, Integer playerId, Class<T> type) {
        for (Sprite sprite : getSprites()) {
            if (type == null || type.isAssignableFrom(sprite.getClass())) {
                if ((kind == null && sprite.getKind() == null) || kind != null && kind.equals(sprite.getKind())) {
                    if (playerId == null || playerId.equals(sprite.getPlayerId())) {
                        return ((T) sprite);
                    }
                }
            }
        }
        return null;
    }

    public <T extends Sprite> T findSpriteByPlayer(Class<T> type, int playerId) {
        for (Sprite sprite : getSprites()) {
            if (playerId == sprite.getPlayerId()) {
                if (type.isAssignableFrom(sprite.getClass())) {
                    return ((T) sprite);
                }
            }
        }
        return null;
    }

    /**
     * private ModelTracker
     */
    private class ModelTracker extends SceneEngineModelAdapter {

        boolean stillFiringCollisionEvents = false;

        /**
         * {@inheritDoc }
         * call collisionManager
         */
        @Override
        public void spriteAdded(SceneEngineModel model, Sprite sprite) {
            if (stillFiringCollisionEvents) {
                return;
            }
//            fireCollisionEvents(sprite, null, sprite.getLocation());
            String kind = sprite.getKind();
            if(kind!=null) {
                SpriteTask instanceByKind = spriteTasks.getInstanceByKind(kind);
                if (instanceByKind != null) {
                    setSpriteTask(sprite.getId(), instanceByKind);
                } else {
                    Class<? extends SpriteTask> cls = spriteTasks.getBeanByKind(kind);
                    if (cls != null) {
                        setSpriteTask(sprite.getId(), spriteTasks.create(cls));
                    }
                }
                SpriteCollisionManager c_instanceByKind = spriteCollisionManagers.getInstanceByKind(kind);
                if (c_instanceByKind != null) {
                    setSpriteCollisionManager(sprite.getId(), c_instanceByKind);
                } else {
                    Class<? extends SpriteCollisionManager> c_cls = spriteCollisionManagers.getBeanByKind(kind);
                    if (c_cls != null) {
                        setSpriteCollisionManager(sprite.getId(), spriteCollisionManagers.create(c_cls));
                    }
                }
            }
            movingSprites.put(sprite.getId(), null);
        }

        @Override
        public void spriteRemoving(SceneEngineModel model, Sprite sprite) {
            setSpriteTask(sprite.getId(),(SpriteTask) null);
            setSpriteCollisionManager(sprite.getId(),(SpriteCollisionManager) null);
        }

        /**
         * {@inheritDoc }
         * call collisionManager
         */
        @Override
        public void spriteMoved(SceneEngineModel model, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation) {
            if (stillFiringCollisionEvents) {
                return;
            }
            //update movingSprites list
            int spriteId = sprite.getId();
            ModelPoint initialLocation = movingSprites.get(spriteId);
            if (initialLocation == null) {
                movingSprites.put(spriteId, oldLocation);
            } else if (newLocation != null && newLocation.equals(initialLocation)) {
                movingSprites.remove(spriteId);
            }

            //fire Collision Events
            //fireCollisionEvents(sprite, oldLocation, newLocation);
        }

        protected void fireCollisionEvents() {
            stillFiringCollisionEvents = true;
            for (Map.Entry<Integer, ModelPoint> entry : movingSprites.entrySet()) {
                Sprite s = getSprite(entry.getKey());
                if (s != null) {
                    fireCollisionEvents(s, entry.getValue(), s.getLocation());
                }
            }
            stillFiringCollisionEvents = false;
        }

        protected void fireCollisionEvents(Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation) {
            SpriteCollisionManager scm = getSpriteCollisionManager(sprite);
            for (Collision collision : collisionManager.detectCollisions(DefaultSceneEngine.this, sprite/*, oldLocation, newLocation*/, true, true, true)) {
                if (collision instanceof SpriteCollision) {
                    SpriteCollision sc = (SpriteCollision) collision;
                    if (scm != null) {
                        scm.collideWithSprite(sc);
                    }
                    SpriteCollisionManager oscm = getSpriteCollisionManager(sc.getOther());
                    if (oscm != null) {
                        oscm.collideWithSprite(new SpriteCollision(sc.getSceneEngine(), sprite, sc.getSpriteCollisionSides(), sc.getOther(), sc.getOtherCollisionSides(), false, sc.getCollisionTiles(), sc.getOther().getLocation(), sc.getOther().getLocation()));
                    }
                    for (SpriteCollisionManager spriteCollisionManager : globalSpriteCollisionManagers) {
                        spriteCollisionManager.collideWithSprite(sc);
                    }
                } else if (collision instanceof BorderCollision) {
                    BorderCollision sc = (BorderCollision) collision;
                    if (scm != null) {
                        scm.collideWithBorder(sc);
                    }
                    for (SpriteCollisionManager spriteCollisionManager : globalSpriteCollisionManagers) {
                        spriteCollisionManager.collideWithBorder(sc);
                    }
                } else if (collision instanceof TileCollision) {
                    TileCollision sc = (TileCollision) collision;
                    if (scm != null) {
                        scm.collideWithTile(sc);
                    }
                    for (SpriteCollisionManager spriteCollisionManager : globalSpriteCollisionManagers) {
                        spriteCollisionManager.collideWithTile(sc);
                    }
                }
            }
        }
        public void reset(){
            movingSprites.clear();
        }
    }

    public Object getCompanionObject() {
        return companionObject;
    }

    public void setCompanionObject(Object companionObject) {
        this.companionObject = companionObject;
    }
}
