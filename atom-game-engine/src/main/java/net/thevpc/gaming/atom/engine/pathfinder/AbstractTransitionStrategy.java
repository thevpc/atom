/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.pathfinder;

import net.thevpc.gaming.atom.model.ModelBox;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.extension.heatmap.HeatMapBuilder;
import net.thevpc.gaming.atom.extension.heatmap.HeatMapSceneExtension;
import net.thevpc.gaming.atom.model.Sprite;

import java.util.List;

/**
 * Base Move Strategy. Cost is based on Heat Map and max sight.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class AbstractTransitionStrategy implements TransitionStrategy {

    private SceneEngine sceneEngine;
    private Sprite sprite;
    private ModelBox window;
    private double maxSight;
    private double[][] heatMap;

    public AbstractTransitionStrategy(SceneEngine sceneEngine, Sprite sprite, double maxMovesSight) {
        this.sprite = sprite;
        this.sceneEngine = sceneEngine;
        int cols = (int) sceneEngine.getSize().getWidth();
        int rows = (int) sceneEngine.getSize().getHeight();
        this.window = new ModelBox(0, 0, cols, rows);
        this.maxSight = maxMovesSight * sprite.getSpeed();
        int sid = sprite.getPlayerId();
        HeatMapBuilder h = new HeatMapBuilder();
        HeatMapSceneExtension e = sceneEngine.getExtension(HeatMapSceneExtension.class);
        h.buildHeatMap(sprite, heatMap = e.getHeatMap(sceneEngine.getPlayer(sid)));
    }

    protected void addIfValid(ModelPoint from, ModelPoint to, List<Transition> moves) {
        double tox = to.getX();
        double toy = to.getY();
        if (tox >= window.getMinX() && tox + sprite.getWidth() <= window.getMaxX()
                && toy >= window.getMinY() && toy + sprite.getWidth() <= window.getMaxY()
                && (sprite.getLocation().distance(to) <= maxSight)) {
            double cost = getCost(from, to);
            if (!Double.isInfinite(cost)) {
                moves.add(new DefaultTransition(from, to, cost));
            }
        }
    }

    protected double getCost(ModelPoint from, ModelPoint to) {
        double w = sprite.getWidth();
        double h = sprite.getHeight();
        Tile tileFrom = sceneEngine.findTile(from);
        Tile tileTo = sceneEngine.findTile(to);
        int c1 = tileFrom.getColumn();
        int c2 = tileTo.getColumn();
        int r1 = tileFrom.getRow();
        int r2 = tileTo.getRow();
        int[] z1 = tileFrom.getZ();
        int[] z2 = tileTo.getZ();
        int w1 = tileFrom.getWalls();
        int w2 = tileTo.getWalls();
        if ((c1 + 1) == c2) {
            //  [ ][ ][ ]
            //  [ ][1][2]
            //  [ ][ ][ ]
            if (r1 == r2) {
                if ((w1 & Tile.WALL_EAST) != 0 || (w2 & Tile.WALL_WEST) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[1] != z2[0]) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[2] != z2[3]) {
                    return Double.POSITIVE_INFINITY;
                }
                //  [ ][ ][ ]
                //  [ ][1][ ]
                //  [ ][ ][1]
            } else if ((r1 + 1) == r2) {
                if ((w1 & Tile.WALL_EAST) != 0 || (w1 & Tile.WALL_SOUTH) != 0 || (w2 & Tile.WALL_WEST) != 0 || (w2 & Tile.WALL_NORTH) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[2] != z2[0]) {
                    return Double.POSITIVE_INFINITY;
                }
                //  [ ][ ][2]
                //  [ ][1][ ]
                //  [ ][ ][ ]
            } else if (r1 == (r2 + 1)) {
                if ((w1 & Tile.WALL_EAST) != 0 || (w1 & Tile.WALL_NORTH) != 0 || (w2 & Tile.WALL_WEST) != 0 || (w2 & Tile.WALL_SOUTH) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[1] != z2[3]) {
                    return Double.POSITIVE_INFINITY;
                }
            }
        } else if (c1 == (c2 + 1)) {
            //x transition
            //  [ ][ ][ ]
            //  [2][1][ ]
            //  [ ][ ][ ]
            if (r1 == r2) {
                if ((w1 & Tile.WALL_WEST) != 0 || (w1 & Tile.WALL_EAST) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[0] != z2[1]) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[3] != z2[2]) {
                    return Double.POSITIVE_INFINITY;
                }
                //  [ ][ ][ ]
                //  [ ][1][ ]
                //  [2][ ][ ]
            } else if ((r1 + 1) == r2) {
                if ((w1 & Tile.WALL_WEST) != 0 || (w1 & Tile.WALL_SOUTH) != 0 || (w2 & Tile.WALL_EAST) != 0 || (w2 & Tile.WALL_NORTH) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[3] != z2[1]) {
                    return Double.POSITIVE_INFINITY;
                }
                //  [2][ ][ ]
                //  [ ][1][ ]
                //  [ ][ ][ ]
            } else if (r1 == (r2 + 1)) {
                if ((w1 & Tile.WALL_WEST) != 0 || (w1 & Tile.WALL_NORTH) != 0 || (w2 & Tile.WALL_EAST) != 0 || (w2 & Tile.WALL_SOUTH) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[0] != z2[2]) {
                    return Double.POSITIVE_INFINITY;
                }
            }
        } else if (c1 == c2) {
            //x transition
            //  [ ][   ][ ]
            //  [ ][1/2][ ]
            //  [ ][   ][ ]
            if (r1 == r2) {
//                return 0;
            } else if ((r1 + 1) == r2) {
                //  [ ][ ][ ]
                //  [ ][1][ ]
                //  [ ][2][ ]
                if ((w1 & Tile.WALL_SOUTH) != 0 || (w2 & Tile.WALL_NORTH) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[0] != z2[3]) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[1] != z2[2]) {
                    return Double.POSITIVE_INFINITY;
                }
                //  [ ][2][ ]
                //  [ ][1][ ]
                //  [ ][ ][ ]
            } else if (r1 == (r2 + 1)) {
                if ((w1 & Tile.WALL_NORTH) != 0 || (w2 & Tile.WALL_SOUTH) != 0) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[2] != z2[1]) {
                    return Double.POSITIVE_INFINITY;
                }
                if (z1[3] != z2[0]) {
                    return Double.POSITIVE_INFINITY;
                }
            }
        }
        double value = 0;
        for (Tile tile : sceneEngine.findTiles(new ModelBox(to.getX(), to.getY(), w, h))) {
            value += heatMap[tile.getRow()][tile.getColumn()];
        }
        return value;
    }

    public double getMaxSight() {
        return maxSight;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public ModelBox getWindow() {
        return window;
    }
}
