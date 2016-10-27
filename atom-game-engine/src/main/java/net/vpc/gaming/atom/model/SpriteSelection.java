/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpriteSelection {

    private PropertyChangeSupport pcs;
    private HashMap<Integer, Sprite> selectedSprites = new HashMap<Integer, Sprite>();

    public SpriteSelection(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    public void clear() {
        selectedSprites.clear();
        pcs.firePropertyChange("selectionReset", null, selectedSprites);
    }

    public void add(Sprite sprite) {
        selectedSprites.put(sprite.getId(), sprite);
        pcs.firePropertyChange("selectionAdded", null, sprite.getId());
    }

    public void remove(Sprite sprite) {
        selectedSprites.remove(sprite.getId());
        pcs.firePropertyChange("selectionRemoved", null, sprite);
    }

    public List<Integer> getSelectedSpriteIds() {
        List<Integer> all = new ArrayList<>(selectedSprites.size());
        for (Sprite s : selectedSprites.values()) {
            all.add(s.getId());
        }
        return all;
    }

    public List<Sprite> getSelectedSprites() {
        return new ArrayList<Sprite>(selectedSprites.values());
    }

    public boolean contains(Sprite sprite) {
        return selectedSprites.containsKey(sprite.getId());
    }

    public void toggleSelection(Sprite sprite, boolean appendSelection) {
        if (!appendSelection) {
            clear();
            if (sprite.isSelectable()) {
                add(sprite);
            }
        } else {
            if (sprite.isSelectable()) {
                if (contains(sprite)) {
                    remove(sprite);
                } else {
                    add(sprite);
                }
            }
        }

    }
}
