package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.ioc.AtomIoCContainer;
import net.thevpc.gaming.atom.model.GameProperties;

import java.awt.*;

/**
 * 
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 11:13:50
 */
public interface Game {

    public SceneFactory getSceneFactory();

    public void setSceneFactory(SceneFactory sceneFactory);

    public GameProperties getProperties();

    public void setModel(GameProperties gameEngineModel);

    public GameEngine getGameEngine();

    public void setGameEngine(GameEngine gameEngine);

    public void start();

    public void addScene(Scene scene, String sceneID);

    public void addScene(Scene scene, Class<? extends SceneEngine> sceneType);

    public void addScene(Scene scene, SceneEngine sceneEngine);

    public void addScene(Scene scene);

    public Scene createScene();

    public java.util.List<Scene> getScenes();

    public Scene getScene(String sceneId);

    public SceneEngine getSceneEngine(String sceneEngineId);

    public java.util.List<Scene> findScenesBySceneEngineId(String sceneEnginId);

    public Scene addScene(SceneEngine sceneEngine);

    public Scene addScene(String sceneID);

    public void addSceneEngine(SceneEngine sceneEngine);

    public SceneEngine addSceneEngine(String sceneID);

    public Image getIcon();

    public void setIcon(String image);

    public void setIcon(Image image);

    public void setIcon(String image, Class baseClass);

    public void buildAssets();

    public AtomIoCContainer getContainer() ;
}
