package net.thevpc.gaming.atom.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/15/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultGameEngineProperties implements GameEngineProperties {
    protected PropertyChangeSupport propertyChangeSupport;
    protected Map<String, Object> properties;

    public DefaultGameEngineProperties() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Map<String, Object> toMap() {
        //return new LinkedHashMap<String,Object>(properties);
        return properties != null ? new LinkedHashMap<String, Object>(properties) : new HashMap<String, Object>(1);
    }

    public <T> T getProperty(String propertyName) {
        if (properties != null) {
            return (T) properties.get(propertyName);
        }
        return null;
    }

    public void removeProperty(String propertyName) {
        setProperty(propertyName, null);
    }

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

}
