/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.model.*;

import net.vpc.gaming.atom.engine.collision.Collision;
import net.vpc.gaming.atom.engine.collision.SceneCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollisionManager;
import net.vpc.gaming.atom.extension.SceneEngineExtension;
import net.vpc.gaming.atom.presentation.Game;

import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * A Scene is an animated phase of the game that handles its own logic. Usually,
 * Scenes are decoupled, but different scenes may share information as far as
 * that belong to the same Game. For instance, a simple game may have the scenes
 * :
 * <ul>
 * <li>a Welcome Scene that shows up the famous "press key to continue"</li>
 * <li>a Play Board Scene that shows the game to play</li>
 * <li>a High scores Scene that displays High scores</li>
 * </ul>
 * <p/>
 * The simplest Way to create a SceneEngine is to subclass DefaultSceneEngine.
 * Here is an example :
 * <pre>
 *  public class MySceneEngine extends DefaultSceneEngine {
 *
 *     public MySceneEngine() {
 *     }
 * }
 * </pre>
 * <p/>
 * Usually, it is appropriate (though not necessary if sprites are very dynamic)
 * to define one own SceneModel which contains one's sprites. One can initialize
 * model in the overridden method
 * <code>sceneActivating()</code> Here is another more accurate example :
 * <pre>
 *  public class MySceneEngine extends DefaultSceneEngine {
 *
 *     public MySceneEngine() {
 *         setModel(new MySceneModel());
 *         addFrameListener(new FogOfWarFeature(5));
 *     }
 *
 *     @Override
 *     protected void sceneActivating() {
 *         Person person = getModel().findSprite(Person.class);
 *         person.setCollisionManager(new SimpleSpriteCollisionManager(true,true,false));
 *         person.setTask(new HoldPositionSpriteTask());
 *         person.setLocation(randomVacantLocation(person));
 *     }
 * }
 * </pre>
 * <p/>
 * SceneEngine has its own State (SceneEngineState) that follows a predefined
 * State diagram
 * <p/>
 * GameEngine state diagram is described as follows.
 * <table border=1>
 * <tr><td>From/To
 * </td><td>UNINITALIZED</td><td>INITALIZING</td><td>INITALIZED</td><td>STARTING</td><td>ACTIVATING</td><td>ACTIVATED</td><td>DEACTIVATING</td><td>DEACTIVATED</td><td>PAUSING</td><td>PAUSING</td><td>RESUMING</td><td>STOPPING</td><td>STOPPED</td><td>DISPOSING</td><td>DISPOSED</td></tr>
 * <tr><td>UNINITALIZED</td><td> </td><td> X </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td></tr>
 * <tr><td>INITALIZING </td><td> </td><td> </td><td> X </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td></tr>
 * <tr><td>INITALIZED </td><td> </td><td> </td><td> </td><td> X </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td></tr>
 * <tr><td>STARTING </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> X </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td></tr>
 * <tr><td>ACTIVATING </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> X </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td></tr>
 * <tr><td>ACTIVATED </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> X </td><td> </td><td> X </td><td> </td><td> </td><td> X </td><td>
 * </td><td> X </td><td> </td></tr>
 * <tr><td>DEACTIVATING</td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td></tr>
 * <tr><td>DEACTIVATED </td><td> </td><td> </td><td> </td><td> </td><td> X
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> X
 * </td><td> </td><td> X </td><td> </td></tr>
 * <tr><td>PAUSING </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> X </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td></tr>
 * <tr><td>PAUSED </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> X </td><td> X </td><td>
 * </td><td> X </td><td> </td></tr>
 * <tr><td>RESUMING </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * X </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td></tr>
 * <tr><td>STOPPING </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> X
 * </td><td> </td><td> </td></tr>
 * <tr><td>STOPPED </td><td> </td><td> </td><td> </td><td> X </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> X </td><td> </td></tr>
 * <tr><td>DISPOSING </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> X </td></tr>
 * <tr><td>DISPOSED </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td><td> </td><td> </td><td> </td><td> </td><td>
 * </td><td> </td><td> </td></tr>
 * </table>
 * <p/>
 * SceneEngine may or may not run a SingleThread. In all cases, it is
 * recommended to run SceneModel updates from within SceneEngine Thread. To Run
 * updates into SceneEngine Thread use the <code>invokeLater<method> Method.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngine {

    /**
     * return Scene Engine Id. If the returned id is Null, return
     * getClass().getName() instead.
     *
     * @return Scene Engine Id
     */
    public String getId();

    /**
     * update Scene Engine Id. Update is possible only in initialize method
     * (state UNINITIALIZED or INITIALIZING)
     *
     * @param id new Scene Engine Id
     */
    public void setId(String id);

    /**
     * Called once to initialize the scene Engine
     *
     * @param gameEngine associated GameEngine
     */
    public void init(GameEngine gameEngine);

    /**
     * Start the Scene Engine. The scene passes to the state "DEACTIVATED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to STARTING is not allowed
     */
    public void start();

    /**
     * Stop the Scene Engine. The scene passes to the state "STOPPED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to STOPPING is not allowed
     */
    public void stop();

    /**
     * Pause the Scene Engine. The scene passes to the state "PAUSED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to PAUSING is not allowed
     */
    public void pause();

    /**
     * Resume the Scene Engine. The scene passes to the state "ACTIVATED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to RESUMING is not allowed
     */
    public void resume();

    /**
     * Activate the Scene Engine. The scene passes to the state "ACTIVATED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to ACTIVATING is not allowed
     */
    public void activate();

    /**
     * Deactivate the Scene Engine. The scene passes to the state "DECATIVATED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to DEACTIVATING is not allowed
     */
    public void deactivate();

    /**
     * Dispose the Scene Engine. The scene passes to the state "DISPOSED"
     *
     * @throws IllegalStateException raised when transition from actual State
     *                               to DISPOSING is not allowed
     */
    public void dispose();

    /**
     * current sceneEngine state
     *
     * @return current sceneEngine state
     */
    public SceneEngineState getSceneEngineState();

    /**
     * return SceneModel
     *
     * @param <T> Scene Model Type
     * @return Scene Model
     */
    public <T extends SceneEngineModel> T getModel();

    /**
     * update SceneModel if model changes, fires
     * <code>SceneEngineUpdateListener.modelChanged</code>
     *
     * @param model new SceneModel
     */
    public void setModel(SceneEngineModel model);

    /**
     * return associated GameEngine
     *
     * @return GameEngine associated GameEngine
     */
    public <T extends GameEngine> T getGameEngine();

    /**
     * Anticipates Sprite move to check if any collision will be fired when
     * sprite location changes to
     * <code>newLocation</code>. This method does not update Sprite location. It
     * does only check for collision using the defined Collision Manager.
     *
     * @param sprite          sprite to move
     * @param newLocation     new sprite location
     * @param borderCollision check borderCollision
     * @param tileCollision   check tile borderCollision
     * @param spriteCollision check Sprite Collision
     * @return Detected Collisions or empty array (not null)
     */
    public List<Collision> detectCollisions(Sprite sprite, ModelPoint newLocation, boolean borderCollision, boolean tileCollision, boolean spriteCollision);

    /**
     * Add a SpriteCollisionManager. SpriteCollisionManager will handle
     * collision for ALL Sprites after Sprite's own SpriteCollisionManager has
     * handled is logic
     *
     * @param collisionManager manager to register
     */
    public void addSpriteCollisionManager(SpriteCollisionManager collisionManager);

    /**
     * Remove a SpriteCollisionManager
     *
     * @param collisionManager manager to unregister
     */
    public void removeSpriteCollisionManager(SpriteCollisionManager collisionManager);

    public void setSpriteCollisionManager(int spriteId, SpriteCollisionManager collisionManager);

    public void setSpriteCollisionManager(Sprite sprite, SpriteCollisionManager collisionManager);

    public void setSpriteCollisionManager(String kind, SpriteCollisionManager collisionManager);

    public void setSpriteCollisionManager(int spriteId, Class<? extends SpriteCollisionManager> collisionManager) ;

    public void setSpriteCollisionManager(String spriteKind, Class<? extends SpriteCollisionManager> collisionManager) ;

    public SpriteCollisionManager getSpriteCollisionManager(int spriteId);

    public SpriteCollisionManager getSpriteCollisionManager(Sprite sprite);

    public SpriteCollisionManager getSpriteCollisionManager(String sprite);

    public List<Sprite> findSpritesByName(String name) ;

    public List<Sprite> findSpritesByKind(String name) ;


    /**
     * add a SceneEngineChangeListener
     *
     * @param listener listener to register
     */
    public void addSceneEngineChangeListener(SceneEngineChangeListener listener);

    /**
     * remove a SceneEngineChangeListener
     *
     * @param listener listener to unregister
     */
    public void removeSceneEngineChangeListener(SceneEngineChangeListener listener);

    /**
     * add a SceneEngineFrameListener
     *
     * @param listener listener to register
     */
    public void addSceneFrameListener(SceneEngineFrameListener listener);

    /**
     * remove a SceneEngineFrameListener
     *
     * @param listener listener to unregister
     */
    public void removeSceneFrameListener(SceneEngineFrameListener listener);

    /**
     * add a SceneEngineFrameListener
     *
     * @throws NullPointerException raised when <code>task</code> is null
     */
    public void addSceneTask(SceneEngineTask task);

    /**
     * remove a SceneEngineTask
     *
     * @param task task to unregister
     * @throws NullPointerException raised when <code>task</code> is null
     */
    public void removeSceneTask(SceneEngineTask task);

    /**
     * add a SceneEngineExtension
     *
     * @param extension listener to register
     * @throws NullPointerException     raised when <code>extension</code> is null
     * @throws IllegalArgumentException raised when un extension with the same
     *                                  ID than <code>extension</code> is already registered
     */
    public void installExtension(SceneEngineExtension extension);

    /**
     * remove a SceneEngineExtension
     *
     * @param extensionId extension ID
     * @throws NullPointerException     raised when <code>extensionId</code> is null
     * @throws IllegalArgumentException raised when extension is not yet
     *                                  installed
     */
    public void uninstallExtension(String extensionId);

    /**
     * Find installed Extension
     *
     * @param extensionId extension ID
     * @return Extension with id <code>extensionId</code> or null if not found
     * @throws NullPointerException raised when <code>extensionId</code> is null
     */
    public SceneEngineExtension getExtension(String extensionId);

    /**
     * return Extension with Class Name
     *
     * @param <T>           Extension Type
     * @param extensionType Extension Class
     * @return Extension with id <code>extensionType.getName()</code>
     */
    public <T extends SceneEngineExtension> T getExtension(Class<? extends T> extensionType);

    /**
     * Array list of the installed extensions. Empty arrays if no extension is
     * installed
     */
    public SceneEngineExtension[] getExtensions();

    /**
     * add a SceneEngineStateListener
     *
     * @param listener listener to register
     */
    public void addStateListener(SceneEngineStateListener listener);

    /**
     * remove a SceneEngineStateListener
     *
     * @param listener listener to unregister
     */
    public void removeStateListener(SceneEngineStateListener listener);

    /**
     * return current collision manager
     *
     * @return current collision manager
     */
    public SceneCollisionManager getSceneCollisionManager();

    /**
     * change collision manager if collisionManager changes, fires
     * <code>SceneEngineUpdateListener.collisionManagerChanged</code>
     *
     * @param collisionManager new collisionManager
     */
    public void setSceneCollisionManager(SceneCollisionManager collisionManager);

    /**
     * return configured Frames per Second value
     *
     * @return Frames per Second value
     */
    public int getFps();

    /**
     * return effective Frames per Second value
     *
     * @return effective Frames per Second
     */
    public int getEffectiveFps();

    /**
     * update Frames per Second value if fps changes, fires
     * <code>SceneEngineUpdateListener.fpsChanged</code>
     *
     * @param fps new value
     * @throws IllegalArgumentException raised if <code>fps&lt;=0</code>
     */
    public void setFps(int fps);

    /**
     * invokes the runnable the the Game Thread
     *
     * @param runnable runnable to execute
     */
    public void invokeLater(Runnable runnable);

    /**
     * true if the spite has moved the last frame
     */
    public boolean hasMovedLastFrame(int spriteId);

    public boolean hasMovedLastFrame(String spriteName);

    public <T extends SpriteTask> T getSpriteTask(int spriteId, Class<T> type);

    public <T extends SpriteTask> T getSpriteTask(Sprite sprite, Class<T> type);

    public <T extends SpriteTask> T getSpriteTask(String name, Class<T> type);

    public <T extends SpriteTask> T getSpriteTask(int spriteId);

    public <T extends SpriteTask> T getSpriteTask(String spriteId);

    public <T extends SpriteTask> T getSpriteTask(Sprite sprite);

    public void setSpriteTask(int spriteId, SpriteTask task);

    public void setSpriteTask(Sprite sprite, SpriteTask task);

    public void setSpriteTask(String spriteName, SpriteTask task);

    public void setSpriteTask(String spriteKind, Class<? extends SpriteTask> task) ;

    public long getFrame();

    public Player addPlayer();

    public int addPlayer(final Player player);

    public int addSprite(Sprite sprite);

    //    public void addSprite(Sprite sprite, int id);
    public List<Sprite> findSprites(ModelPoint point);

    public List<Sprite> findSprites(ModelPoint point, int layer);

    public List<Sprite> findSprites(ModelBox rect);

    public List<Sprite> findSpritesByPlayer(int playerId);

    public <T extends Sprite> List<T> findSpritesByName(Class<T> type, String name);

    public <T extends Sprite> List<T> findSprites(Class<T> type);

    public <T extends Sprite> List<T> findSpritesByPlayer(Class<T> type, int playerId);

    public <T extends Sprite> T findSprite(Class<T> type);

    public <T extends Sprite> T findSpriteByPlayer(Class<T> type, int playerId);

    public <T extends Sprite> T findSpriteByKind(String kind);

    public <T extends Sprite> T findSpriteByKind(String kind, Integer playerId, Class<T> type);

    public <T extends Sprite> T findSpriteByName(String name);

    public <T extends Sprite> T findUniqueSpriteByName(String name);

    public <T extends Sprite> T findUniqueSpriteByNameOrNull(String name);

    public <T extends Sprite> T findSpriteByName(String name, Integer playerId, Class<T> type);

    //     <T> List<T> findSprites(int playerId, Class<T> type);
    public Tile findTile(ModelPoint point);

    public List<Tile> findTiles(Path2D rect);

    public List<Tile> findTiles(ModelBox rect);

    public int getPlayersCount();

    public List<Player> getPlayers();

    public Player getPlayer(int id);

    public Sprite getSprite(int id);

    public List<Sprite> getSprites();

    void removePlayer(int player);

    void removeSprite(int sprite);

    void resetSprites();
    void resetPlayers();

    public Tile[] getObstacleTiles();

    public Tile[] getTiles();

    public Tile[][] getTileMatrix();

    public void synchronizeSprites(List<Sprite> sprites);

    public void synchronizePlayers(List<Player> players);

    public Player createPlayer() ;

    public Sprite createSprite(String kind);

    public void setSize(double width, double height);

    public int attack(Sprite attacker, Sprite victim);

    /**
     * >=0 if some damage -1 if in range b
     *
     * @return
     */
    public int attack(Sprite sprite, ModelPoint victim);

    public int processShot(Sprite attacker, Sprite victim, int damage);

    public void setSpriteArmorAction(Class<? extends SpriteArmor> armor, SpriteArmorAction action);

    public SpriteArmorAction getSpriteArmorAction(Class<? extends SpriteArmor> armor);

    public ModelDimension getSize();

    public void setSize(ModelDimension dimension);

    public PlayerFactory getPlayerFactory();

    public void setPlayerFactory(PlayerFactory playerFactory);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String property, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String property, PropertyChangeListener listener);

    public ModelPoint getLastSpritePosition(int s);


    public Object getCompanionObject() ;

    public void setCompanionObject(Object companionObject) ;

}
