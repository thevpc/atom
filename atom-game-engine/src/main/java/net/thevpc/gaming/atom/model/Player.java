/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Taha Ben Salah
 */
public interface Player extends Serializable {

    public static final int NO_PLAYER = -1;

    int getId();

    Player setId(int id);

    String getName();

    Player setName(String name);

    public Map<String, Object> getProperties();

    Object getProperty(String name);

    Player setProperty(String name, Object value);

    Player addPropertyChangeListener(PropertyChangeListener listener);

    Player addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    Player setSceneModel(SceneEngineModel sceneModel);

    SceneEngineModel getModel();

    Player removePropertyChangeListener(PropertyChangeListener listener);

    Player removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    Color getColor();

    Player setColor(Color color);

    Player copy();

    Player copyFrom(Player player);

    SpriteSelection getSelection();

}
