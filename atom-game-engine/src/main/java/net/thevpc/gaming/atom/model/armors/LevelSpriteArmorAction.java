package net.thevpc.gaming.atom.model.armors;

import net.thevpc.gaming.atom.model.SpriteArmor;
import net.thevpc.gaming.atom.model.SpriteArmorAction;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/15/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class LevelSpriteArmorAction implements SpriteArmorAction {
    @Override
    public int hit(SpriteArmor armor, int damage, Sprite entity) {
        return Math.max(damage - ((LevelSpriteArmor) armor).getLevel() * 5, 0);
    }
}
