package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.GameEngineChangeAdapter;
import net.vpc.gaming.atom.engine.GameEngineStateAdapter;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.ioc.AtomIoCContainer;
import net.vpc.gaming.atom.ioc.GameIoCContainer;
import net.vpc.gaming.atom.model.DefaultGameModel;
import net.vpc.gaming.atom.model.GameModel;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.util.AtomUtils;
import net.vpc.gaming.atom.util.RepeatingReleasedEventsFixer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

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
    private GameModel gameModel;
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
        container=new GameIoCContainer(this);
    }

    @Override
    public GameModel getModel() {
        return gameModel;
    }

    @Override
    public void setModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

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

    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }

    public void setSceneFactory(SceneFactory sceneViewFactory) {
        this.sceneFactory = sceneViewFactory;
    }

    public void addScene(Scene scene, String sceneID) {
        SceneEngine engine = getGameEngine().getSceneEngine(sceneID);
        scene.init(engine, this);
        loadedScenes.put(sceneID, scene);
    }

    public void addScene(Scene scene, Class<? extends SceneEngine> sceneType) {
        addScene(scene, sceneType.getName());
    }

    public void addScene(Scene scene, SceneEngine sceneEngine) {
        getGameEngine().addSceneEngine(sceneEngine);
        addScene(scene, sceneEngine.getId());
    }

    @Override
    public void addScene(Scene scene) {
        getGameEngine().addSceneEngine(getGameEngine().createSceneEngine(scene.getId()));
        addScene(scene, scene.getId());
    }

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
        getGameEngine().addSceneEngine(sceneEngine);
    }

    @Override
    public Scene addScene(String sceneId) {
        Scene scene = createScene();
        SceneEngine sceneEngine = getGameEngine().createSceneEngine(sceneId);
        getGameEngine().addSceneEngine(sceneEngine);
        addScene(scene, sceneEngine.getId());
        return scene;
    }

    @Override
    public SceneEngine addSceneEngine(String sceneID) {
        return getGameEngine().addSceneEngine(sceneID);
    }

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
        return getGameEngine().getSceneEngine(sceneEngineId);
    }

    @Override
    public List<Scene> findScenesBySceneEnginId(String sceneEnginId) {
        java.util.List<Scene> all=new ArrayList<>();
        for (Map.Entry<String, Scene> e : loadedScenes.entrySet()) {
            String id = e.getValue().getSceneEngine().getId();
            if(id.equals(sceneEnginId)){
                all.add(e.getValue());
            }
        }
        return all;
    }

    @Override
    public void showScene(Class<? extends SceneEngine> sceneType) {
        showScene(sceneType.getName());
    }

    @Override
    public void showScene(String sceneID) {
        SceneEngine sceneEngine = gameEngine.getSceneEngine(sceneID);

        Scene scene = getScene(sceneID);

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
        this.currentSceneEngineId = sceneID;
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
            ViewBox vp = currentScene.getAbsoluteCamera();
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
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            window = new JFrame();
                            window.setLayout(new BorderLayout());
                            window.setSize(600, 400);
                            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            updateWindow();
                        }
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

    public void start() {
        if (gameEngine != null) {
            switch (gameEngine.getEngineState()) {
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
        SceneEngine se = gameEngine.getActiveSceneEngine();
        String c = se == null ? null : se.getId();
        if (!Objects.equals(c0, c)) {
            showScene(c);
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

    ;

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
                switch (gameEngine.getEngineState()) {
                    case STARTED: {
                        showScene(newEngine == null ? null : newEngine.getId());
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
