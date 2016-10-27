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
public class FillBoardColorLayer extends FlatBoardLayer {

    private Color color;

    public FillBoardColorLayer(int layer, Color color) {
        this.color = color;
        setLayer(layer);
    }

    public FillBoardColorLayer(Color color) {
        this(UNDERGROUND_LAYER, color);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();

        graphics.setColor(color);
        ViewBox viewPort = getScene().getAbsoluteCamera();
        int iwidth = viewPort.getWidth();
        int iheight = viewPort.getHeight();
        graphics.fillRect(0, 0, iwidth, iheight);
    }
}
