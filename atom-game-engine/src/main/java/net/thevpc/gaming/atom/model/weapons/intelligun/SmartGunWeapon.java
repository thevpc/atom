/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.weapons.intelligun;

import net.thevpc.gaming.atom.model.DamageEffects;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.SpriteWeapon;
import net.thevpc.gaming.atom.model.weapons.NoDamage;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SmartGunWeapon implements SpriteWeapon {

    private int level;
    private int damage;
    private int distance;
    private boolean active = true;
    private boolean enabled = true;

    public SmartGunWeapon(int level, int damage, int distance) {
        this.level = level;
        this.damage = damage;
        this.distance = distance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DamageEffects fire(final Sprite source, ModelPoint target) {
        for (Sprite other : source.getSceneEngine().findSprites(target)) {
            if (other.getPlayerId() != source.getPlayerId()) {
                SmartBullet b = new SmartBullet(source.getPlayerId(), "bullet", source.getLocation(), other);
                source.getSceneEngine().addSprite(b);
                break;
            }
        }
        return NoDamage.ISTANCE;
    }

    public int getDamage() {
        return damage;
    }

    public int getDistance() {
        return distance;
    }

    public int getLevel() {
        return level;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
