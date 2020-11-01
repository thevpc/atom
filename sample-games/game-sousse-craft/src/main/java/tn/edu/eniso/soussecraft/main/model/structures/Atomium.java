package tn.edu.eniso.soussecraft.main.model.structures;

import net.vpc.gaming.atom.extension.strategy.Structure;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.armors.LevelSpriteArmor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class Atomium extends Structure {
    public Atomium() {
        setName("Atomium");
        setSize(new ModelDimension(8,8));
        setMaxLife(1000);
        setLife(1000);
        setArmors(new LevelSpriteArmor(0));
    }

}
