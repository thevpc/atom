package net.vpc.gaming.atom.engine.collision;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.util.AtomUtils;
import net.vpc.gaming.atom.util.CollectionHashMap;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.sqrt;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;

/**
 * Created with IntelliJ IDEA. User: vpc Date: 9/6/13 Time: 11:42 AM To change
 * this template use File | Settings | File Templates.
 */
public class DiscreteSceneCollisionManager implements SceneCollisionManager {

    double modelPrecision = 1E-3;
    CollectionHashMap<Integer, Collision> spriteCollisionsList = new CollectionHashMap<>();
    private int maxIterations = 10;

    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
        CollectionHashMap<Integer, Collision> m = detectCollisions(engine, Arrays.asList(new SpriteMove(sprite.getId(), oldLocation, newLocation)));
        List<Collision> values = (List<Collision>) m.getValues(sprite.getId());
        return filterCollisions(values, borderCollision, tileCollision, spriteCollision);
    }

    @Override
    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
        List<Collision> values = (List<Collision>) spriteCollisionsList.getValues(sprite.getId());
        return filterCollisions(values, borderCollision, tileCollision, spriteCollision);
    }

    public List<Collision> filterCollisions(List<Collision> values, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
        if (borderCollision && tileCollision && spriteCollision) {
            return values;
        }
        List<Collision> filteredValues = new ArrayList<>();
        for (Collision value : values) {
            if ((value instanceof BorderCollision)) {
                if (!borderCollision) {
                    continue;
                }
            } else if ((value instanceof TileCollision)) {
                if (!tileCollision) {
                    continue;
                }
            } else if ((value instanceof SpriteCollision)) {
                if (!spriteCollision) {
                    continue;
                }
            }
            filteredValues.add(value);
        }
        return filteredValues;
    }

    @Override
    public void nextFrame(SceneEngine engine) {
        spriteCollisionsList = detectCollisions(engine, null);
    }

    private CollectionHashMap<Integer, Collision> detectCollisions(SceneEngine engine, List<SpriteMove> moves) {
        Map<Integer, SpriteMove> movesMap = new HashMap<>();
        if (moves != null) {
            for (SpriteMove move : moves) {
                movesMap.put(move.getSpriteId(), move);
            }
        }
        //should eval all collisions
        CollectionHashMap<Integer, Collision> spriteCollisionsList = new CollectionHashMap<>();
        List<Obstacle> motionlessObstacles = new ArrayList<>();
        List<Obstacle> motionObstacles = new ArrayList<>();
        for (Sprite sprite : engine.getSprites()) {
            SpriteMove m = movesMap.get(sprite.getId());
            if (m == null) {
                ModelPoint lastSpritePosition = engine.getLastSpritePosition(sprite.getId());
                if (lastSpritePosition != null) {
                    motionObstacles.add(new Obstacle(ObstacleType.SPRITE,
                            sprite, new ModelBox(lastSpritePosition, sprite.getSize()), sprite.getBounds(), true, sprite.isCrossable(), false
                    ));
                }
            } else {
                ModelPoint lastSpritePosition = m.getOldLocation();
                if (lastSpritePosition == null) {
                    lastSpritePosition = engine.getLastSpritePosition(sprite.getId());
                }
                ModelPoint nextSpritePosition = m.getNewLocation();
                if (nextSpritePosition == null) {
                    nextSpritePosition = sprite.getLocation();
                }
                if (lastSpritePosition != null) {
                    motionObstacles.add(new Obstacle(ObstacleType.SPRITE,
                            sprite, new ModelBox(lastSpritePosition, sprite.getSize()), new ModelBox(nextSpritePosition, sprite.getSize()), true, sprite.isCrossable(), false
                    ));
                }
            }
        }
        //TODO should cache this
        for (Tile tile : engine.getObstacleTiles()) {
            motionlessObstacles.add(new Obstacle(ObstacleType.TILE, tile, tile.getBounds(), tile.getBounds(), false, false, true));
        }
        ModelDimension size = engine.getModel().getSize();
        int borderPrecision = 10;
        //TODO should cache this
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER, Collision.SIDE_NORTH, new ModelBox(0, -borderPrecision, size.getWidth(), borderPrecision), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER, Collision.SIDE_SOUTH, new ModelBox(0, size.getHeight(), size.getWidth(), borderPrecision), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER, Collision.SIDE_WEST, new ModelBox(-borderPrecision, 0, borderPrecision, size.getHeight()), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER, Collision.SIDE_EAST, new ModelBox(size.getWidth(), 0, borderPrecision, size.getHeight()), null, false, false, true));

        for (CollisionInfo o : evalCollisions(engine, motionObstacles, motionlessObstacles)) {
            if (o.collisionInitiator instanceof Sprite) {
                Sprite sprite = (Sprite) o.collisionInitiator;
                int spriteId = sprite.getId();
                if (o.collisionObstacle instanceof Sprite) {
                    Sprite other = (Sprite) o.collisionObstacle;
                    spriteCollisionsList.add(spriteId, new SpriteCollision(
                            engine, other, o.otherCollisionSides, sprite, o.collisionSides, true, o.collisionTiles, o.lastPosition, o.preferredPosition
                    ));
                } else if (o.collisionObstacle instanceof Tile) {
                    Tile other = (Tile) o.collisionObstacle;
                    spriteCollisionsList.add(spriteId, new TileCollision(
                            engine, other, o.otherCollisionSides, sprite, o.collisionSides, o.lastPosition, o.preferredPosition
                    ));
                } else if (o.collisionObstacle instanceof Integer) {
                    //borderBox
                    Integer border = (Integer) o.collisionObstacle;
                    BorderCollision lastBorderCollision = null;
                    for (Collision collision : spriteCollisionsList.getValues(spriteId)) {
                        if (collision instanceof BorderCollision) {
                            lastBorderCollision = (BorderCollision) collision;
                            break;
                        }
                    }
                    if (lastBorderCollision != null) {
                        spriteCollisionsList.remove(spriteId, lastBorderCollision);
                        int allBorders = border | lastBorderCollision.getBorderCollisionSides();
                        spriteCollisionsList.add(spriteId, new BorderCollision(
                                engine, allBorders, sprite, o.collisionSides, o.lastPosition, o.preferredPosition
                        ));
                    } else {
                        spriteCollisionsList.add(spriteId, new BorderCollision(
                                engine, border, sprite, o.collisionSides, o.lastPosition, o.preferredPosition
                        ));
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return spriteCollisionsList;
    }

    //get all obstacles/sprites
    //return only those modified
    public List<CollisionInfo> evalCollisions(SceneEngine engine, List<Obstacle> motionObstacles, List<Obstacle> motionlessObstacles) {
//        motionObstacles.addAll(motionlessObstacles);
//        motionlessObstacles.clear();
        Set<Integer> collided = new HashSet<>();
        List<Obstacle> allObstacles = new ArrayList<>();
        int currentId = 0;
        for (Obstacle obstacle : motionObstacles) {
            obstacle.id = currentId++;
            allObstacles.add(obstacle);
        }
        for (Obstacle obstacle : motionlessObstacles) {
            obstacle.id = currentId++;
            allObstacles.add(obstacle);
        }
        for (int i = maxIterations; i >= 0; i--) {
            CollectionHashMap<Integer, Integer> obstacleChecks = new CollectionHashMap<>();
            for (int s1 = 0; s1 < motionObstacles.size(); s1++) {
                Obstacle o1 = motionObstacles.get(s1);
                for (int s2 = s1 + 1; s2 < allObstacles.size(); s2++) {
                    Obstacle o2 = allObstacles.get(s2);
                    boolean moved1 = !o1.oldBox.equals(o1.nextBox);
                    boolean moved2 = !o2.oldBox.equals(o2.nextBox);
                    if ( //at least one obs moved
                            (moved1 || moved2)
                            //at least one obs moved
                            && o1.getBoundingBox().intersects(o2.getBoundingBox())) {
                        if (moved1) {
                            obstacleChecks.add(s1, s2);
                        } else {
                            obstacleChecks.add(s2, s1);
                        }
                    }
                }
            }
//        for (int i = maxIterations; i >= 0; i--) {
            boolean coll = false;
            for (Map.Entry<Integer, Collection<Integer>> e : obstacleChecks.entrySet()) {
                int s1 = e.getKey();
                Obstacle o1 = allObstacles.get(s1);
                for (Integer s2 : e.getValue()) {
                    Obstacle o2 = allObstacles.get(s2);
                    if (eval(o1, o2)) {
                        if (!o1.collidesWith.contains(o2)) {
                            o1.collidesWith.add(o2);
                            collided.add(o1.id);
                        }
                        coll = true;
                    }
                }
            }
            if (!coll) {
                break;
            }
        }

        List<CollisionInfo> collisions = new ArrayList<>();
        for (Integer integer : collided) {
            Obstacle obstacle = allObstacles.get(integer);
            if (obstacle.collides) {
                double oX = obstacle.originalNextBox.getX();
                double oY = obstacle.originalNextBox.getY();
                double fX = obstacle.nextBox.getX();
                double fY = obstacle.nextBox.getY();
                int collisionSides = 0;
                int otherCollisionSides = 0;
                if (fX > oX) {
                    collisionSides = Collision.SIDE_WEST;
                    otherCollisionSides = Collision.SIDE_EAST;
                } else if (fX < oX) {
                    collisionSides = Collision.SIDE_EAST;
                    otherCollisionSides = Collision.SIDE_WEST;
                }
                if (fY > oY) {
                    collisionSides = Collision.SIDE_NORTH;
                    otherCollisionSides = Collision.SIDE_SOUTH;
                } else if (fY < oY) {
                    collisionSides = Collision.SIDE_SOUTH;
                    otherCollisionSides = Collision.SIDE_NORTH;
                }
                for (Obstacle other : obstacle.collidesWith) {
                    CollisionInfo i = new CollisionInfo();
                    i.collisionInitiator = obstacle.element;
                    i.collisionObstacle = other.element;
                    i.collisionSides = collisionSides;
                    i.otherCollisionSides = otherCollisionSides;
                    i.preferredPosition = obstacle.nextBox.getLocation();
                    i.lastPosition = obstacle.oldBox.getLocation();
                    i.nextPosition = obstacle.originalNextBox.getLocation();
                    i.collisionTiles = engine.findTiles(obstacle.nextBox);
                    i.collisionTiles.retainAll(engine.findTiles(other.nextBox));
                    collisions.add(i);
                }
            }
        }
        return collisions;
    }

    private boolean eval(Obstacle o1, Obstacle o2) {
        ModelBox specIntBounds = o1.getBoundingBox().intersect(o2.getBoundingBox());
        if (specIntBounds.isEmpty()) {
            return false;
        }
        if (o1.crossable || o2.crossable) {
            //change nothing
            o1.collides = true;
            return true;
        }
        double oldX = o1.oldBox.getX();
        double oldY = o1.oldBox.getY();
        double width = o1.nextBox.getWidth();
        double height = o1.nextBox.getHeight();

        double nextMoveX = o1.nextBox.getX() - oldX;
        double nextMoveY = o1.nextBox.getY() - oldY;
        double safeMoveX = max(max(specIntBounds.getMinX() - (o1.nextBox.getX() + width), o1.nextBox.getX() - specIntBounds.getMaxX()), 0);
        double safeMoveY = max(max(specIntBounds.getMinY() - (o1.nextBox.getY() + height), o1.nextBox.getY() - specIntBounds.getMaxY()), 0);
        double safeVecLen = sqrt(safeMoveX * safeMoveX + safeMoveY * safeMoveY);

        // Calculate the length of the movement vector using Pythagoras
        double vectorLength = sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
        if (vectorLength > 0) {
            // Our current position along the anticipated movement vector of the player this frame

            //vectorLength -= safeVecLen;
            double a = 0;
            double b = vectorLength - safeVecLen;
            //dicotomic collision detection
            while (!AtomUtils.safeEquals(a, b, modelPrecision)) {
                double x = (a + b) / 2;
                double projectedMoveX = nextMoveX * ((safeVecLen + x) / vectorLength);
                double projectedMoveY = nextMoveY * ((safeVecLen + x) / vectorLength);
                o1.nextBox = new ModelBox(oldX + projectedMoveX, oldY + projectedMoveY, width, height);
                boolean someCollision = false;
                if (o1.nextBox.intersects(o2.nextBox)) {
                    //should do extra test shape based
                    someCollision = true;
                }
                if (someCollision) {
                    b = x;
                } else {
                    a = x;
                }
            }
            //should
            if (AtomUtils.safeEquals(a, 0, modelPrecision)) {
                //disable all update
                double x = 0;
                double projectedMoveX = nextMoveX * ((safeVecLen + x) / vectorLength);
                double projectedMoveY = nextMoveY * ((safeVecLen + x) / vectorLength);
                o1.nextBox = new ModelBox(oldX + projectedMoveX, oldY + projectedMoveY, width, height);
            }
            if (!AtomUtils.safeEquals(o1.nextBox.getX(), o1.originalNextBox.getX(), modelPrecision)
                    || !AtomUtils.safeEquals(o1.nextBox.getY(), o1.originalNextBox.getY(), modelPrecision)) {

                o1.collides = true;
                return true;
            }
        }
        return false;
    }

    private static enum ObstacleType {

        SPRITE, TILE, BORDER
    }

    private static class Obstacle {

        int id;
        ObstacleType type;
        Object element;
        ModelBox oldBox;
        ModelBox originalNextBox;
        ModelBox nextBox;
        boolean collides;
        boolean crossable;
        List<Obstacle> collidesWith = new ArrayList<>();
        boolean adjustablePositioning = true;
        boolean motionless = false;

        public Obstacle(ObstacleType type, Object element, ModelBox oldBox, ModelBox nextBox, boolean adjustablePositioning, boolean crossable, boolean motionless) {
            this.type = type;
            this.element = element;
            this.oldBox = oldBox;
            this.nextBox = nextBox == null ? oldBox : nextBox;
            this.adjustablePositioning = adjustablePositioning;
            this.crossable = crossable;
            this.motionless = motionless;
            this.originalNextBox = this.nextBox;
        }

        public ModelBox getBoundingBox() {
            return oldBox.unionBox(nextBox);
        }
    }

    private static class CollisionInfo {

        Object collisionInitiator;
        Object collisionObstacle;
        int collisionSides;
        int otherCollisionSides;
        ModelPoint preferredPosition;
        ModelPoint lastPosition;
        ModelPoint nextPosition;
        Collection<Tile> collisionTiles;

    }

    protected static class SpriteMove {

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

    private static class ObstaclePair {

        int o1;
        int o2;

        public ObstaclePair(int o1, int o2) {
            this.o1 = o1;
            this.o2 = o2;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + this.o1;
            hash = 11 * hash + this.o2;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ObstaclePair other = (ObstaclePair) obj;
            if (this.o1 != other.o1) {
                return false;
            }
            if (this.o2 != other.o2) {
                return false;
            }
            return true;
        }

    }
}
