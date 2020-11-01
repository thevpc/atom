/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.view.layers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import net.vpc.gaming.atom.presentation.layers.DefaultLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DashboardLayer extends DefaultLayer {

    public static Dimension DASHBOARD_SIZE = new Dimension(600, 200);

    public DashboardLayer() {
        setLayer(SCREEN_DASHBOARD_LAYER);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics=context.getGraphics();

        //custom background
        
        Rectangle vp = getScene().getCamera().getViewPort().toRectangle();
        int height = DASHBOARD_SIZE.height;
        int width = DASHBOARD_SIZE.width;

        Color gradientStart = Color.BLUE.darker();//new Color(204, 249, 124);
        Color gradientEnd = Color.WHITE;//new Color(174, 222, 94);
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        GradientPaint painter = new GradientPaint(0, vp.height - height, gradientEnd, 0, vp.height, gradientStart);
        graphics.setPaint(painter);
        graphics.fillRoundRect(0, vp.height - height, width, height, 50, 50);
        graphics.setComposite(composite);
    }
}
