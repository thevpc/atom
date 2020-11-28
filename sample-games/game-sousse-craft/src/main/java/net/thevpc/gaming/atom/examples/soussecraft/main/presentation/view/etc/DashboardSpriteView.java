/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc;

import java.awt.Graphics2D;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface DashboardSpriteView extends SpriteView {
    public void drawDetails(Sprite sprite, Graphics2D graphics, Scene view, ViewPoint point, ViewDimension dimension) ;
}
