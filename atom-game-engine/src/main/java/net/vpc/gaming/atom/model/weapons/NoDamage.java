/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model.weapons;

import net.vpc.gaming.atom.model.DamageEffects;
import net.vpc.gaming.atom.model.ModelPoint;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class NoDamage implements DamageEffects {
    public static final DamageEffects ISTANCE = new NoDamage();

    /**
     * @param source   : le sprite qui a declanché l'arme
     * @param distance : distance maximale de couverture de l'arme
     * @param damage   : le nombre de point à enlever à l'adversaire lorsque l'armure est nulle
     */
    private NoDamage() {
    }

    public int getDamage(ModelPoint point, int player) {
        return 0;
    }
}
