/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.strategy;

import net.thevpc.gaming.atom.model.SpriteAction;
import net.thevpc.gaming.atom.model.DefaultSprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Entity extends DefaultSprite {
    private SpriteAction[] actions = new SpriteAction[0];


    public SpriteAction[] getActions() {
        return actions;
    }

    public void setActions(SpriteAction[] actions) {
        this.actions = actions;
    }

}
