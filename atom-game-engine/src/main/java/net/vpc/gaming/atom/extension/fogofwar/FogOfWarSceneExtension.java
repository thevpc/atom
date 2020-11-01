/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension.fogofwar;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SceneEngineChangeAdapter;
import net.vpc.gaming.atom.engine.SceneEngineChangeListener;
import net.vpc.gaming.atom.extension.DefaultSceneExtension;
import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.SceneEngineModel;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.presentation.*;
import net.vpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FogOfWarSceneExtension extends DefaultSceneExtension {

    SceneEngineChangeListener sceneEngineChangeListener = new SceneEngineChangeAdapter() {
        @Override
        public void modelChanged(SceneEngine sceneEngine, SceneEngineModel oldValue, SceneEngineModel newValue) {
            reconfigure();
        }
    };
    SceneChangeListener sceneChangeListener = new SceneChangeAdapter() {
        @Override
        public void imageProducerManagerChanged(Scene scene, ImageProducer oldValue, ImageProducer newValue) {
            reconfigure();
        }

    };
    PropertyChangeListener updater = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            //mapInfo = null;
        }
    };
    SceneLifeCycleListener sceneEngineChangeListenerInstaller = new SceneLifeCycleListener() {
        @Override
        public void sceneInitialized(Scene scene) {
            scene.getSceneEngine().addSceneEngineChangeListener(sceneEngineChangeListener);
        }
    };

    private Scene scene;
    private FogOfWarLayer baseLayer;

    public void install(Scene scene) {
        this.scene = scene;
        scene.getModel().addPropertyChangeListener(updater);
        scene.addLifeCycleListener(sceneEngineChangeListenerInstaller);
        scene.addSceneChangeListener(sceneChangeListener);
        if (scene.getSceneEngine() != null) {
            scene.getSceneEngine().addSceneEngineChangeListener(sceneEngineChangeListener);
        }
        reconfigure();
    }

    @Override
    public void uninstall(Scene scene) {
        this.scene = null;
        scene.getModel().removePropertyChangeListener(updater);
        scene.removeSceneChangeListener(sceneChangeListener);
        if (scene.getSceneEngine() != null) {
            scene.getSceneEngine().removeSceneEngineChangeListener(sceneEngineChangeListener);
        }
        if (baseLayer != null) {
            scene.removeLayer(baseLayer);
        }
    }

    protected void reconfigure() {
        if (scene.getSceneEngine() != null) {
            if (scene.getSceneEngine().getExtension(FogOfWarSceneEngineExtension.class) != null) {
                if (!scene.containsLayer(FogOfWarLayer.class)) {
                    scene.addLayer(getBaseLayer());
                }
                return;
            }
        }
        if (baseLayer != null) {
            scene.removeLayer(baseLayer);
        }
    }

    public FogOfWarLayer getBaseLayer() {
        if (baseLayer == null) {
            baseLayer = new FogOfWarLayer();
        }
        return baseLayer;
    }

    private static class FogOfWarLayer extends FlatBoardLayer {

        private Composite[] compositeArray;

        public FogOfWarLayer() {
            this(SKY_LAYER);
        }

        public FogOfWarLayer(int zIndex) {
            setLayer(zIndex);
        }


        public void draw(LayerDrawingContext context) {
            Graphics2D graphics = context.getGraphics();
            Scene view = context.getScene();

            Scene sv = view;
            if (compositeArray == null || compositeArray.length != FogOfWarSceneEngineExtension.FOG_MAX) {
                compositeArray = new Composite[FogOfWarSceneEngineExtension.FOG_MAX - 1];
                for (int i = 0; i < compositeArray.length; i++) {
                    compositeArray[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (i + 1F) / compositeArray.length);
                }
            }
//        HashMap<Integer, Area> fogs = new HashMap<>();
            final Collection<Tile> visibleCells = view.findDisplayTiles();
            Composite composite = graphics.getComposite();
            graphics.setColor(Color.DARK_GRAY);
            FogOfWarSceneEngineExtension e = view.getSceneEngine().getExtension(FogOfWarSceneEngineExtension.class);
            for (Player player : view.getControlPlayers()) {
                FogOfWarSceneEngineExtension.FogOfWarLayerInfo playerInfo = e.getPlayerInfo(player);
                for (Tile tile : visibleCells) {
                    final ViewBox r = sv.toViewBox(tile.getBounds());
                    int col = tile.getColumn();
                    int row = tile.getRow();
                    int v = playerInfo.getTileFog(col, row);
                    if (v > 0) {
//                    Area a = new Area(r);
//                    final Area a0 = fogs.get(v - 1);
//                    if (a0 == null) {
//                        fogs.put(v - 1, a);
//                    } else {
//                        a0.add(a);
//                    }
                        graphics.setComposite(compositeArray[v - 1]);
                        graphics.fillRect(r.getX(), r.getY(), r.getWidth() + 2, r.getHeight() + 2);
                    }
                }
            }
//        for (Entry<Integer, Area> entry : fogs.entrySet()) {
//            graphics.setComposite(compositeArray[entry.getKey()]);
//            graphics.fill(entry.getValue());
//        }
            graphics.setComposite(composite);
        }
    }

}
