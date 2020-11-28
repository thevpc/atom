/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.palace.main.presentation.view;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.debug.DebugLayer;
import net.thevpc.gaming.atom.debug.DebugVelocityLayer;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.examples.palace.main.presentation.controller.MainController;

import java.awt.*;

@AtomScene(id = "main",tileWidth = 60,tileHeight = 60)
/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainScene extends DefaultScene {
    public static final ImageMatrixProducer WALL_IMAGE_PRODUCER = new ImageMatrixProducer(
            "/net/thevpc/gaming/atom/examples/palace/wall.png",
            new ImageGrid(1, 6)
    ) {
        int[] EMPTY = {0, 3, 8, 11};

        @Override
        public Image getImage(int type, int index) {
            if (index == 0) {
                index = 0;//
            } else {
                index = 3;//11+index%4;
            }
            return super.getImage(type, index);
        }
    };
    public MainScene() {
        super(30, 30);
        setImageProducer(WALL_IMAGE_PRODUCER);
        addController(new MainController());
        addLayer(new DebugVelocityLayer());
        addLayer(new DebugLayer());
        getCamera().followSprite(()->getSceneEngine().findSpriteByKind("Prince"));
        addLifeCycleListener(new SceneLifeCycleListener() {
            @Override
            public void sceneStarted(Scene scene) {
                addControlPlayer(getSceneEngine().getPlayers().get(0));
            }
        });
    }
}
