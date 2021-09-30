package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.CollisionSides;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Tile;

import java.util.Collection;

public class CollisionInfo {

     Object collisionInitiator;
     Object collisionObstacle;
     CollisionSides initiatorCollisionSides;
     CollisionSides otherCollisionSides;
     ModelPoint initiatorPreferredPosition;
     ModelPoint initiatorLastPosition;
     ModelPoint initiatorNextPosition;
     ModelPoint otherPreferredPosition;
     ModelPoint otherLastPosition;
     ModelPoint otherNextPosition;
     Collection<Tile> collisionTiles;

    public CollisionInfo setCollisionInitiator(Object collisionInitiator) {
        this.collisionInitiator = collisionInitiator;
        return this;
    }

    public CollisionInfo setCollisionObstacle(Object collisionObstacle) {
        this.collisionObstacle = collisionObstacle;
        return this;
    }

    public CollisionInfo setInitiatorCollisionSides(CollisionSides initiatorCollisionSides) {
        this.initiatorCollisionSides = initiatorCollisionSides;
        return this;
    }

    public CollisionInfo setOtherCollisionSides(CollisionSides otherCollisionSides) {
        this.otherCollisionSides = otherCollisionSides;
        return this;
    }

    public CollisionInfo setInitiatorPreferredPosition(ModelPoint initiatorPreferredPosition) {
        this.initiatorPreferredPosition = initiatorPreferredPosition;
        return this;
    }

    public CollisionInfo setInitiatorLastPosition(ModelPoint initiatorLastPosition) {
        this.initiatorLastPosition = initiatorLastPosition;
        return this;
    }

    public CollisionInfo setInitiatorNextPosition(ModelPoint initiatorNextPosition) {
        this.initiatorNextPosition = initiatorNextPosition;
        return this;
    }

    public CollisionInfo setCollisionTiles(Collection<Tile> collisionTiles) {
        this.collisionTiles = collisionTiles;
        return this;
    }

    public Object getCollisionInitiator() {
        return collisionInitiator;
    }

    public Object getCollisionObstacle() {
        return collisionObstacle;
    }

    public CollisionSides getInitiatorCollisionSides() {
        return initiatorCollisionSides;
    }

    public CollisionSides getOtherCollisionSides() {
        return otherCollisionSides;
    }

    public ModelPoint getInitiatorPreferredPosition() {
        return initiatorPreferredPosition;
    }

    public ModelPoint getInitiatorLastPosition() {
        return initiatorLastPosition;
    }

    public ModelPoint getInitiatorNextPosition() {
        return initiatorNextPosition;
    }

    public Collection<Tile> getCollisionTiles() {
        return collisionTiles;
    }

    public ModelPoint getOtherPreferredPosition() {
        return otherPreferredPosition;
    }

    public CollisionInfo setOtherPreferredPosition(ModelPoint otherPreferredPosition) {
        this.otherPreferredPosition = otherPreferredPosition;
        return this;
    }

    public ModelPoint getOtherLastPosition() {
        return otherLastPosition;
    }

    public CollisionInfo setOtherLastPosition(ModelPoint otherLastPosition) {
        this.otherLastPosition = otherLastPosition;
        return this;
    }

    public ModelPoint getOtherNextPosition() {
        return otherNextPosition;
    }

    public CollisionInfo setOtherNextPosition(ModelPoint otherNextPosition) {
        this.otherNextPosition = otherNextPosition;
        return this;
    }
}
