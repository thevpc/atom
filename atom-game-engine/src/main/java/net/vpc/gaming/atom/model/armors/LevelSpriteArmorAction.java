package net.vpc.gaming.atom.model.armors;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.SpriteArmor;
import net.vpc.gaming.atom.model.SpriteArmorAction;

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
