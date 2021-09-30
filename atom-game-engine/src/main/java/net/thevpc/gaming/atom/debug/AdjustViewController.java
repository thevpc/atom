/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.debug;

import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.layers.Layer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class AdjustViewController extends DefaultSceneController {
    java.util.List<ViewDimension> resolutions = Arrays.asList(
            new ViewDimension(200, 150),
//            new ViewDimension(320, 180),
//            new ViewDimension(320, 240),
//            new ViewDimension(360, 202),
//            new ViewDimension(400, 300),
//            new ViewDimension(432, 243),
//            new ViewDimension(480, 270),
//            new ViewDimension(512, 288),
            new ViewDimension(512, 384),
            new ViewDimension(400, 360),
            new ViewDimension(640, 400),
            new ViewDimension(640, 512),
//            new ViewDimension(684, 384),
//            new ViewDimension(700, 450),
//            new ViewDimension(700, 525),
            new ViewDimension(720, 405),
//            new ViewDimension(800, 450),
            new ViewDimension(800, 600),
            new ViewDimension(840, 525),
//            new ViewDimension(864, 486),
//            new ViewDimension(896, 672),
            new ViewDimension(900, 600),
//            new ViewDimension(928, 696),
            new ViewDimension(960, 540),
//            new ViewDimension(1024, 576),
            new ViewDimension(1024, 600),
            new ViewDimension(1024, 768),
            new ViewDimension(1280, 720),
//            new ViewDimension(1280, 768),
            new ViewDimension(1152, 864),
            new ViewDimension(1280, 800),
            new ViewDimension(1280, 960),
            new ViewDimension(1280, 1024),
            new ViewDimension(1360, 768),
//            new ViewDimension(1368, 768),
            new ViewDimension(1400, 900),
            new ViewDimension(1400, 1050),
            new ViewDimension(1440, 810),
            new ViewDimension(1600, 900),
            new ViewDimension(1680, 1050),
            new ViewDimension(1920, 1080)
    );
    private final ViewDimensionComparator comp = new ViewDimensionComparator();
    private final SceneKeyEventFilter nextResolutionFilter = new DefaultSceneKeyEventFilter(KeyCode.CONTROL, KeyCode.R, KeyCode.ADD);
    private final SceneKeyEventFilter previousResolutionFilter = new DefaultSceneKeyEventFilter(KeyCode.CONTROL, KeyCode.R, KeyCode.SUBTRACT);
    private final SceneKeyEventFilter nextTileSizeFilter = new DefaultSceneKeyEventFilter(KeyCode.CONTROL, KeyCode.T, KeyCode.ADD);
    private final SceneKeyEventFilter previousTileSizeFilter = new DefaultSceneKeyEventFilter(KeyCode.CONTROL, KeyCode.T, KeyCode.SUBTRACT);
    private final SceneKeyEventFilter debugFilter = new DefaultSceneKeyEventFilter(KeyCode.CONTROL, KeyCode.D);
    private final SceneKeyEventFilter isometricFilter = new DefaultSceneKeyEventFilter(KeyCode.CONTROL, KeyCode.I);

    public AdjustViewController() {
        Collections.sort(resolutions, comp);
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        if (nextResolutionFilter.accept(e)) {
            changeResolution(e.getScene(), true);
            e.setConsumed(true);
        }
        if (previousResolutionFilter.accept(e)) {
            changeResolution(e.getScene(), false);
            e.setConsumed(true);
        }
        if (nextTileSizeFilter.accept(e)) {
            changeTileSize(e.getScene(), false);
            e.setConsumed(true);
        }
        if (previousTileSizeFilter.accept(e)) {
            changeTileSize(e.getScene(), true);
            e.setConsumed(true);
        }
        if (debugFilter.accept(e)) {
            changeDebugMode(e.getScene());
            e.setConsumed(true);
        }
        if (isometricFilter.accept(e)) {
            changeIsometric(e.getScene());
            e.setConsumed(true);
        }
    }

    public void changeTileSize(Scene scene, boolean smaller) {
        ViewDimension tileSize = scene.getModel().getTileSize();
        if (smaller) {
            int size = tileSize.getWidth();
            if (size > 5) {
                scene.getModel().setTileSize(new ViewDimension(size - 5, size - 5, size - 5));
            }
        } else {
            int size = tileSize.getWidth();
            if (size < 100) {
                scene.getModel().setTileSize(new ViewDimension(size + 5, size + 5, size + 5));
            }
        }
    }
    private ViewDimension nextResolutionOf(ViewDimension previous){
        for (ViewDimension resolution : resolutions) {
            int s1 = comp.compare(resolution, previous);
            if (s1 > 0) {
                return resolution;
            }
        }
        return null;
    }
    private ViewDimension previousResolutionOf(ViewDimension previous){
        for (int i = resolutions.size() - 1; i >= 0; i--) {
            ViewDimension resolution = resolutions.get(i);
            int s1 = comp.compare(resolution, previous);
            if (s1 < 0) {
                return resolution;
            }
        }
        return null;
    }

    public void changeResolution(Scene scene, boolean nextOrPrevious) {
        //do nothing on change resolution
        ViewDimension previous0 = scene.getCamera().getViewDimension();
        ViewDimension previous = previous0;
        while(true) {
            ViewDimension d = nextOrPrevious ? nextResolutionOf(previous):previousResolutionOf(previous);
            if (nextOrPrevious) {
                for (ViewDimension resolution : resolutions) {
                    int s1 = comp.compare(resolution, previous);
                    if (s1 > 0) {
                        d = resolution;
                        break;
                    }
                }
            } else {
                for (int i = resolutions.size() - 1; i >= 0; i--) {
                    ViewDimension resolution = resolutions.get(i);
                    int s1 = comp.compare(resolution, previous);
                    if (s1 < 0) {
                        d = resolution;
                        break;
                    }
                }
            }
            if (d != null) {
                ModelDimension dimRatio = ModelDimension.of(scene.getTileSize()).div(ModelDimension.of(previous));
                scene.setTileSize(ViewDimension.of(ModelDimension.of(d).multiply(dimRatio)));
                scene.getCamera().setDimension(d);
                ViewDimension q = scene.getCamera().getViewDimension();
                if(q.equals(previous)){
                    previous=d;
                }else{
                    break;
                }
            }else{
                break;
            }
        }
    }

    public void changeDebugMode(Scene scene) {
        for (Layer layer : scene.getLayers()) {
            if (layer.getClass().getSimpleName().startsWith("Debug")) {
                layer.setEnabled(!layer.isEnabled());
            }
        }
    }

    public void changeIsometric(Scene scene) {
        scene.setIsometric(!scene.isIsometric());
    }

    private static class ViewDimensionComparator implements Comparator<ViewDimension> {
        @Override
        public int compare(ViewDimension o1, ViewDimension o2) {
            long s1 = o1.getWidth() * o1.getHeight();
            long s2 = o2.getWidth() * o2.getHeight();
            return Long.compare(s1, s2);
        }
    }
}
