/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.armors;

import net.thevpc.gaming.atom.model.SpriteArmor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class InvincibleSpriteArmor implements SpriteArmor {

    private boolean active;

    public InvincibleSpriteArmor() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
