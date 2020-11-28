/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.SpriteSelection;

import java.beans.PropertyChangeListener;

/**
 * @author vpc
 */
public interface ControlPlayer {

    public int getId();

    public <T extends Player> T getPlayer();

    public SpriteSelection getSelection();

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void addPropertyChangeListener(PropertyChangeListener listener);
}
