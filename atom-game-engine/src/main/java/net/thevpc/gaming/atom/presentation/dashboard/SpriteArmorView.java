/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.gaming.atom.presentation.dashboard;

import net.thevpc.gaming.atom.model.SpriteArmor;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Scene;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SpriteArmorView {
    public abstract void drawDetails(SpriteArmor armor, Graphics2D graphics, Scene view, ViewPoint point, Dimension dimension);
}
