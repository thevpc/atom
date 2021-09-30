/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.extension.SceneExtension;
import net.thevpc.gaming.atom.model.Box;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.presentation.components.SceneComponent;
import net.thevpc.gaming.atom.presentation.layers.Layer;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface Scene {
    Object getCompanionObject();

    void setCompanionObject(Object value);

    String getId();

    String getTitle();

    void setTitle(String title);

    JComponent toComponent();

    boolean isIsometric();

    void setIsometric(boolean isometricView);

    <T extends SceneModel> T getModel();

    SceneController[] getSceneControllers();

    void addLifeCycleListener(SceneLifeCycleListener listener);

    void removeLifeCycleListener(SceneLifeCycleListener listener);

    SceneLifeCycleListener[] getLifecycleListeners();

    Game getGame();

    <T extends GameEngine> T getGameEngine();

    <T extends SceneEngine> T getSceneEngine();

    void init(SceneEngine sceneEngine, Game game);

    void start();

    void stop();

    void setTileSize(int width, int height);

    void setTileSize(int width, int height, int altitude);

    ViewDimension getTileSize();

    void setTileSize(int size);

    void setTileSize(ViewDimension tileSize);

    void addChangeListener(SceneChangeListener listener);

    void removeChangeListener(SceneChangeListener listener);

    SceneChangeListener[] getChangeListeners();

    boolean containsSceneChangeListener(SceneChangeListener listener);

    void addController(SceneController listener);

    void removeController(SceneController listener);

    <T extends Layer> T getLayer(Class<T> type);

    void installExtension(SceneExtension sceneExtension);

    void uninstallExtension(String sceneExtensionId);

    void uninstallExtension(Class sceneExtensionType);

    boolean containsExtension(String sceneViewExtensionId);

    boolean containsExtension(Class sceneViewExtensionType);

    SceneExtension[] getSceneExtensions();

    Layer[] getLayers();

    boolean containsLayer(Class clazz);

    boolean containsLayer(Layer layer);

    void addLayer(Layer layer);

    void removeLayer(Layer layer);

    void setViewBinding(Class model, Object view);

    Object getViewBinding(Class model);

    void setSpriteView(SpriteFilter sprites, SpriteView view);

    SpriteView getSpriteView(Sprite sprite);


    ViewPoint toViewPoint(Point point);

    ModelBox toModelBox(Box rectangle);

    ModelPoint toModelPoint(Point point);

    ViewPoint toIsometricViewPoint(ViewPoint point);

    //    public void addPropertyChangeListener(PropertyChangeListener listener);
//
//    public void addPropertyChangeListener(String property, PropertyChangeListener listener);
//
//    public void removePropertyChangeListener(PropertyChangeListener listener);
//
//    public void removePropertyChangeListener(String property, PropertyChangeListener listener);
    List<Sprite> findDisplaySprites();

    List<Sprite> findSprites(ViewPoint point);

    Tile findTile(ModelPoint point);

    Tile findTile(ViewPoint point);

    int getEventPlayer(ModelPoint modelPoint, ViewPoint viewPoint, int clickCount, int button, int modifiers);

    int getEventPlayer(int keyCode, int keyChar, int keyLocation);

    boolean isWithinScreen(ViewBox viewBox);

    boolean isWithinScreen(Tile tile);

    boolean isWithinScreen(Sprite sprint);

    java.util.List<Tile> findDisplayTiles();

    List<Tile> findTiles(ModelBox modelBox);

    List<Tile> findTiles(ViewBox viewBox);

    Path2D toPath2D(ModelBox modelBox);


    /**
     * toView(sprite.getBound()) return visible rectangle
     */
    ViewBox toViewBox(Sprite sprite);

    ViewBox toViewBox(Tile tile);

    ViewBox toViewBox(Box modelBox);


    ViewDimension toViewDimension(Dimension dim);


    //    public ViewBox getBounds();

    ViewBox getSceneScreen();

    ViewDimension getSceneSize();

    String getNextFocusComponent(String componentName);

    String getPreviousFocusComponent(String componentName);

    List<String> getFocusedComponents();

    List<String> getFocusableComponents();

    <T extends SceneComponent> T getComponent(String name);

    void removeComponent(String name);

    void addComponent(SceneComponent gameComponent);

    void addComponent(SceneComponent gameComponent, Object constraints);

    void addComponent(SceneComponent gameComponent, Object constraints, int layer);

    void addComponent(SceneComponent gameComponent, Object constraints, int layer, boolean mapAligned);

    AffineTransform getScreenAffineTransform();

    void setScreenAffineTransform(AffineTransform affineTransform);

    AffineTransform getBoardAffineTransform();

    void setBoardAffineTransform(AffineTransform boardAffineTransform);

    ViewBox getLayoutBox(ViewBox spriteTilesBox, boolean tilesAligned, SceneLayoutType boundsType, AffineTransform screenTransform);

    ViewBox getLayoutBox(Sprite sprite);

    long getFrame();

    ImageProducer getImageProducer();

    void setImageProducer(ImageProducer environmentImageProducer);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    Player getPlayer(int id);

    List<Player> getPlayers();

    Sprite getSprite(int id);

    void addControlPlayer(int id);

    void addControlPlayer(Player player);

    void resetControlPlayers();

    boolean removeControlPlayer(int id);

    /**
     * getControlPlayers()[0] or null if no player
     *
     * @return getControlPlayers()[0] or null if no player
     */
    Player getControlPlayer();

    void setControlPlayer(int id);

    List<Player> getControlPlayers();

    boolean isControlPlayer(int id);

    SceneCamera getCamera();
}