/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.components;

import net.thevpc.gaming.atom.debug.AtomDebug;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.DefaultSceneComponent;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.awt.*;

/**
 * @author Taha Ben Salah
 */
public class SImage extends DefaultSceneComponent {

    private Image image;

    public SImage(String name, String image, Class baseType) {
        this(name, AtomUtils.createImage(image, baseType));
    }

    public SImage(String name, String image) {
        this(name, AtomUtils.createImage(image));
    }

    public SImage(String name, Image image) {
        super(name);
        this.image = image;
    }



    @Override
    public void draw(LayerDrawingContext context) {
        if (!isVisible()) {
            return;
        }
        switch (getAlignment()) {
            case CENTER: {
                ViewPoint p = AtomUtils.getCenteredPosition(image.getWidth(null), image.getHeight(null), getViewBounds());
                AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                context.getGraphics().drawImage(image, p.getX(), p.getY(), null);
                break;
            }
            case NORTH_WEST: {
                AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                context.getGraphics().drawImage(image, getX(), getY(), null);
                break;
            }
            default: {
                AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                context.getGraphics().drawImage(image, getX(), getY(), null);
            }
        }
    }
}
