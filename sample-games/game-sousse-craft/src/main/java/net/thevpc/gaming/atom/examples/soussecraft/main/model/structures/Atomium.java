package net.thevpc.gaming.atom.examples.soussecraft.main.model.structures;

import net.thevpc.gaming.atom.extension.strategy.Structure;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.model.armors.LevelSpriteArmor;

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
