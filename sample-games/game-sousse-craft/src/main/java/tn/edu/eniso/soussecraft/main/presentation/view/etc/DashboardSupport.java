/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.view.etc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import net.vpc.gaming.atom.extension.strategy.resources.Resource;
import net.vpc.gaming.atom.extension.strategy.resources.ResourceCarrier;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.dashboard.SpriteArmorView;
import net.vpc.gaming.atom.presentation.dashboard.WeaponView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DashboardSupport {

    private ImageSpriteView spriteView;

    public DashboardSupport(ImageSpriteView spriteView) {
        this.spriteView = spriteView;
    }

    
    public void drawDetails(Sprite sprite, Graphics2D graphics, Scene view, ViewPoint point, ViewDimension dimension) {
        int x0 = 5;
        Image image = spriteView.getImage(sprite, view, 0,0);
        if (dimension.getWidth() > 100) {
            int rowHeight = 30;
            int r = 0;

            graphics.drawImage(image.getScaledInstance(30, 30, Image.SCALE_SMOOTH), x0 + point.getX(), point.getY(), null);
            //graphics.drawImage(op.filter((BufferedImage)image, null), x0 + point.getX(), point.getY(), null);
            graphics.setColor(Color.WHITE);
            graphics.drawString("Name", x0 + point.getX() + 40, point.getY() + r * rowHeight + 20);
            graphics.setColor(Color.YELLOW);
            graphics.drawString(sprite.getName(), x0 + point.getX() + 80, point.getY() + r * rowHeight + 20);
            r++;
            graphics.setColor(Color.WHITE);
            graphics.drawString("Life", x0 + point.getX() + 40, point.getY() + r * rowHeight);
            graphics.setColor(Color.YELLOW);
            graphics.drawString(sprite.getLife() + "/" + sprite.getMaxLife(), x0 + point.getX() + 80, point.getY() + r * rowHeight);

            r++;
            graphics.setColor(Color.WHITE);
            graphics.drawString("Task", x0 + point.getX() + 40, point.getY() + r * rowHeight);
            graphics.setColor(Color.YELLOW);
            graphics.drawString(String.valueOf(sprite.getTask()), x0 + point.getX() + 80, point.getY() + r * rowHeight);

            if (sprite instanceof ResourceCarrier) {
                r++;
                graphics.setColor(Color.WHITE);
                graphics.drawString("Resource", x0 + point.getX() + 40, point.getY() + r * rowHeight);
                graphics.setColor(Color.YELLOW);
                StringBuilder sb = new StringBuilder();
                ResourceCarrier rr = (ResourceCarrier) sprite;
                for (Class<? extends Resource> t : rr.getResources().getResources()) {
                    if (sb.length() > 0) {
                        sb.append(";");
                    }
                    sb.append(t.getSimpleName()).append(":").append(rr.getResources().getResource(t));
                    if (rr.getResources().getMaxResource(t) > 0) {
                        sb.append("/").append(rr.getResources().getMaxResource(t));
                    }
                }
                graphics.drawString(sb.toString(), x0 + point.getX() + 105, point.getY() + r * rowHeight);
            }

            r++;
            graphics.setColor(Color.WHITE);
            graphics.drawString("Armors", x0 + point.getX(), point.getY() + r * rowHeight);
            Dimension ddd = new Dimension(30, 30);
            int xx = 0;
            for (SpriteArmor a : sprite.getArmors()) {
                SpriteArmorView v = (SpriteArmorView) view.getViewBinding(a.getClass());
                v.drawDetails(a, graphics, view, new ViewPoint(x0 + point.getX() + 60 + xx * (ddd.width + 5), point.getY() + r * rowHeight - 15), ddd);
                xx++;
            }
            r++;
            graphics.setColor(Color.WHITE);
            graphics.drawString("Weapons", x0 + point.getX(), point.getY() + r * rowHeight);
            xx = 0;
            for (SpriteWeapon a : sprite.getWeapons()) {
                WeaponView v = (WeaponView) view.getViewBinding(a.getClass());
                v.drawDetails(a, graphics, view, new ViewPoint(x0 + point.getX() + 60 + xx * (ddd.width + 5), point.getY() + r * rowHeight - 15), ddd);
                xx++;
            }

        } else {
            graphics.drawImage(image.getScaledInstance(30, 30, Image.SCALE_SMOOTH), x0 + point.getX(), point.getY(), null);
        }
    }
}
