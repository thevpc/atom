/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;
import net.thevpc.gaming.atom.presentation.Scene;

import java.awt.Graphics2D;
import java.awt.Image;

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
