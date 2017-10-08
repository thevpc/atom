/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.extension.SceneExtension;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.presentation.components.SceneComponent;
import net.vpc.gaming.atom.presentation.layers.Layer;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.util.List;

import net.vpc.gaming.atom.model.RatioViewBox;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface Scene {
    public Object getCompanionObject();

    public void setCompanionObject(Object value);

    public String getId();

    public String getTitle();

    public void setTitle(String title);

    JComponent toComponent();

    public boolean isIsometric();

    public void setIsometric(boolean isometricView);

    public <T extends SceneModel> T getModel();

    public Game getGame();

    public <T extends GameEngine> T getGameEngine();

    public <T extends SceneEngine> T getSceneEngine();

    public void init(SceneEngine sceneEngine, Game game);

    public void start();

    public void stop();

    public void setTileSize(int size);

    public void setTileSize(int width, int height);

    public void setTileSize(int width, int height, int altitude);

    public ViewDimension getTileSize();

    public void setTileSize(ViewDimension tileSize);

    public void addSceneChangeListener(SceneChangeListener listener);

    public void removeSceneChangeListener(SceneChangeListener listener);

    public SceneChangeListener[] getSceneChangeListeners();

    public boolean containsSceneChangeListener(SceneChangeListener listener);

    public void addSceneController(SceneController listener);

    public void removeSceneController(SceneController listener);

    public <T extends Layer> T getLayer(Class<T> type);

    public void installExtension(SceneExtension sceneExtension);

    public void uninstallExtension(String sceneExtensionId);

    public void uninstallExtension(Class sceneExtensionType);

    public boolean containsExtension(String sceneViewExtensionId);

    public boolean containsExtension(Class sceneViewExtensionType);

    public SceneExtension[] getSceneExtensions();

    public Layer[] getLayers();

    public boolean containsLayer(Class clazz);

    public boolean containsLayer(Layer layer);

    public void addLayer(Layer layer);

    public void removeLayer(Layer layer);

    public void setViewBinding(Class model, Object view);

    public Object getViewBinding(Class model);

    public void setSpriteView(Class<? extends Sprite> spriteType, SpriteView view);

    public void setSpriteView(String spriteKind, SpriteView view);

    public void setSpriteView(int spriteId, SpriteView view);

    public SpriteView getSpriteView(Sprite sprite);

    public ViewBox toViewBox(ModelBox modelBox);

    public ViewPoint toViewPoint(ModelPoint point);

    public ModelBox toModelBox(ViewBox rectangle);

    public ModelPoint toModelPoint(ViewPoint point);

    public ViewPoint toIsometricViewPoint(ViewPoint point);

    //    public void addPropertyChangeListener(PropertyChangeListener listener);
//
//    public void addPropertyChangeListener(String property, PropertyChangeListener listener);
//
//    public void removePropertyChangeListener(PropertyChangeListener listener);
//
//    public void removePropertyChangeListener(String property, PropertyChangeListener listener);
    public List<Sprite> findDisplaySprites();

    public List<Sprite> findSprites(ViewPoint point);

    public Tile findTile(ModelPoint point);

    public Tile findTile(ViewPoint point);

    public int getEventPlayer(ModelPoint modelPoint, ViewPoint viewPoint, int clickCount, int button, int modifiers);

    public int getEventPlayer(int keyCode, int keyChar, int keyLocation);

    public boolean isWithinScreen(ViewBox viewBox);

    public boolean isWithinScreen(Tile tile);

    public boolean isWithinScreen(Sprite sprint);

    public java.util.List<Tile> findDisplayTiles();

    public List<Tile> findTiles(ModelBox modelBox);

    public List<Tile> findTiles(ViewBox viewBox);

    public Path2D toPath2D(ModelBox modelBox);

    public void setCameraLocation(ModelPoint modelLocation);

    public void setAbsoluteCameraLocation(ViewPoint viewLocation);

    public void setCameraLocation(RatioPoint ratioPoint);

    public void setCameraLocation(Sprite sprite);

    public boolean moveAbsoluteCameraBy(int viewDx, int viewDy);

    public boolean moveCameraTo(Sprite sprite);

    public Path2D getPolygonAbsoluteCameraModel();

    public Path2D getPolygonAbsoluteCamera();

    public RatioViewBox getCamera();

    public ViewBox getAbsoluteCamera();

    public void setAbsoluteCameraSize(ViewDimension rect);

    public void setAbsoluteCamera(ViewBox rect);

    public void setCamera(RatioViewBox ratioViewBox);

    public ModelBox getAbsoluteCameraModelBox();

    public void setCameraSize(RatioDimension size);

    /**
     * toView(sprite.getBound()) return visible rectangle
     */
    public ViewBox toViewBox(Sprite sprite);

    public ViewBox toViewBox(Tile tile);

    //    public ViewBox getBounds();
    public ViewBox getCameraScreen();

    public ViewBox getSceneScreen();

    public ViewDimension getSceneSize();

    public ViewDimension getAbsoluteCameraSize();

    public String getNextFocusComponent(String componentName);

    public String getPreviousFocusComponent(String componentName);

    public List<String> getFocusedComponents();

    public List<String> getFocusableComponents();

    public <T extends  SceneComponent> T getComponent(String name);

    public void removeComponent(String name);

    public void addComponent(SceneComponent gameComponent);

    public void addComponent(SceneComponent gameComponent, Object constraints);

    public void addComponent(SceneComponent gameComponent, Object constraints, int layer);

    public void addComponent(SceneComponent gameComponent, Object constraints, int layer, boolean mapAligned);

    public AffineTransform getScreenAffineTransform();

    public void setScreenAffineTransform(AffineTransform affineTransform);

    public AffineTransform getBoardAffineTransform();

    public void setBoardAffineTransform(AffineTransform boardAffineTransform);

    public ViewBox getLayoutBox(ViewBox spriteTilesBox, boolean tilesAligned, SceneLayoutType boundsType, AffineTransform screenTransform);

    public ViewBox getLayoutBox(Sprite sprite);

    public long getFrame();

    public ImageProducer getImageProducer();

    public void setImageProducer(ImageProducer environmentImageProducer);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    public void lockCamera(Sprite s);

    public void unlockCamera();

    public Player getPlayer(int id);

    public List<Player> getPlayers();

    public Sprite getSprite(int id);

    public void addControlPlayer(int id);

    public void addControlPlayer(Player player);

    public void resetControlPlayers();

    public boolean removeControlPlayer(int id);

    /**
     * getControlPlayers()[0] or null if no player
     *
     * @return getControlPlayers()[0] or null if no player
     */
    public Player getControlPlayer();

    public List<Player> getControlPlayers();

    public boolean isControlPlayer(int id);

    public void addSceneListener(SceneListener sceneListener);

    public void removeSceneListener(SceneListener sceneListener);
}