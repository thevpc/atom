package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.GameEngineChangeAdapter;
import net.thevpc.gaming.atom.engine.GameEngineStateAdapter;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.ioc.AtomIoCContainer;
import net.thevpc.gaming.atom.ioc.GameIoCContainer;
import net.thevpc.gaming.atom.model.DefaultGameModel;
import net.thevpc.gaming.atom.model.GameProperties;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.util.RepeatingReleasedEventsFixer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import net.thevpc.gaming.atom.util.AtomUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 11:11:45
 */
public class DefaultGame implements Game {

    private Image image;
    private GameEngine gameEngine;
    private JFrame window;
    private String currentSceneEngineId;
    private Scene currentScene;
    private GameProperties gameModel;
    //private PropertyChangeSupport pcs;
//    private boolean started;
    private SceneFactory sceneFactory = new DefaultSceneFactory();
    private GameStateTracker gameEngineStateListener = new GameStateTracker();
    private GameChangeTracker gameEngineChangeListener = new GameChangeTracker();
    private Map<String, Scene> loadedScenes = new HashMap<String, Scene>();
    //    private GameViewEventListenerAdapter adapter = new GameViewEventListenerAdapter();
    private PropertyChangeListener sceneEngineModelChangeListener = new WindowUpdaterPropertyChangeListener();
    private PropertyChangeListener sceneModelChangeListener = sceneEngineModelChangeListener;
    private AtomIoCContainer container;

    static {
        RepeatingReleasedEventsFixer.installOnNeed();
    }

    public DefaultGame() {
        this(null);
    }

    public DefaultGame(GameEngine gameEngine) {
        gameModel = new DefaultGameModel();
        if (gameEngine != null) {
            this.gameEngine = gameEngine;
            this.gameEngine.addStateListener(gameEngineStateListener);
            this.gameEngine.addChangeListener(gameEngineChangeListener);
        }
        container = new GameIoCContainer(this);
        setIcon("/net/thevpc/gaming/atom/atom-color.png",DefaultGame.class);
    }

    @Override
    public GameProperties getProperties() {
        return gameModel;
    }

    @Override
    public void setModel(GameProperties gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public GameEngine getGameEngine() {
        return gameEngine;
    }

    @Override
    public void setGameEngine(GameEngine gameEngine) {
        GameEngine old = this.gameEngine;
        if (old != null) {
            old.removeStateListener(gameEngineStateListener);
            old.removeChangeListener(gameEngineChangeListener);
        }
        this.gameEngine = gameEngine;
        if (gameEngine != null) {
            this.gameEngine.addStateListener(gameEngineStateListener);
            this.gameEngine.addChangeListener(gameEngineChangeListener);
        }
    }

    @Override
    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }

    @Override
    public void setSceneFactory(SceneFactory sceneViewFactory) {
        this.sceneFactory = sceneViewFactory;
    }

    @Override
    public void addScene(Scene scene, String sceneEnginId) {
        if (sceneEnginId == null) {
            sceneEnginId = scene.getId();
        }
        Scene old = loadedScenes.get(scene.getId());
        if (old != null) {
            return;
        }
        SceneEngine engine = getGameEngine().getScene(sceneEnginId);
        scene.init(engine, this);
        loadedScenes.put(sceneEnginId, scene);
    }

    @Override
    public void addScene(Scene scene, Class<? extends SceneEngine> sceneType) {
        String sceneId = sceneType.getName();
        AtomSceneEngine a = sceneType.getAnnotation(AtomSceneEngine.class);
        if (a != null && a.id().length() > 0) {
            sceneId = a.id();
        }
        if (!getGameEngine().containsScene(sceneId)) {
            SceneEngine b = (SceneEngine) getContainer().getBean(sceneType);
            getGameEngine().addScene(b);
        }
        addScene(scene, sceneId);
    }

    @Override
    public void addScene(Scene scene, SceneEngine sceneEngine) {
        getGameEngine().addScene(sceneEngine);
        addScene(scene, sceneEngine.getId());
    }

    @Override
    public void addScene(Scene scene) {
        getGameEngine().addScene(getGameEngine().createScene(scene.getId()));
        addScene(scene, scene.getId());
    }

    @Override
    public Scene createScene() {
        return new DefaultScene(new ViewDimension(1, 1, 1));
    }

    @Override
    public Scene addScene(SceneEngine sceneEngine) {
        Scene scene = createScene();
        addScene(scene, sceneEngine.getId());
        return scene;
    }

    @Override
    public void addSceneEngine(SceneEngine sceneEngine) {
        getGameEngine().addScene(sceneEngine);
    }

    @Override
    public Scene addScene(String sceneId) {
        Scene scene = createScene();
        SceneEngine sceneEngine = getGameEngine().createScene(sceneId);
        getGameEngine().addScene(sceneEngine);
        addScene(scene, sceneEngine.getId());
        return scene;
    }

    @Override
    public SceneEngine addSceneEngine(String sceneID) {
        return getGameEngine().addScene(sceneID);
    }

    @Override
    public Scene getScene(String sceneId) {
        Scene v = loadedScenes.get(sceneId);
        if (v == null) {
            v = getSceneFactory().createScene(sceneId);
            if (v == null) {
                throw new NoSuchElementException("Scene not found : " + sceneId);
            }
            loadedScenes.put(sceneId, v);
        }
        return v;
    }

    @Override
    public SceneEngine getSceneEngine(String sceneEngineId) {
        return getGameEngine().getScene(sceneEngineId);
    }

    @Override
    public List<Scene> getScenes() {
        return new ArrayList<>(loadedScenes.values());
    }

    @Override
    public List<Scene> findScenesBySceneEngineId(String sceneEnginId) {
        java.util.List<Scene> all = new ArrayList<>();
        for (Map.Entry<String, Scene> e : loadedScenes.entrySet()) {
            String id = e.getValue().getSceneEngine().getId();
            if (id.equals(sceneEnginId)) {
                all.add(e.getValue());
            }
        }
        return all;
    }

//    @Override
//    public void showScene(Class<? extends SceneEngine> sceneType) {
//        showScene(sceneType.getName());
//    }
//    @Override
    public void updateSceneShowing(/*String sceneID*/) {
        SceneEngine sceneEngine = gameEngine.getActiveScene();

        Scene scene = getScene(sceneEngine.getId());

        Scene oldScreen = this.currentScene;
        if (this.currentScene != null) {
//            this.sceneView.removeGameViewEventListener(adapter);
            this.currentScene.getSceneEngine().removePropertyChangeListener("size", sceneEngineModelChangeListener);
            this.currentScene.removePropertyChangeListener("title", sceneEngineModelChangeListener);
            this.currentScene.removePropertyChangeListener("camera", sceneModelChangeListener);
            this.currentScene.removePropertyChangeListener("size", sceneModelChangeListener);
            this.currentScene.stop();
        }
        this.currentScene = scene;
        this.currentSceneEngineId = scene.getSceneEngine().getId();
        scene.start();
        sceneEngine.addPropertyChangeListener("size", sceneEngineModelChangeListener);
        scene.addPropertyChangeListener("title", sceneEngineModelChangeListener);
        scene.addPropertyChangeListener("camera", sceneModelChangeListener);
        scene.addPropertyChangeListener("size", sceneModelChangeListener);
        JFrame f = getWindow();
//        f.removeAll();
        if (oldScreen != null) {
            f.remove(oldScreen.toComponent());
        }
        JComponent component = scene.toComponent();
        f.add(component);
        component.requestFocus();
        component.requestFocusInWindow();
//        f.add(screen.toComponent());
        updateWindow();
        f.invalidate();
        f.validate();
        f.repaint();
    }

    private void updateWindow() {
        if (window != null && currentScene != null) {
            window.setIconImage(null);
            window.setTitle(currentScene.getTitle());
            window.setIconImage(getIcon());
            ViewBox vp = currentScene.getCamera().getViewBounds();
            window.setSize(vp.getWidth(), vp.getHeight() + 30);
        }
    }

    public JFrame getWindow() {
        if (window == null) {
            boolean b = false;
            // a workaround in JDK7
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //
            }

            if (SwingUtilities.isEventDispatchThread()) {
                window = new JFrame();
                window.setLayout(new BorderLayout());
                window.setSize(600, 400);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                updateWindow();
            } else {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        window = new JFrame();
                        window.setLayout(new BorderLayout());
                        window.setSize(600, 400);
                        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        updateWindow();
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
//            frame.addKeyListener(new KeyAdapter() {
//
//                @Override
//                public void keyPressed(KeyEvent e) {
//                    DefaultGameView.this.keyPressed(e);
//                }
//            });
        }
        return window;
    }

    @Override
    public void start() {
        if (gameEngine != null) {
            switch (gameEngine.getState()) {
                case UNINITIALIZED: {
                    gameEngine.start();
                    break;
                }
            }
        }
        if (SwingUtilities.isEventDispatchThread()) {
            showWindow();
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        showWindow();
                    }
                });
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected void showWindow() {
        if (gameEngine == null) {
            throw new NullPointerException("GameEngine is not defined for Game");
        }
        JFrame f = getWindow();
        String c0 = currentSceneEngineId;
        SceneEngine se = gameEngine.getActiveScene();
        String c = se == null ? null : se.getId();
        if (!Objects.equals(c0, c)) {
            updateSceneShowing();
        }
        f.setVisible(true);
    }

    public Image getIcon() {
        return image;
    }

    public void setIcon(String image) {
        setIcon(image, null);
    }

    public void setIcon(Image image) {
        this.image = image;
    }

    public void setIcon(String image, Class baseClass) {
        if (image == null) {
            setIcon((Image) null);
        } else {
            setIcon(AtomUtils.createImage(image, baseClass));
        }
    }

    @Override
    public void buildAssets() {
        container.start();
    }

    public AtomIoCContainer getContainer() {
        return container;
    }

    //    public void setModel(GameModel model) {
//        this.model = model;
//    }
    private class GameStateTracker extends GameEngineStateAdapter {

        @Override
        public void gameStarted(GameEngine game) {
            start();
        }
    }

    private class GameChangeTracker extends GameEngineChangeAdapter {

        @Override
        public void activeSceneChanged(SceneEngine oldEngine, SceneEngine newEngine) {
            if (gameEngine != null) {
                switch (gameEngine.getState()) {
                    case STARTED: {
                        //newEngine == null ? null : newEngine.getId()
                        updateSceneShowing();
                        break;
                    }
                }
            }
        }
    }

    private class WindowUpdaterPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateWindow();
        }
    }
}
