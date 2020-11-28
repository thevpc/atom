package net.thevpc.gaming.atom.extension.strategy;

import net.thevpc.gaming.atom.engine.DefaultPlayerFactory;
import net.thevpc.gaming.atom.engine.PlayerFactory;
import net.thevpc.gaming.atom.extension.DefaultSceneEngineExtension;
import net.thevpc.gaming.atom.extension.strategy.players.StrategyGamePlayer;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.extension.strategy.resources.Resource;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/31/13
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class StrategySceneExtension extends DefaultSceneEngineExtension {
    private Set<Class<? extends Resource>> resourcesTypes = new HashSet<Class<? extends Resource>>();
    StrategyPlayerFactory playerFactory = new StrategyPlayerFactory();

    public void declareResources(Class<? extends Resource>... resources) {
        for (Class<? extends Resource> other : resources) {
            declareResource(other);
        }
    }

    public void declareResource(Class<? extends Resource> resourceType) {
        resourcesTypes.add(resourceType);
    }

    public void check(Class<? extends Resource> t) {
        if (!resourcesTypes.contains(t)) {
            throw new NoSuchElementException("Unknown resource");
        }
    }

    public Class<? extends Resource>[] getResourceTypes() {
        return resourcesTypes.toArray(new Class[resourcesTypes.size()]);
    }

    @Override
    public void install(SceneEngine engine) {
        engine.setPlayerFactory(playerFactory);
    }

    @Override
    public void uninstall(SceneEngine engine) {
        engine.setPlayerFactory(new DefaultPlayerFactory());
    }

    private class StrategyPlayerFactory implements PlayerFactory {
        @Override
        public Player createPlayer() {
            final StrategyGamePlayer sp = new StrategyGamePlayer("");
            sp.getResources().setMaxResource(-1, resourcesTypes);
            return sp;
        }
    }
}
