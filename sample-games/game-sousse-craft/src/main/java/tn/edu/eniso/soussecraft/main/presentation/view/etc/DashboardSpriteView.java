/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.view.etc;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface DashboardSpriteView extends SpriteView {
    public void drawDetails(Sprite sprite, Graphics2D graphics, Scene view, ViewPoint point, ViewDimension dimension) ;
}
