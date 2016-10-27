package net.vpc.games.helloworld;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.annotations.Inject;
import net.vpc.gaming.atom.annotations.OnSceneStarted;
import net.vpc.gaming.atom.debug.AdjustViewController;
import net.vpc.gaming.atom.debug.DebugLayer;
import net.vpc.gaming.atom.presentation.*;
import net.vpc.gaming.atom.presentation.components.SLabel;

/**
 * Created by vpc on 9/23/16.
 */
@AtomScene(
        id = "hello",
        engine = "hello",
        title = "Hello World",
        tileWidth = 30,
        tileHeight = 30
)
public class HelloWorldScene {
    @Inject
    private Scene scene;

    @OnSceneStarted
    private void init() {
        scene.addLayer(new DebugLayer(false));
        scene.addSceneController(new SpriteController("ball"));
        scene.addSceneController(new AdjustViewController());
        scene.addComponent(new SLabel("Click CTRL-D to switch debug mode, use Arrows to move ball"));
    }
}
