/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineModel extends Serializable {

    public long getFrame();

    public void setFrame(long frame);

    SceneEngineModelListener[] getListeners();

    void addSceneEngineModelListener(SceneEngineModelListener listener);

    public Player createPlayer();

    int addPlayer(Player player);

    int addSprite(Sprite sprite);

    //    public void addSprite(Sprite sprite, int id);
    List<Sprite> findSprites(ModelPoint point);

    List<Sprite> findSprites(ModelPoint point, int layer);

    List<Sprite> findSprites(ModelBox rect);

    Tile findTile(ModelPoint point);

    List<Tile> findTiles(Path2D rect);

    List<Tile> findTiles(ModelBox rect);

    ModelDimension getSize();

    void setSize(ModelDimension dimension);

    public int getPlayersCount();

    public List<Player> getPlayers();

    Player getPlayer(int id);

    Sprite getSprite(int id);

    List<Sprite> getSprites();

    void removeSceneEngineModelListener(SceneEngineModelListener listener);

    void removePlayer(int player);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    void removeSprite(int sprite);

    void resetSprites();

    void resetPlayers();

    public Tile[] getTiles();

    public Tile[][] getTileMatrix();

    public Map<String, Object> getProperties();

    public <T> T getProperty(String propertyName);

    public void removeProperty(String propertyName);

    public void setProperty(String propertyName, Object value);
}
