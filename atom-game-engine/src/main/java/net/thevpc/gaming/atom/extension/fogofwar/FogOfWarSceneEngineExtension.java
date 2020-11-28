/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.fogofwar;

import net.thevpc.gaming.atom.extension.DefaultSceneEngineExtension;
import net.thevpc.gaming.atom.model.ModelBox;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngineTask;
import net.thevpc.gaming.atom.model.Sprite;

import java.util.HashMap;
import java.util.Map;

/**
 * FogOfWarSceneExtension implements fog of war facility To enable FogOfWar display
 * consider adding a FogOfWarLayer to the Scene Usage Example
 * <pre>
 *  public class MySceneEngine extends DefaultSceneEngine {
 *
 *     public MySceneEngine() {
 *         setModel(new MySceneModel());
 *         installExtension(new FogOfWarFeature(5));
 *     }
 * }
 * public class MyScene extends DefaultScene {
 *
 *     public MyScene() {
 *         super(30, 30);
 *         bindSprite(new PersonView(), Person.class);
 *         addLayer(new FogOfWarLayer());
 *     }
 * }
 * </pre>
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FogOfWarSceneEngineExtension extends DefaultSceneEngineExtension implements SceneEngineTask {

    public static final int FOG_MAX = 10;
    private long fogSpeed;
    private int fogMax = FOG_MAX;
    private int rows;
    private int columns;
    private Map<Integer, FogOfWarLayerInfo> infoMap = new HashMap<>();

    /**
     * create FogOfWarFeature with feogSpedd=100
     */
    public FogOfWarSceneEngineExtension() {
        this(100);
    }

    /**
     * Create a FogOfWarFeature instance
     *
     * @param fogSpeed frame needed to dissipate Frog
     */
    public FogOfWarSceneEngineExtension(long fogSpeed) {
        this.fogSpeed = fogSpeed;
    }

    @Override
    public void install(SceneEngine engine) {
        rows = (int) engine.getSize().getHeight();
        columns = (int) engine.getSize().getWidth();
        engine.addSceneTask(this);
    }

    @Override
    public void uninstall(SceneEngine engine) {
        engine.removeSceneTask(this);
    }

    /**
     * Called by SceneEngine
     *
     * @param sceneEngine current sceneEngine
     */
    @Override
    public boolean nextFrame(SceneEngine sceneEngine) {
        long frame = sceneEngine.getFrame();
        for (Player p : sceneEngine.getPlayers()) {
            FogOfWarLayerInfo info = getPlayerInfo(p);
            for (Sprite s : sceneEngine.findSpritesByPlayer(p.getId())) {
                int scope = s.getSight();
                ModelBox r1 = s.getBounds();
                ModelBox r2 = new ModelBox(r1.getMinX() - scope, r1.getMinY() - scope, r1.getWidth() + 2 * scope, r1.getHeight() + 2 * scope);
                for (Tile tile : sceneEngine.findTiles(r2)) {
                    if (r1.distance(tile.getBounds()) <= scope) {
//                    if (r1.getCenter().distance(r2.getCenter()) < scope) {
                        info.setTileVisitedFrame(tile.getColumn(), tile.getRow(), frame);
                    }
                }
            }
            for (Tile tile : sceneEngine.getTiles()) {
                int col = tile.getColumn();
                int row = tile.getRow();
                long visitFrame = info.getTileVisitedFrame(col, row);
                if (visitFrame > 0) {
                    long v = (int) ((frame - visitFrame) / fogSpeed);
                    if (v >= fogMax) {
                        v = fogMax - 1;
                    }
                    info.setTileFog(col, row, (int) v);
                } else {
                    info.setTileFog(col, row, fogMax - 1);
                }
            }
        }
        return true;
    }

    public FogOfWarLayerInfo getPlayerInfo(Player player) {
        FogOfWarLayerInfo i = infoMap.get(player.getId());
        if (i != null) {
            //should check rows and columns?
            return i;
        }
        i = new FogOfWarLayerInfo(rows, columns);
        infoMap.put(player.getId(), i);
        return i;
    }

    public class FogOfWarLayerInfo {

        private long[][] tilesVisitedTime;
        private int[][] fogMap;
        private int rows;
        private int columns;

        public FogOfWarLayerInfo(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
        }

        protected int[][] getFogMap() {
            if (fogMap == null) {
                fogMap = new int[rows][columns];
            }
            return fogMap;
        }

        protected long[][] getTilesVisitedTime() {
            if (tilesVisitedTime == null) {
                tilesVisitedTime = new long[rows][columns];
            }
            return tilesVisitedTime;
        }

        public long getTileVisitedFrame(int col, int row) {
            return getTilesVisitedTime()[row][col];
        }

        public void setTileVisitedFrame(int col, int row, long visibility) {
            getTilesVisitedTime()[row][col] = visibility;
        }

        public int getTileFog(int col, int row) {
            return getFogMap()[row][col];
        }

        public void setTileFog(int col, int row, int fog) {
            getFogMap()[row][col] = fog;
        }
    }
}
