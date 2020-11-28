package net.thevpc.gaming.atom.model;

import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Game View
 * User: vpc
 * Date: 8/15/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GameProperties {
    public Map<String, Object> toMap();

    public <T> T getProperty(String propertyName);

    public void removeProperty(String propertyName);

    public void setProperty(String propertyName, Object value);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

}
