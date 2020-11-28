/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.strategy.players;

import net.thevpc.gaming.atom.model.DefaultPlayer;
import net.thevpc.gaming.atom.extension.strategy.resources.DefaultResourceRepository;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceCarrier;
import net.thevpc.gaming.atom.extension.strategy.resources.ResourceRepository;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class StrategyGamePlayer extends DefaultPlayer implements ResourceCarrier {

    private ResourceRepository resources;

    public StrategyGamePlayer(String name) {
        this(name, new DefaultResourceRepository());
    }

    public StrategyGamePlayer(String name, ResourceRepository deposit) {
        super(name);
        this.resources = deposit;
    }

    public ResourceRepository getResources() {
        return resources;
    }

}
