package net.thevpc.gaming.atom.examples.soussecraft.main.model.units;

import net.thevpc.gaming.atom.extension.strategy.Unit;
import net.thevpc.gaming.atom.extension.strategy.resources.DefaultResourceRepository;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceCarrier;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceRepository;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.model.Orientation;
import net.thevpc.gaming.atom.model.armors.LevelSpriteArmor;
import net.thevpc.gaming.atom.model.weapons.gun.GunWeapon;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.tasks.BuildNewAtomiumMainTask;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Minerals;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Woods;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com) @creationtime 10 dec. 2006
 * 23:19:21
 */
public class Worker extends Unit implements ResourceCarrier {

    private ResourceRepository resources;

    public Worker() {
        setName("Worker");
        setSize(new ModelDimension(1, 1));
        setSpeed(0);
        setDirection(Orientation.SOUTH);
        setMaxLife(20);
        setLife(20);
        setArmors(new LevelSpriteArmor(0));
        setWeapons(new GunWeapon(
                1, //bullet speed
                1, //LEVEL
                1, 4, //MIN - MAX DAMAGE
                20, //MIN DISTANCE TO FIRE
                GunWeapon.BULLETS_COUNT_ILLIMITED, //NUMBER OF BULLETS CARRIED
                1,//GunWeapon.SIMULTANEOUS_FIRES_ILLIMITED, //NUMBER OF BULLETS FIRED SIMULTANEOUSLY
                GunWeapon.RECHARGE_ASAP, // TIME TO RECHARGE WHEN NO MORE BULLETS REMAIN
                10 // TIME BETWEEN TWO FIRES
                ));
        setSpeed(0.125);
        setSight(6);
        //setWeapons(new KnifeWeapon(0,1, 40));
        resources = new DefaultResourceRepository();
        //pour stocker les ressources
        resources.setMaxResource(8, Minerals.class, Woods.class);
    }

    public void buildNewAtomium() {
        setMainTask(new BuildNewAtomiumMainTask(15));
    }

    public ResourceRepository getResources() {
        return resources;
    }

    public void setResources(ResourceRepository resources) {
        this.resources = resources;
    }
}
