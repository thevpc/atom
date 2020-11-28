/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.ioc.AtomIoCContainer;
import net.thevpc.gaming.atom.model.GameEngineProperties;

import java.util.List;

/**
 * GameEngine does all the business logic of the the game
 * A Game bundles a set of Scenes and is responsible of switching from one Scene to another.
 * At every time only one Scene is active.
 * GameEngine behavior is described using a state (GameEngineState) that follows a state diagram.
 * GameEngine state is calculated according to invocations of the start,stop,pause,resume and dispose methods.
 * The simplest way to create a GameEngine is to instantiate a DefaultGameEngine.
 * <p/>
 * In most cases GameEngine usage is as follows :
 * <p/>
 * <pre>
 * public class MyGame {
 *
 *     public static void main(String[] args) {
 *         try {
 *             //create a game sceneEngine instance
 *             DefaultGameEngine sceneEngine = new DefaultGameEngine();
 *             //add scene engines to the game sceneEngine, the first one added is set to active
 *             sceneEngine.addScene(new MySceneEngine());
 *
 *             //create game display (a simple JFrame based implementation)
 *             DefaultGame game=new DefaultGame(sceneEngine);
 *             //bind display "MyScene" to sceneEngine "MySceneEngine"
 *             game.bindScene(new MyScene(),MySceneEngine.class);
 *
 *             //start the game
 *             game.start();
 *
 *         } catch (Exception ex) {
 *             ex.printStackTrace();
 *         }
 *     }
 * }
 *
 * </pre>
 * <p/>
 * GameEngine state diagram is described as follows.
 * <table border=1>
 * <tr><td>From/To     </td><td>UNINITALIZED</td><td>STARTING</td><td>STARTED</td><td>PAUSING</td><td>PAUSING</td><td>RESUMING</td><td>STOPPING</td><td>STOPPED</td><td>DISPOSING</td><td>DISPOSED</td></tr>
 * <tr><td>UNINITALIZED</td><td>            </td><td>   X    </td><td>       </td><td>       </td><td>       </td><td>        </td><td>        </td><td>       </td><td>         </td><td>        </td></tr>
 * <tr><td>STARTING    </td><td>            </td><td>        </td><td>   X   </td><td>       </td><td>       </td><td>        </td><td>        </td><td>       </td><td>         </td><td>        </td></tr>
 * <tr><td>STARTED     </td><td>            </td><td>        </td><td>       </td><td>  X    </td><td>       </td><td>        </td><td>   X    </td><td>       </td><td>   X     </td><td>        </td></tr>
 * <tr><td>PAUSING     </td><td>            </td><td>        </td><td>       </td><td>       </td><td>  X    </td><td>        </td><td>        </td><td>       </td><td>         </td><td>        </td></tr>
 * <tr><td>PAUSED      </td><td>            </td><td>        </td><td>       </td><td>       </td><td>       </td><td>   X    </td><td>   X    </td><td>       </td><td>   X     </td><td>        </td></tr>
 * <tr><td>RESUMING    </td><td>            </td><td>        </td><td>   X   </td><td>       </td><td>       </td><td>        </td><td>        </td><td>       </td><td>         </td><td>        </td></tr>
 * <tr><td>STOPPING    </td><td>            </td><td>        </td><td>       </td><td>       </td><td>       </td><td>        </td><td>        </td><td>   X   </td><td>         </td><td>        </td></tr>
 * <tr><td>STOPPED     </td><td>            </td><td>   X    </td><td>       </td><td>       </td><td>       </td><td>        </td><td>        </td><td>       </td><td>   X     </td><td>        </td></tr>
 * <tr><td>DISPOSING   </td><td>            </td><td>        </td><td>       </td><td>       </td><td>       </td><td>        </td><td>        </td><td>       </td><td>         </td><td>   X    </td></tr>
 * <tr><td>DISPOSED    </td><td>            </td><td>        </td><td>       </td><td>       </td><td>       </td><td>        </td><td>        </td><td>       </td><td>         </td><td>        </td></tr>
 * </table>
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @see SceneEngine
 * @see DefaultGameEngine
 */
public interface GameEngine {

    /**
     * Start the game sceneEngine.
     * If a Scene sceneEngine is active, it will be started (if not yet) and activated
     */
    public void start();

    /**
     * Stop the game sceneEngine
     * If a Scene sceneEngine is active, it will be deactivated (if not yet) and stopped
     * All scenes are stopped
     */
    public void stop();

    /**
     * Pause the game sceneEngine
     * If a Scene sceneEngine is active, it will be paused
     */
    public void pause();

    /**
     * Resume the game sceneEngine
     * If a Scene sceneEngine is active, it will be resumed
     */
    public void resume();

    /**
     * dispose the game sceneEngine
     * If a Scene sceneEngine is active, it will be deactivated (if not yet)
     * All scenes are stopped and disposed
     */
    public void dispose();

    /**
     * The current sceneEngine state
     *
     * @return EngineState
     * @see GameEngineState
     */
    public GameEngineState getState();

    public GameEngineProperties getProperties();

    /**
     * Add a <code>GameEngineStateListener</code> to the GameEngine
     *
     * @param engineListener the listener to be added
     */
    public void addStateListener(GameEngineStateListener engineListener);

    /**
     * Remove a <code>GameEngineStateListener</code> from the GameEngine, if found
     *
     * @param engineListener the listener to be removed
     */
    public void removeStateListener(GameEngineStateListener engineListener);

    /**
     * Add a <code>GameEngineChangeListener</code> to the GameEngine
     *
     * @param engineListener the listener to be added
     */
    public void addChangeListener(GameEngineChangeListener engineListener);

    /**
     * Remove a <code>GameEngineChangeListener</code> from the GameEngine, if found
     *
     * @param engineListener the listener to be removed
     */
    public void removeChangeListener(GameEngineChangeListener engineListener);

    /**
     * Add a scene sceneEngine to the GameEngine.
     * If there is no scene sceneEngine set as current yet (see {@link SceneEngine#), this SceneEngine will be set as current
     *
     * @param sceneEngine the scene sceneEngine to be added
     * @throws java.util.IllegalArgumentException raised if sceneId is already bound to another scene sceneEngine
     */
    public void addScene(SceneEngine sceneEngine);

    /**
     * Remove a scene sceneEngine from the GameEngine.
     *
     * @param sceneId the id of the scene to be removed
     * @throws java.util.NoSuchElementException raised if sceneId is null or does not denote a valid scene sceneEngine
     */
    public void removeScene(String sceneId);

    /**
     * Remove a scene sceneEngine from the GameEngine.
     * same as <code>removeSceneEngine(sceneType.getName())</code>.
     *
     * @param sceneType the type of the scene to be removed
     * @throws java.util.NoSuchElementException raised if sceneId is null or does not denote a valid scene sceneEngine
     */
    public void removeScene(Class<? extends SceneEngine> sceneType);

    /**
     * resolve SceneEngine by its Id
     *
     * @param sceneId id of the scene sceneEngine to retrieve
     * @return SceneEngine corresponding to the given <code>sceneId</code>
     * @throws java.util.NoSuchElementException raised if sceneId is null or does not denote a valid scene sceneEngine
     */
    public SceneEngine getScene(String sceneId);

    /**
     * return true if the scene is registered
     * @param sceneId to check for existence
     * @return true if the scene is registered
     */
    public boolean containsScene(String sceneId);

    /**
     * @param <T>       a subclass of SceneEngine
     * @param sceneType type of the scene sceneEngine
     * @return corresponding to the the id <code>sceneType.getName()</code>
     * @throws java.util.NoSuchElementException raised if sceneId is null or does not denote a valid scene sceneEngine
     */
    public <T extends SceneEngine> T getScene(Class<T> sceneType);

    /**
     * array of all declared scene engines
     *
     * @return array of all declared scene engines
     */
    List<SceneEngine> getScenes();

    /**
     * Deactivate the current activated scene and Activate the scene with id <code>sceneId</code>.
     * Active Scene is the one displayed.
     * When a scene is activated, its <code>activate()</code> method is invoked.
     * When a scene is deactivated, its <code>deactivate()</code> method is invoked.
     * GameEngine guarantees that a scene is activate after started.
     * If the scene is not started and the GameEngine has already started,
     * the scene sceneEngine will be started before being activated.
     *
     * @param sceneId id of the scene to activate
     * @throws java.util.NoSuchElementException raised if sceneId is null or does not denote a valid scene sceneEngine
     */
    public void setActiveSceneEngine(String sceneId);

    /**
     * return active scene sceneEngine if defined or null
     *
     * @return active scene sceneEngine if defined or null
     */
    public SceneEngine getActiveScene();

    /**
     * same as <code>setActiveSceneEngine(sceneType.getName())</code>
     *
     * @param sceneType sceneId id of the scene to activate
     * @throws java.util.NoSuchElementException raised if sceneId is null or does not denote a valid scene sceneEngine
     */
    public void setActiveSceneEngine(Class<? extends SceneEngine> sceneType);

    /**
     * create new scene engine
     * @param sceneId scene engine Id
     * @return new scene engine Id
     */
    public SceneEngine createScene(String sceneId);

    /**
     * add new scene engine named {@code sceneId}
     * @param sceneId scene engine id
     * @return new scene engine
     */
    public SceneEngine addScene(String sceneId);

    /**
     * return IoC container instance
     * @return IoC container instance 
     */
    public AtomIoCContainer getContainer();

    /**
     * set IoC container instance
     * @param container IoC container instance
     */
    public void setContainer(AtomIoCContainer container);

    /**
     * default scene engine id
     * @return default scene
     */
    public String getDefaultSceneId();

    /**
     * set default scene
     * @param sceneEngineId default scene id 
     */
    public void setDefaultSceneId(String sceneEngineId);

}
