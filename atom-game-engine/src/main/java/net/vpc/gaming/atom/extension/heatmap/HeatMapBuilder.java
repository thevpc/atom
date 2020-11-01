/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension.heatmap;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;

/**
 * Heat Map Builder, builds Heat Map that defines how far a sprite is from enemy lines
 * Used in AbstractMoveStrategy
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class HeatMapBuilder {

    private double ownerCrossableHeat = 0;
    private double noPlayerHeat = 2;
    private double ennemyHeat = 5;
    private double ennemyMinDistance = 20;

    public double getEnemyHeat() {
        return ennemyHeat;
    }

    public void setEnemyHeat(double ennemyHeat) {
        this.ennemyHeat = ennemyHeat;
    }

    public double getNoPlayerHeat() {
        return noPlayerHeat;
    }

    public void setNoPlayerHeat(double noPlayerHeat) {
        this.noPlayerHeat = noPlayerHeat;
    }

    public double getOwnerCrossableHeat() {
        return ownerCrossableHeat;
    }

    public void setOwnerCrossableHeat(double ownerCrossableHeat) {
        this.ownerCrossableHeat = ownerCrossableHeat;
    }

    public double[][] buildHeatMap(Sprite sprite) {
        return buildHeatMap0(sprite, null);
    }

    public void buildHeatMap(Sprite sprite, double[][] heatMap) {
        buildHeatMap0(sprite, heatMap);
    }

    private double[][] buildHeatMap0(Sprite sprite, double[][] heatMap) {
        SceneEngine gameModel = sprite.getSceneEngine();
        Tile[][] matrix = gameModel.getTileMatrix();
        int cols = (int) gameModel.getSize().getWidth();
        int rows = (int) gameModel.getSize().getHeight();
        int playerId = sprite.getPlayerId();
        if (heatMap != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    heatMap[r][c] = 0;
                }
            }
        } else {
            heatMap = new double[rows][cols];
        }
        double maxDistance = ennemyMinDistance <= 0 ? Math.min(gameModel.getSize().getWidth(), gameModel.getSize().getHeight()) / 2.0 : ennemyMinDistance;
        for (Sprite s : gameModel.getSprites()) {
            int sp = s.getPlayerId();
            ModelBox rectangle = s.getBounds();
            if (sp == Player.NO_PLAYER) {
                if (s.isCrossable()) {
                    for (Tile tile : gameModel.findTiles(rectangle)) {
//                        double o = heatMap[tile.getRow()][tile.getColumn()];
                        heatMap[tile.getRow()][tile.getColumn()] += noPlayerHeat;
                    }
                } else {
                    for (Tile tile : gameModel.findTiles(rectangle)) {
//                        double o = heatMap[tile.getRow()][tile.getColumn()];
                        heatMap[tile.getRow()][tile.getColumn()] = Double.POSITIVE_INFINITY;
                    }
                }
            } else if (sp == playerId) {
                if (s.getId() != sprite.getId()) {
                    if (s.isCrossable()) {
                        for (Tile tile : gameModel.findTiles(rectangle)) {
//                            double o = heatMap[tile.getRow()][tile.getColumn()];
                            heatMap[tile.getRow()][tile.getColumn()] += ownerCrossableHeat;
                        }
                    } else {
                        for (Tile tile : gameModel.findTiles(rectangle)) {
//                            double o = heatMap[tile.getRow()][tile.getColumn()];
                            heatMap[tile.getRow()][tile.getColumn()] = Double.POSITIVE_INFINITY;
                        }
                    }
                }
            } else {
                if (s.isCrossable()) {
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            double d = (matrix[r][c].getBounds().distance(rectangle));
                            if (d < maxDistance) {
                                heatMap[r][c] += ennemyHeat * ((maxDistance - d) / maxDistance);
                            }
                        }
                    }
                } else {
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            double d = matrix[r][c].getBounds().distance(rectangle);
                            int i = (int) Math.round(d);
                            if (i == 0) {
                                heatMap[r][c] = Double.POSITIVE_INFINITY;
                            } else if (d < maxDistance) {
                                heatMap[r][c] += ennemyHeat * ((maxDistance - d) / maxDistance);
                            }
                        }
                    }
                }
            }
        }
        return heatMap;
    }
}
