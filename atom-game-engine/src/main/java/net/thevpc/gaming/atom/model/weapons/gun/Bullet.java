package net.thevpc.gaming.atom.model.weapons.gun;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.armors.InvincibleSpriteArmor;
import net.thevpc.gaming.atom.model.DefaultSprite;
import net.thevpc.gaming.atom.model.ModelDimension;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class Bullet extends DefaultSprite {

    private int damage;

    public Bullet(int damage, int player, String name, ModelPoint startPosition, ModelPoint target, double speed) {
        this.damage = damage;
        setName(name);
        setPlayerId(player);
        setSize(new ModelDimension(4d / 15, 4d / 15));
        setLocation(startPosition);
        setSpeed(speed);
        setDirection(startPosition.getAngleTo(target));
//        setTask(new MoveSpriteMainTask());
        setArmors(new InvincibleSpriteArmor());
        setCrossable(true);
//        setCollisionManager(new BulletCollisionTask());
    }

    public int getDamage() {
        return damage;
    }


}
