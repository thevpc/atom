package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnInstall;
import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;
import net.thevpc.gaming.atom.presentation.Scene;

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
        mainTask = MoveSpriteMainTask.class,
        collisionTask = StopSpriteCollisionTask.class
        
)
public class Ball {
    @Inject
    Sprite sprite;
    @Inject
    Scene scene;

    @OnInstall
    private void init(){
        sprite.setLocation(
                Math.random()*scene.getModel().getTileSize().getWidth(),
                Math.random()*scene.getModel().getTileSize().getHeight()
        );
        scene.setSpriteView("ball", new ImageSpriteView("/ball.png", 8, 4));
    }
}
