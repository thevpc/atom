/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.strategy.resources;

import net.thevpc.gaming.atom.extension.strategy.Entity;
import net.thevpc.gaming.atom.model.armors.InvincibleSpriteArmor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Resource extends Entity implements ResourceCarrier {

    public ResourceRepository repo;

    public Resource(int reserve) {
        setSelectable(true);
        setPlayerId(-1);
        setArmors(new InvincibleSpriteArmor());
        setName(getClass().getSimpleName());
        repo = new DefaultResourceRepository(getClass());
        repo.addResource(getClass(), reserve);
    }

    public int gather(int count) {
        return repo.removeResource(getClass(), count);
    }

    public boolean isDepleted() {
        return getValue() == 0;
    }

    public double getValue() {
        return repo.getMaxResource(getClass());
    }

    public ResourceRepository getResources() {
        return repo;
    }

}
