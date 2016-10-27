/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.armors;

import net.vpc.gaming.atom.model.SpriteArmor;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.model.armors.InvincibleSpriteArmor;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.dashboard.SpriteArmorView;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class InvincibleArmorView implements SpriteArmorView {
    public void drawDetails(SpriteArmor armor, Graphics2D graphics, Scene view, ViewPoint point, Dimension dimension) {
        int x = point.getX();
        int y = point.getY();
        int w = dimension.width;
        int h = dimension.height;
        graphics.drawRect(x, y, w, h);
        InvincibleSpriteArmor sarmor = (InvincibleSpriteArmor) armor;
        graphics.setColor(Color.GREEN);
        graphics.drawPolygon(
                new int[]{x + 3, x + w - 3, x + w - 3, x + w / 2, x + 3},
                new int[]{y + 2, y + 2, y + h - 10, y + h - 2, y + h - 10}, 5
        );
        graphics.setColor(Color.YELLOW);
        graphics.drawString("Invincible", x + w / 2 - 8, y + h / 2);
    }
}
