package net.vpc.games.atom.examples.kombla.main.shared.engine.collision;

import net.vpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.DefaultSpriteCollisionManager;

/**
 * Created by vpc on 9/25/16.
 */
@AtomSpriteCollisionManager(
        engine = "mainLocal,mainServer",
        kind = "Bomb"
)
public class BombCollisionManager extends DefaultSpriteCollisionManager{
}
