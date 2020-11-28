package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnNextFrame;
import net.thevpc.gaming.atom.annotations.OnSceneStarted;
import net.thevpc.gaming.atom.debug.AdjustViewController;
import net.thevpc.gaming.atom.debug.DebugLayer;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.RatioPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteController;
import net.thevpc.gaming.atom.presentation.components.SLabel;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.layers.FillBoardColorLayer;
import net.thevpc.gaming.atom.presentation.layers.FillScreenColorLayer;

import java.awt.*;

/**
 * Created by vpc on 9/23/16.
 */
@AtomScene(
        id = "hello",
        title = "Hello World",
        tileWidth = 30,
        tileHeight = 30
)
public class HelloWorldScene {

    @Inject
    private Scene scene;
    @Inject
    private SceneEngine sceneEngine;

    @OnSceneStarted
    private void init() {
        scene.addLayer(new DebugLayer(false));
        scene.addLayer(new FillBoardColorLayer(Color.GREEN));
        scene.addLayer(new FillScreenColorLayer(Color.red));
        scene.addLayer(new FillScreenColorLayer(Color.red));
        scene.addController(new SpriteController("ball"));
        scene.addController(new AdjustViewController());
        scene.addComponent(new SLabel("Click CTRL-D to switch debug mode, use Arrows to move ball")
                .setLocation(new RatioPoint(0.5f,0.5f))
        );
    }

}
