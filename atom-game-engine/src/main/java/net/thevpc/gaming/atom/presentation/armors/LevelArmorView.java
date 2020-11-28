/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.armors;

import net.thevpc.gaming.atom.model.SpriteArmor;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.model.armors.LevelSpriteArmor;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.dashboard.SpriteArmorView;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class LevelArmorView implements SpriteArmorView {
    public void drawDetails(SpriteArmor armor, Graphics2D graphics, Scene view, ViewPoint point, Dimension dimension) {
        int x = point.getX();
        int y = point.getY();
        int w = dimension.width;
        int h = dimension.height;
        graphics.drawRect(x, y, w, h);
        LevelSpriteArmor sarmor = (LevelSpriteArmor) armor;
        graphics.setColor(Color.GREEN);
        graphics.drawPolygon(
                new int[]{x + 3, x + w - 3, x + w - 3, x + w / 2, x + 3},
                new int[]{y + 2, y + 2, y + h - 10, y + h - 2, y + h - 10}, 5
        );
        graphics.setColor(Color.YELLOW);
        graphics.drawString("L" + String.valueOf(sarmor.getLevel()), x + w / 2 - 8, y + h / 2);
    }
}
