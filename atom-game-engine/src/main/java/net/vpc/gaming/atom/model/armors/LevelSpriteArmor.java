/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model.armors;

import net.vpc.gaming.atom.model.SpriteArmor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class LevelSpriteArmor implements SpriteArmor {

    private int level;
    private boolean active;

    public LevelSpriteArmor(int level) {
        this.level = level;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getLevel() {
        return level;
    }


}
