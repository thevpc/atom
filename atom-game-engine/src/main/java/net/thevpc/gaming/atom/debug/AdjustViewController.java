/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.debug;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.layers.Layer;
import net.thevpc.gaming.atom.presentation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class AdjustViewController extends DefaultSceneController {
    private ViewDimensionComparator comp=new ViewDimensionComparator();
    java.util.List<ViewDimension> resolutions = Arrays.asList(
            new ViewDimension(200, 150),
            new ViewDimension(400, 300),
            new ViewDimension(640, 480),
            new ViewDimension(800, 600),
            new ViewDimension(1024, 600),
            new ViewDimension(1024, 768),
            new ViewDimension(1280, 720),
            new ViewDimension(1280, 768),
            new ViewDimension(1152, 864),
            new ViewDimension(1280, 800),
            new ViewDimension(1280, 960),
            new ViewDimension(1280, 1024),
            new ViewDimension(1360, 768),
            new ViewDimension(1366, 768),
            new ViewDimension(1400, 1050)
    );

    private SceneKeyEventFilter nextResolutionFilter=new DefaultSceneKeyEventFilter(new int[]{SceneKeyEvent.VK_CONTROL ,SceneKeyEvent.VK_R,SceneKeyEvent.VK_ADD});
    private SceneKeyEventFilter previousResolutionFilter=new DefaultSceneKeyEventFilter(new int[]{SceneKeyEvent.VK_CONTROL, SceneKeyEvent.VK_R,SceneKeyEvent.VK_SUBTRACT});
    private SceneKeyEventFilter nextTileSizeFilter=new DefaultSceneKeyEventFilter(new int[]{SceneKeyEvent.VK_CONTROL, SceneKeyEvent.VK_T,SceneKeyEvent.VK_ADD});
    private SceneKeyEventFilter previousTileSizeFilter=new DefaultSceneKeyEventFilter(new int[]{SceneKeyEvent.VK_CONTROL, SceneKeyEvent.VK_T,SceneKeyEvent.VK_SUBTRACT});
    private SceneKeyEventFilter debugFilter=new DefaultSceneKeyEventFilter(new int[]{SceneKeyEvent.VK_CONTROL, SceneKeyEvent.VK_D});
    private SceneKeyEventFilter isometricFilter=new DefaultSceneKeyEventFilter(new int[]{SceneKeyEvent.VK_CONTROL, SceneKeyEvent.VK_I});

    public AdjustViewController() {
        Collections.sort(resolutions,comp);
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        if(nextResolutionFilter.accept(e)){
            changeResolution(e.getScene(),true);
            e.setConsumed(true);
        }
        if(previousResolutionFilter.accept(e)){
            changeResolution(e.getScene(),false);
            e.setConsumed(true);
        }
        if(nextTileSizeFilter.accept(e)){
            changeTileSize(e.getScene(), false);
            e.setConsumed(true);
        }
        if(previousTileSizeFilter.accept(e)){
            changeTileSize(e.getScene(), true);
            e.setConsumed(true);
        }
        if(debugFilter.accept(e)){
            changeDebugMode(e.getScene());
            e.setConsumed(true);
        }
        if(isometricFilter.accept(e)){
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

    public void changeResolution(Scene scene, boolean nextOrPrevious) {
        //do nothing on change resolution
//        ViewDimension previous = scene.getCamera().getSize();
//        ViewDimension d=null;
//        if(nextOrPrevious){
//            for (ViewDimension resolution : resolutions) {
//                int s1 = comp.compare(resolution,previous);
//                if(s1>0){
//                    d=resolution;
//                    break;
//                }
//            }
//        }else{
//            for (int i = resolutions.size()-1; i >=0 ; i--) {
//                ViewDimension resolution = resolutions.get(i);
//                int s1 = comp.compare(resolution,previous);
//                if (s1 < 0) {
//                    d = resolution;
//                    break;
//                }
//            }
//        }
//        if(d!=null){
//            scene.setCameraSize(d);
//        }
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
            int s1 = o1.getWidth() * o1.getHeight();
            int s2 = o2.getWidth() * o2.getHeight();
            return Integer.compare(s1,s2);
        }
    }
}
