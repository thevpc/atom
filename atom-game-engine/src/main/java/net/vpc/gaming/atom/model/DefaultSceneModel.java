/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * @author vpc
 */
public class DefaultSceneModel implements SceneModel {

    protected PropertyChangeSupport propertyChangeSupport;
    protected Map<String, Object> properties;
    private ViewDimension tileSize;
    private ViewDimension sceneSize;
    private RatioViewBox camera=new RatioViewBox(0,0,0,1,1,1);
    private boolean isometric;
    private LinkedHashSet<Integer> controlPlayers = new LinkedHashSet<Integer>();

    public DefaultSceneModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public ViewDimension getSceneSize() {
        return sceneSize;
    }

    @Override
    public void setSceneSize(ViewDimension sceneSize) {
        ViewDimension old = this.sceneSize;
        this.sceneSize = sceneSize;
        getPropertyChangeSupport().firePropertyChange("sceneSize", old, sceneSize);
    }

    @Override
    public ViewDimension getTileSize() {
        return tileSize;
    }

    @Override
    public void setTileSize(ViewDimension tileSize) {
        int w0 = tileSize.getWidth();
        if(w0<=0){
            w0=50;
        }
        int h0 = tileSize.getHeight() <= 0 ? w0 : tileSize.getHeight();
        tileSize=new ViewDimension(
                w0,
                h0,
                tileSize.getAltitude()<=0?1:tileSize.getAltitude()
        );

        ViewDimension old = this.tileSize;
        this.tileSize = tileSize;
        getPropertyChangeSupport().firePropertyChange("tileSize", old, tileSize);
    }

    @Override
    public RatioViewBox getCamera() {
        return camera;
    }

    @Override
    public void setCamera(RatioViewBox camera) {
        RatioViewBox old = this.camera;
        this.camera = camera;
        getPropertyChangeSupport().firePropertyChange("camera", old, camera);
    }

    @Override
    public boolean isIsometric() {
        return isometric;
    }

    @Override
    public void setIsometric(boolean isometric) {
        boolean old = this.isometric;
        this.isometric = isometric;
        getPropertyChangeSupport().firePropertyChange("isometric", old, isometric);
    }

    @Override
    public Map<String, Object> getProperties() {
        //return new LinkedHashMap<String,Object>(properties);
        return properties != null ? new LinkedHashMap<String, Object>(properties) : new HashMap<String, Object>(1);
    }

    @Override
    public <T> T getProperty(String propertyName) {
        if (properties != null) {
            return (T) properties.get(propertyName);
        }
        return null;
    }

    @Override
    public void removeProperty(String propertyName) {
        setProperty(propertyName, null);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        Object old;
        if (value != null) {
            if (properties == null) {
                properties = new LinkedHashMap<>();
            }
            old = properties.put(propertyName, value);
            getPropertyChangeSupport().firePropertyChange(propertyName, old, value);
        } else {
            if (properties != null) {
                old = properties.remove(propertyName);
                getPropertyChangeSupport().firePropertyChange(propertyName, old, value);
            }
        }
    }

    protected PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
    }

    public void addControlPlayer(int player) {
        controlPlayers.add(player);
    }

    public List<Integer> getControlPlayers() {
        return new ArrayList<>(controlPlayers);
    }

    @Override
    public Integer getControlPlayer() {
        for (Integer p : controlPlayers) {
            return p;
        }
        return null;
    }

    @Override
    public boolean isControlPlayer(int id) {
        return controlPlayers.contains(id);
    }

    @Override
    public void resetControlPlayers() {
        controlPlayers.clear();
    }

    @Override
    public boolean removeControlPlayer(int id) {
        return controlPlayers.remove(id);
    }

}
