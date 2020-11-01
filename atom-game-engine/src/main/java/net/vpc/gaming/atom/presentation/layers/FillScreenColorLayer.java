/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.model.ViewBox;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FillScreenColorLayer extends DefaultLayer {

    private Color color;

    public FillScreenColorLayer(int layer, Color color) {
        this.color = color;
        setLayer(layer);
    }

    public FillScreenColorLayer(Color color) {
        this(SCREEN_BACKGROUND_LAYER, color);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();

        graphics.setColor(color);
        ViewBox viewPort = getScene().getCamera().getViewBounds();
        int iwidth = viewPort.getWidth();
        int iheight = viewPort.getHeight();
        graphics.fillRect(0, 0, iwidth, iheight);
    }
}
