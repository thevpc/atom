/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.business;

import net.thevpc.gaming.atom.engine.DefaultSceneEngine;
import net.thevpc.gaming.atom.extension.fogofwar.FogOfWarSceneEngineExtension;
import net.thevpc.gaming.atom.extension.heatmap.HeatMapSceneExtension;
import net.thevpc.gaming.atom.extension.strategy.StrategySceneExtension;
import net.thevpc.gaming.atom.model.DefaultSceneEngineModel;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.GamePhase;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Minerals;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Woods;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class MainEngine extends DefaultSceneEngine {

    public MainEngine() {
        setModel(new DefaultSceneEngineModel("/net/thevpc/gaming/atom/examples/soussecraft/MyBackground.map"));
        installExtension(new FogOfWarSceneEngineExtension(50));
        installExtension(new HeatMapSceneExtension());
        StrategySceneExtension strategyExtension = new StrategySceneExtension();
        installExtension(strategyExtension);
        strategyExtension.declareResource(Woods.class);
        strategyExtension.declareResource(Minerals.class);
    }

    public abstract void selectTile(int playerId, ModelPoint point, int idTile);

    public abstract void selectSprite(int playerId, ModelPoint point, Integer spriteId, boolean appendSelection);

    public abstract void moveSelectionToSprite(int playerId, ModelPoint point, Integer spriteId);

    public abstract void moveSelectionToTile(int playerId, ModelPoint point, Integer tileId);

    public GamePhase getGamePhase(){
        return getGameEngine().getProperties().getProperty(GamePhase.class.getName());
    }

    public void setGamePhase(GamePhase staring) {
        getGameEngine().getProperties().setProperty(GamePhase.class.getName(),staring);
    }

}
