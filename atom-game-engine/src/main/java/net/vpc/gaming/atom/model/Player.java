/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Taha Ben Salah
 */
public interface Player extends Serializable {

    public static final int NO_PLAYER = -1;

    void setId(int id);

    int getId();

    String getName();

    void setName(String name);

    public Map<String, Object> getProperties();

    Object getProperty(String name);

    void setProperty(String name, Object value);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void setSceneModel(SceneEngineModel sceneModel);

    SceneEngineModel getModel();

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    Color getColor();

    void setColor(Color color);

    public void copyFrom(Player player);

    public SpriteSelection getSelection();

}
