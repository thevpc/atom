package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.ioc.AtomIoCContainer;
import net.vpc.gaming.atom.model.GameModel;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 11:13:50
 */
public interface Game {

    public SceneFactory getSceneFactory();

    public void setSceneFactory(SceneFactory sceneFactory);

    void showScene(String sceneID);

    void showScene(Class<? extends SceneEngine> sceneType);

    public GameModel getModel();

    public void setModel(GameModel gameEngineModel);

    public GameEngine getGameEngine();

    public void setGameEngine(GameEngine gameEngine);

    public void start();

    public void addScene(Scene scene, String sceneID);

    public void addScene(Scene scene, Class<? extends SceneEngine> sceneType);

    public void addScene(Scene scene, SceneEngine sceneEngine);

    public void addScene(Scene scene);

    public Scene createScene();

    public Scene getScene(String sceneId);

    public SceneEngine getSceneEngine(String sceneEngineId);

    public java.util.List<Scene> findScenesBySceneEnginId(String sceneEnginId);

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
