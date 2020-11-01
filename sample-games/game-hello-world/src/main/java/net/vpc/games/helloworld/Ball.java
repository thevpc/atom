package net.vpc.games.helloworld;

import net.vpc.gaming.atom.annotations.AtomSprite;
import net.vpc.gaming.atom.annotations.Inject;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.engine.collision.SimpleSpriteCollisionManager;
import net.vpc.gaming.atom.engine.tasks.MoveSpriteTask;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.Scene;

/**
 * Created by vpc on 9/23/16.
 */
@AtomSprite(
        name = "ball",
        kind = "ball",
        scene = "hello",
        x=2,
        y=2,
        direction = Math.PI/4,
        speed = 0.2,
        task = MoveSpriteTask.class,
        collision = SimpleSpriteCollisionManager.class
        
)
public class Ball {
    @Inject
    Sprite sprite;
    @Inject
    Scene scene;

    @OnInstall
    private void init(){
        scene.setSpriteView("ball", new ImageSpriteView("/ball.png", 8, 4));
    }
}
