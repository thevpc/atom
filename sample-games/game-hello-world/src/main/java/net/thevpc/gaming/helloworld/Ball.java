package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.collisiontasks.BounceSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Created by vpc on 9/23/16.
 */
@AtomSprite(
        name = "Ball1",
        kind = "Ball",
        sceneEngine = "hello",
        x=2,
        y=2,
        direction = Math.PI/4,
        speed = 0.2,
        mainTask = MoveSpriteMainTask.class,
        collisionTask = StopSpriteCollisionTask.class
//        collisionTask = BounceSpriteCollisionTask.class

)
public class Ball {
    @Inject
    Sprite sprite;
    @Inject
    SceneEngine sceneEngine;

    @OnInit
    private void init(){
        sprite.setLocation(2,2);
    }
}
