/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultPlayer implements Player {

    private int id;
    private String name;
    private Color color;
    private Map<String, Object> properties;
    private transient PropertyChangeSupport propertyChangeSupport;
    private transient SceneEngineModel sceneModel;
    private transient SpriteSelection selection;

    public DefaultPlayer() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public DefaultPlayer(String name) {
        this.name = name;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    public Map<String, Object> getProperties() {
        return properties == null ? new HashMap<String, Object>() : new HashMap<String, Object>(properties);
    }

    public Object getProperty(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        return properties == null ? null : properties.get(name);
    }

    public void setProperty(String name, Object value) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (properties == null) {
            if (value != null) {
                properties = new HashMap<String, Object>();
                properties.put(name, value);
            }
        } else {
            if (value != null) {
                properties.put(name, value);
            } else {
                properties.remove(name);
            }
        }
    }

    public SceneEngineModel getModel() {
        return sceneModel;
    }

    public void setSceneModel(SceneEngineModel gameModel) {
        this.sceneModel = gameModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void copyFrom(Player player) {
        setColor(player.getColor());
        setName(player.getName());
        for (Map.Entry<String, Object> e : player.getProperties().entrySet()) {
            setProperty(e.getKey(), e.getValue());
        }
    }

    public SpriteSelection getSelection() {
        if (selection == null) {
            selection = new SpriteSelection(getPropertyChangeSupport());
        }
        return selection;
    }
}
