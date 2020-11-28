/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.weapons.gun;

import net.thevpc.gaming.atom.model.DamageEffects;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.SpriteWeapon;
import net.thevpc.gaming.atom.model.weapons.NoDamage;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class GunWeapon implements SpriteWeapon {

    public static final int BULLETS_COUNT_ILLIMITED = -1;
    public static final int SIMULTANEOUS_FIRES_ILLIMITED = -1;
    public static final long RECHARGE_ASAP = 0;
    public static final long RECHARGE_NEVER = -1;
    public static final long FIRE_ASAP = -1;
    private int level;
    private double bulletSpeed;
    private int minDamage;
    private int maxDamage;
    private int distance;
    private boolean active = true;
    private boolean enabled = true;
    private int maxBulletsCount;
    private int bulletsCount = BULLETS_COUNT_ILLIMITED;
    private long rechargeTime = RECHARGE_ASAP;
    private long remainingRechargeTime;
    private int simultaneousFires;
    private long fireDelayTime = FIRE_ASAP;
    private long _sleepTime;
    private HashSet<Bullet> currentBullets = new HashSet<Bullet>();

    /**
     * @param level             gun level
     * @param damage            damage
     * @param distance
     * @param bulletsCount      initial bullets count
     * @param simultaneousFires number of bullets living (moving to target) that can be handled at once
     * @param fireDelayTime     time between to fires
     */
    public GunWeapon(
            double bulletSpeed,
            int level,
            int minDamage,
            int maxDamage,
            int distance,
            int bulletsCount,
            int simultaneousFires,
            long rechargeTime,
            long fireDelayTime) {
        this.bulletSpeed = bulletSpeed;
        this.level = level;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.distance = distance;
        this.bulletsCount = bulletsCount;
        this.maxBulletsCount = bulletsCount;
        this.rechargeTime = rechargeTime;
        this.simultaneousFires = simultaneousFires;
        this.fireDelayTime = fireDelayTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DamageEffects fire(final Sprite source, ModelPoint targetPoint) {
        if (source.distance(targetPoint) > distance) {
            //out of range
            return null;
        }
        if (bulletsCount <= 0) {
            if (rechargeTime == RECHARGE_ASAP) {
                //do thing
            } else if (rechargeTime == RECHARGE_NEVER) {
                if (bulletsCount <= 0) {
                    return NoDamage.ISTANCE;
                }
            } else {
                if (remainingRechargeTime > 0) {
                    remainingRechargeTime--;
                    if (remainingRechargeTime <= 0) {
                        bulletsCount = maxBulletsCount;
                    } else {
                        //charging
                        return NoDamage.ISTANCE;
                    }
                }
            }
        }
        if (_sleepTime > 0) {
            _sleepTime--;
            return NoDamage.ISTANCE;
        }

        if (simultaneousFires != SIMULTANEOUS_FIRES_ILLIMITED) {
            for (Iterator<Bullet> ii = currentBullets.iterator(); ii.hasNext(); ) {
                Bullet b = ii.next();
                if (b.isDead()) {
                    ii.remove();
                }
            }
            if (currentBullets.size() >= simultaneousFires) {
                return NoDamage.ISTANCE;
            }
        }

        int damage = (int) Math.round(minDamage + Math.random() * (maxDamage - minDamage));
        Bullet bullet = new Bullet(damage, source.getPlayerId(), "bullet", source.getLocation(), targetPoint, bulletSpeed);
        currentBullets.add(bullet);
        source.getSceneEngine().addSprite(bullet);
        if (bulletsCount != BULLETS_COUNT_ILLIMITED) {
            bulletsCount--;
            if (bulletsCount <= 0) {
                remainingRechargeTime = rechargeTime;
            } else {
                _sleepTime = fireDelayTime;
            }
        } else {
            _sleepTime = fireDelayTime;
        }

        return NoDamage.ISTANCE;
    }

    public int getDamage() {
        return minDamage;
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
