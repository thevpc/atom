package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.CollisionSides;
import net.thevpc.gaming.atom.model.ModelBox;

public class ObstacleCollInfo {

    public Obstacle me;
    public Obstacle other;
    public ModelBox intersection;
    public CollisionSides meCollisionSides;
    public CollisionSides otherCollisionSides;

    public ObstacleCollInfo(Obstacle me, Obstacle other, ModelBox intersection, CollisionSides meCollisionSides) {
        this.me = me;
        this.other = other;
        this.intersection = intersection;
        this.meCollisionSides = meCollisionSides;

        if (meCollisionSides.isEast()) {
            otherCollisionSides = CollisionSides.of(CollisionSides.SIDE_WEST);
        } else if (meCollisionSides.isWest()) {
            otherCollisionSides = CollisionSides.of(CollisionSides.SIDE_EAST);
        } else if (meCollisionSides.isNorth()) {
            otherCollisionSides = CollisionSides.of(CollisionSides.SIDE_SOUTH);
        } else if (meCollisionSides.isSouth()) {
            otherCollisionSides = CollisionSides.of(CollisionSides.SIDE_NORTH);
        } else {
            otherCollisionSides = CollisionSides.of(CollisionSides.SIDE_NONE);
        }
    }

}
