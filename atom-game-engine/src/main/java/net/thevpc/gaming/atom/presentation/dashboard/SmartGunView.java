/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.dashboard;

import net.thevpc.gaming.atom.model.SpriteWeapon;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.model.weapons.intelligun.SmartGunWeapon;
import net.thevpc.gaming.atom.presentation.Scene;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SmartGunView implements WeaponView {
    public void drawDetails(SpriteWeapon weapon, Graphics2D graphics, Scene view, ViewPoint point, Dimension dimension) {
        SmartGunWeapon kweapon = (SmartGunWeapon) weapon;
        graphics.setColor(Color.GREEN);
        int x = point.getX();
        int y = point.getY();
        int w0 = dimension.width;
        int w = w0 / 2;
        int h = dimension.height;
        graphics.drawRect(x, y, w, h);
        graphics.drawPolygon(
                new int[]{x + w / 2 - 3, x + w / 2, x + w / 2 + 3, x + w / 2 + 3, x + w / 2 - 3},
                new int[]{y + 5, y + 2, y + 5, y + h - 2, y + h - 2}, 5
        );
        graphics.setColor(Color.YELLOW);
        graphics.drawString("L" + (kweapon.getLevel()), x + w + 2, y + 10);
        graphics.drawString("D" + (kweapon.getDamage()), x + w + 2, y + 20);
        graphics.drawString("R" + (kweapon.getDistance()), x + w + 2, y + 30);
    }
}
