/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.components;

import net.vpc.gaming.atom.debug.AtomDebug;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.DefaultSceneComponent;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.vpc.gaming.atom.util.AtomUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah
 */
public class SImage extends DefaultSceneComponent {

    private Image image;
    private int alignement = SwingConstants.CENTER;

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

    public int getVerticalAlignement() {
        return alignement;
    }

    public void setAlignement(int verticalAlignement) {
        this.alignement = verticalAlignement;
    }

    @Override
    public void draw(LayerDrawingContext context) {
        if (!isVisible()) {
            return;
        }
        switch (getVerticalAlignement()) {
            case SwingConstants.CENTER: {
                ViewPoint p = AtomUtils.getCenterdPosition(image.getWidth(null), image.getHeight(null), getBounds());
                AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                context.getGraphics().drawImage(image, p.getX(), p.getY(), null);
                break;
            }
            case SwingConstants.NORTH_WEST: {
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
