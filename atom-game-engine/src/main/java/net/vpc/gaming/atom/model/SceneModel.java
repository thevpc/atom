/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;


/**
 * @author vpc
 */
public interface SceneModel {

    public ViewDimension getSceneSize();

    public void setSceneSize(ViewDimension sceneSize);

    public ViewDimension getTileSize();

    public void setTileSize(ViewDimension tileDimension);

    public RatioViewBox getCamera();

    public void setCamera(RatioViewBox camera);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    public Map<String, Object> getProperties();

    public <T> T getProperty(String propertyName);

    public void removeProperty(String propertyName);

    public void setProperty(String propertyName, Object value);

    public boolean isIsometric();

    public void setIsometric(boolean isometricView);


    public void addControlPlayer(int id);

    public void resetControlPlayers();

    public boolean removeControlPlayer(int id);

    /**
     * getControlPlayers()[0] or null if no player
     *
     * @return getControlPlayers()[0] or null if no player
     */
    public Integer getControlPlayer();

    public List<Integer> getControlPlayers();

    public boolean isControlPlayer(int id);

}
