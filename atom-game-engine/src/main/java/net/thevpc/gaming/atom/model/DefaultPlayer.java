/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

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

    public Player setProperty(String name, Object value) {
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
        return this;
    }

    public SceneEngineModel getModel() {
        return sceneModel;
    }

    public Player setSceneModel(SceneEngineModel gameModel) {
        this.sceneModel = gameModel;
        return this;
    }

    public int getId() {
        return id;
    }

    public Player setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public Player removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(propertyName, listener);
        return this;
    }

    public Player removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
        return this;
    }

    public Player addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
        return this;
    }

    public Player addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Player setColor(Color color) {
        this.color = color;
        return this;
    }



    @Override
    public Player copy() {
        return new DefaultPlayer().copyFrom(this);
    }

    @Override
    public Player copyFrom(Player player) {
        setId(player.getId());
        setName(player.getName());
        setColor(player.getColor());
        for (Map.Entry<String, Object> e : player.getProperties().entrySet()) {
            setProperty(e.getKey(), e.getValue());
        }
        return this;
    }

    public SpriteSelection getSelection() {
        if (selection == null) {
            selection = new SpriteSelection(getPropertyChangeSupport());
        }
        return selection;
    }
}
