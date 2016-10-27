package net.vpc.gaming.atom.model.weapons.intelligun;

import net.vpc.gaming.atom.model.DefaultSprite;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.armors.InvincibleSpriteArmor;
import net.vpc.gaming.atom.model.weapons.knife.KnifeWeapon;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class SmartBullet extends DefaultSprite {
    public SmartBullet(int player, String name, ModelPoint startPosition, Sprite gotoPosition) {
        setName(name);
        setPlayerId(player);
        setSize(new ModelDimension(4d / 15, 4d / 15));
        setLocation(startPosition);
        setSpeed(0);
        setDirection(Orientation.SOUTH);
        setMaxLife(20);
        setLife(20);
        setArmors(new InvincibleSpriteArmor());
        setWeapons(new KnifeWeapon(0, 1, 40));
//        setTask(new IntelliKamikazeTask(gotoPosition));
    }
}
