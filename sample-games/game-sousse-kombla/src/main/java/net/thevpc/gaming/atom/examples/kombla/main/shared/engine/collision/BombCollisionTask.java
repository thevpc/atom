package net.thevpc.gaming.atom.examples.kombla.main.shared.engine.collision;

import net.thevpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.thevpc.gaming.atom.engine.collisiontasks.DefaultSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;

/**
 * Created by vpc on 9/25/16.
 */
@AtomSpriteCollisionManager(
        engine = "mainLocal,mainServer",
        kind = "Bomb"
)
public class BombCollisionTask implements SpriteCollisionTask {
}
