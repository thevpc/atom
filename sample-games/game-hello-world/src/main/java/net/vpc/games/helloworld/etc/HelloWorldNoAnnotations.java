package net.vpc.games.helloworld.etc;

import net.vpc.gaming.atom.Atom;
import net.vpc.gaming.atom.debug.AdjustViewController;
import net.vpc.gaming.atom.debug.DebugLayer;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.collision.SimpleSpriteCollisionManager;
import net.vpc.gaming.atom.engine.tasks.MoveSpriteTask;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteController;
import net.vpc.gaming.atom.presentation.components.SLabel;

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
        Sprite ball = sceneEngine.createSprite("ball");
        ball.setLocation(2, 2);
        ball.setSpeed(0.2);
        ball.setDirection(Math.PI/4);
        sceneEngine.addSprite(ball);
        sceneEngine.setSpriteTask("ball", new MoveSpriteTask());
        sceneEngine.setSpriteCollisionManager("ball", new SimpleSpriteCollisionManager());
        
        //configure scene
        Scene scene = game.addScene(sceneEngine);
        scene.addLayer(new DebugLayer(false));
        scene.setTitle("HelloWorld");
        scene.setTileSize(30);
        scene.setSpriteView("ball", new ImageSpriteView("/ball.png", 8, 4));
        scene.addSceneController(new SpriteController(ball));
        scene.addSceneController(new AdjustViewController());
        scene.addComponent(new SLabel("Click CTRL-D to switch debug mode, use Arrows to move ball"));

        //start game
        game.start();
    }
}
