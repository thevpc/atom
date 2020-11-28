/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.heatmap;

import net.thevpc.gaming.atom.extension.DefaultSceneEngineExtension;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.engine.SceneEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class HeatMapSceneExtension extends DefaultSceneEngineExtension {

    private int rows;
    private int columns;
    private Map<Integer, double[][]> infoMap = new HashMap<>();

    @Override
    public void install(SceneEngine engine) {
        super.install(engine);
        rows = (int) engine.getSize().getHeight();
        columns = (int) engine.getSize().getWidth();
    }

    public double[][] getHeatMap(Player player) {
        double[][] heatMap = infoMap.get(player.getId());
        if (heatMap == null) {
            heatMap = new double[rows][columns];
            infoMap.put(player.getId(), heatMap);
        }
        return heatMap;
    }
}
