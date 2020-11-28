/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.model.Orientation;
import net.thevpc.gaming.atom.model.ViewBox;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FillScreenGradientLayer extends DefaultLayer {

    private Color color1;
    private Color color2;
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    public FillScreenGradientLayer(Color color1, Color color2, Orientation direction) {
        this(SCREEN_BACKGROUND_LAYER, color1, color2, direction);
    }

    public FillScreenGradientLayer(int layer, Color color1, Color color2, Orientation direction) {
        if (direction.isRelative()) {
            throw new IllegalArgumentException("Relative directions are not supported");
        }
        setLayer(layer);
        switch (direction) {
            case SOUTH: {
                this.color1 = color1;
                this.color2 = color2;
                this.x1 = 0;
                this.x2 = 0;
                this.y1 = 0;
                this.y2 = 1;
                break;
            }
            case NORTH: {
                this.color1 = color2;
                this.color2 = color1;
                this.x1 = 0;
                this.x2 = 0;
                this.y1 = 0;
                this.y2 = 1;
                break;
            }
            case EAST: {
                this.color1 = color1;
                this.color2 = color2;
                this.x1 = 0;
                this.x2 = 1;
                this.y1 = 0;
                this.y2 = 0;
                break;
            }
            case WEST: {
                this.color1 = color2;
                this.color2 = color1;
                this.x1 = 0;
                this.x2 = 1;
                this.y1 = 0;
                this.y2 = 0;
                break;
            }
            case NORTH_EAST: {
                this.color1 = color1;
                this.color2 = color2;
                this.x1 = 0;
                this.y1 = 1;
                this.x2 = 1;
                this.y2 = 0;
                break;
            }
            case NORTH_WEST: {
                this.color1 = color1;
                this.color2 = color2;
                this.x1 = 1;
                this.y1 = 1;
                this.x2 = 0;
                this.y2 = 0;
                break;
            }
            case SOUTH_WEST: {
                this.color1 = color1;
                this.color2 = color2;
                this.x1 = 1;
                this.y1 = 0;
                this.x2 = 0;
                this.y2 = 1;
                break;
            }
            case SOUTH_EAST: {
                this.color1 = color1;
                this.color2 = color2;
                this.x1 = 0;
                this.y1 = 1;
                this.x2 = 1;
                this.y2 = 0;
                break;
            }
        }
    }

    public FillScreenGradientLayer(int layer, Color color1, float x1, float y1, Color color2, float x2, float y2) {
        this.color1 = color1;
        this.color2 = color2;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        setLayer(layer);
    }

    public FillScreenGradientLayer(Color color1, float x1, float y1, Color color2, float x2, float y2) {
        this(SCREEN_BACKGROUND_LAYER, color1, x1, y1, color2, x2, y2);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        ViewBox viewBox = context.getScene().getCamera().getViewPort();
        int w = viewBox.getWidth();
        int h = viewBox.getHeight();
        Paint oldPaint = graphics.getPaint();
        int _x1 = (int) (x1 * w);
        int _y1 = (int) (y1 * h);
        int _x2 = (int) (x2 * w);
        int _y2 = (int) (y2 * h);
        graphics.setPaint(new GradientPaint(_x1, _y1, color1, _x2, _y2, color2));
        ViewBox viewPort = getScene().getCamera().getViewBounds();
        int iwidth = viewPort.getWidth();
        int iheight = viewPort.getHeight();
        graphics.fillRect(0, 0, iwidth, iheight);
        graphics.setPaint(oldPaint);
    }
}
