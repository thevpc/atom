/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.debug;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.extension.heatmap.HeatMapSceneExtension;
import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.vpc.gaming.atom.presentation.layers.InteractiveLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;
import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DebugTilesHeatLayer extends FlatBoardLayer implements InteractiveLayer{

    private Composite[] heatMapSceneExtensionCompositeArray;

    public DebugTilesHeatLayer() {
        this(SKY_LAYER);
    }

    public DebugTilesHeatLayer(int zIndex) {
        setLayer(zIndex);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();
        SceneEngine sceneEngine = scene.getSceneEngine();
        Player player = scene.getControlPlayer();
        HeatMapSceneExtension heatMapSceneExtension = sceneEngine.getExtension(HeatMapSceneExtension.class);
        double[][] hm = heatMapSceneExtension.getHeatMap(player);
        if (hm != null) {
            double max = 0;
            for (double[] ds : hm) {
                for (double d : ds) {
                    if (!Double.isInfinite(d)) {
                        if (d > max) {
                            max = d;
                        }
                    }
                }
            }
            if (heatMapSceneExtensionCompositeArray == null || heatMapSceneExtensionCompositeArray.length != 4) {
                heatMapSceneExtensionCompositeArray = new Composite[4];
                for (int i = 0; i < heatMapSceneExtensionCompositeArray.length; i++) {
                    heatMapSceneExtensionCompositeArray[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (i + 1F) / heatMapSceneExtensionCompositeArray.length);
                }
            }
            final Collection<Tile> visibleCells = scene.findDisplayTiles();
            Composite composite = graphics.getComposite();
            graphics.setColor(Color.RED);
            for (Tile tile : visibleCells) {
                final ViewBox r = scene.toViewBox(tile.getBounds());
                int col = tile.getColumn();
                int row = tile.getRow();
                double v = hm[row][col];
                if (v > 0) {
                    if (Double.isInfinite(v)) {
                        graphics.setComposite(heatMapSceneExtensionCompositeArray[heatMapSceneExtensionCompositeArray.length - 1]);
                        graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                    } else {
                        double part = max / heatMapSceneExtensionCompositeArray.length;
                        int ii = (int) (v / part);
                        if (ii >= heatMapSceneExtensionCompositeArray.length) {
                            ii = heatMapSceneExtensionCompositeArray.length - 1;
                        }
                        graphics.setComposite(heatMapSceneExtensionCompositeArray[ii]);
                        graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                    }
                }
            }
            graphics.setComposite(composite);

        }

    }
}
