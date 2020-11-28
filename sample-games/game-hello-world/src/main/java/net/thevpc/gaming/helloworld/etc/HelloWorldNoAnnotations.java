package net.thevpc.gaming.helloworld.etc;

import net.thevpc.gaming.atom.Atom;
import net.thevpc.gaming.atom.debug.AdjustViewController;
import net.thevpc.gaming.atom.debug.DebugLayer;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteController;
import net.thevpc.gaming.atom.presentation.components.SLabel;

/**
 * Hello world!
 */
public class HelloWorldNoAnnotations {

    public static void main(String[] args) {
        //create game
        Game game = Atom.createGame();

        //configure scene sceneEngine
        SceneEngine sceneEngine = game.addSceneEngine("HelloWorld");
        sceneEngine.setSize(20, 10);

        //configure ball
        Sprite ball = sceneEngine
                .createSprite("ball")
                .setLocation(2, 2)
                .setSpeed(0.2)
                .setDirection(Math.PI / 4)
                .setMainTask(new MoveSpriteMainTask())
                .setCollisionTask(new StopSpriteCollisionTask());
        sceneEngine.addSprite(ball);

        //configure scene
        Scene scene = game.addScene(sceneEngine);
        scene.addLayer(new DebugLayer(false));
        scene.setTitle("HelloWorld");
        scene.setTileSize(30);
        scene.setSpriteView("ball", new ImageSpriteView("/ball.png", 8, 4));
        scene.addController(new SpriteController(ball));
        scene.addController(new AdjustViewController());
        scene.addComponent(new SLabel("Click CTRL-D to switch debug mode, use Arrows to move ball"));

        //start game
        game.start();
    }
}
