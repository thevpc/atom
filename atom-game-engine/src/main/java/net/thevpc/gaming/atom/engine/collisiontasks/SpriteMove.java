package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.ModelPoint;

class SpriteMove {

    int spriteId;
    ModelPoint oldLocation;
    ModelPoint newLocation;

    public SpriteMove(int spriteId, ModelPoint oldLocation, ModelPoint newLocation) {
        this.spriteId = spriteId;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public ModelPoint getOldLocation() {
        return oldLocation;
    }

    public ModelPoint getNewLocation() {
        return newLocation;
    }
}
