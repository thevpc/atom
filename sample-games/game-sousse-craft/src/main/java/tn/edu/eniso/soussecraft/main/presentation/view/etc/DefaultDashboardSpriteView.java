/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.view.etc;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.Scene;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultDashboardSpriteView extends ImageSpriteView implements DashboardSpriteView{
    private DashboardSupport dashboardSupport; 
    public DefaultDashboardSpriteView(Image[] images) {
        super(images);
        dashboardSupport=new DashboardSupport(this);
    }

    public DefaultDashboardSpriteView(String[] images) {
        super(images);
        dashboardSupport=new DashboardSupport(this);
    }

    public DefaultDashboardSpriteView(Class baseClass, int rows, int cols, String imageMap) {
        super(baseClass, imageMap, rows, cols);
        dashboardSupport=new DashboardSupport(this);
    }

    public DefaultDashboardSpriteView(int rows, int cols, String imageMap) {
        super(imageMap, rows, cols);
        dashboardSupport=new DashboardSupport(this);
    }
    
    
    public void drawDetails(Sprite sprite, Graphics2D graphics, Scene view, ViewPoint point, ViewDimension dimension) {
        dashboardSupport.drawDetails(sprite, graphics, view, point, dimension);
    }
}
