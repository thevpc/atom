/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.weapons;

import net.thevpc.gaming.atom.model.DamageEffects;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SimpleDamage implements DamageEffects {
    private Sprite source;
    private double distance;
    private int damage;

    /**
     * @param source   : le sprite qui a declanché l'arme
     * @param distance : distance maximale de couverture de l'arme
     * @param damage   : le nombre de point à enlever à l'adversaire lorsque l'armure est nulle
     */
    public SimpleDamage(Sprite source, double distance, int damage) {
        this.source = source;
        this.distance = distance;
        this.damage = damage;
    }

    public int getDamage(ModelPoint point, int player) {
        return point.distance(source.getLocation()) < distance ? damage : 0;
    }
}
