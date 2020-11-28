/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImageOp;

import net.thevpc.gaming.atom.extension.strategy.resources.Resource;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.DashboardSpriteView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MyResourceView extends ImageSpriteView implements DashboardSpriteView {

    public MyResourceView(int rows, int cols, String imageMap) {
        super(imageMap, rows, cols);
    }

    public MyResourceView(Class baseClass, int rows, int cols, String imageMap) {
        super(baseClass, imageMap, rows, cols);
    }

    public MyResourceView(String[] images) {
        super(images);
    }

    public MyResourceView(Image[] images) {
        super(images);
    }

    @Override
    public void drawDetails(Sprite sprite, Graphics2D graphics, Scene view, ViewPoint point, ViewDimension dimension) {
        int x0 = 5;
        Image image = getImage(sprite, view, 0,0);
        if (dimension.getWidth() > 100) {
            Resource e = (Resource) sprite;
            int rowHeight = 45;
            int r = 0;

            double scale = 0.5;
            BufferedImageOp op = new AffineTransformOp(
                    AffineTransform.getScaleInstance(scale, scale),
                    new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC));


            graphics.drawImage(image.getScaledInstance(30, 30, Image.SCALE_SMOOTH), x0 + point.getX(), point.getY(), null);
            //graphics.drawImage(op.filter((BufferedImage)image, null), x0 + point.x, point.y, null);
            graphics.setColor(Color.WHITE);
            graphics.drawString("Name", x0 + point.getX() + 40, point.getY() + r * rowHeight + 20);
            graphics.setColor(Color.YELLOW);
            graphics.drawString(sprite.getName(), x0 + point.getX() + 80, point.getY() + r * rowHeight + 20);
            if (!e.isDepleted()) {
                r++;
                graphics.setColor(Color.WHITE);
                graphics.drawString("Value", x0 + point.getX() + 40, point.getY() + r * rowHeight);
                graphics.setColor(Color.YELLOW);
                graphics.drawString(e.getValue() + "/" + e.getMaxLife(), x0 + point.getX() + 80, point.getY() + r * rowHeight);
            } else {
                r++;
                graphics.setColor(Color.RED);
                graphics.drawString("Depleted", x0 + point.getX() + 40, point.getY() + r * rowHeight);
            }
        } else {
            graphics.drawImage(image, x0 + point.getX(), point.getY(), null);
        }
    }
}
