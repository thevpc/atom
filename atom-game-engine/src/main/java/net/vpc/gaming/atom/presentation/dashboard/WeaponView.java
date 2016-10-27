/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.gaming.atom.presentation.dashboard;

import net.vpc.gaming.atom.model.SpriteWeapon;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.Scene;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface WeaponView {
    public abstract void drawDetails(SpriteWeapon armor, Graphics2D graphics, Scene view, ViewPoint point, Dimension dimension);
}
