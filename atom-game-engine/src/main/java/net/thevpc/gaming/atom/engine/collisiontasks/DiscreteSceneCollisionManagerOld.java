package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.util.AtomUtils;
import net.thevpc.gaming.atom.util.CollectionHashMap;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.sqrt;

/**
 * Created with IntelliJ IDEA. User: vpc Date: 9/6/13 Time: 11:42 AM To change
 * this template use File | Settings | File Templates.
 */
public class DiscreteSceneCollisionManagerOld implements SceneCollisionManager {

    double modelPrecision = 1E-3;
    CollectionHashMap<Integer, Collision> spriteCollisionsList = new CollectionHashMap<>();
    private int maxIterations = 10;

//    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
//        CollectionHashMap<Integer, Collision> m = detectCollisions(engine, Arrays.asList(new SpriteMove(sprite.getId(), sprite.getPreviousLocation(), sprite.getLocation())));
//        List<Collision> values = (List<Collision>) m.getValues(sprite.getId());
//        return filterCollisions(values, borderCollision, tileCollision, spriteCollision);
//    }

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
//        Sprite sprite000=null;
        for (Sprite sprite : engine.getSprites()) {
            //reset collision tasks sides
            sprite.setCollisionSides(CollisionSides.NONE);
//            sprite000=sprite;
            SpriteMove m = movesMap.get(sprite.getId());
            if (m == null) {
                ModelPoint lastSpritePosition =sprite.getPreviousLocation();
                if (lastSpritePosition != null) {
                    motionObstacles.add(new Obstacle(ObstacleType.SPRITE,sprite.getName(),
                            sprite, new ModelBox(lastSpritePosition, sprite.getSize()), sprite.getBounds(), true, sprite.isCrossable(), false
                    ));
                }
            } else {
                ModelPoint lastSpritePosition = m.getOldLocation();
                if (lastSpritePosition == null) {
                    lastSpritePosition = sprite.getPreviousLocation();
                }
                ModelPoint nextSpritePosition = m.getNewLocation();
                if (nextSpritePosition == null) {
                    nextSpritePosition = sprite.getLocation();
                }
                if (lastSpritePosition != null) {
                    motionObstacles.add(new Obstacle(ObstacleType.SPRITE,sprite.getName(),
                            sprite, new ModelBox(lastSpritePosition, sprite.getSize()), new ModelBox(nextSpritePosition, sprite.getSize()), true, sprite.isCrossable(), false
                    ));
                }
            }
        }
        //TODO should cache this
        for (Tile tile : engine.getObstacleTiles()) {
            motionlessObstacles.add(new Obstacle(ObstacleType.TILE,"tile:"+tile, tile, tile.getBounds(), tile.getBounds(), false, false, true));
        }
        ModelDimension size = engine.getModel().getSize();
        double xUniverse = size.getWidth()*100;
        double xUniverseMin = -xUniverse;
        double xUniverseWidth = size.getWidth()+2*xUniverse;

        double yUniverse = size.getHeight()*100;
        double yUniverseMin = -yUniverse;
        double yUniverseWidth = size.getHeight()+2*yUniverse;
//        if(sprite000!=null){
//            if(sprite000.getLocation().getX()>9){
//                System.out.println("And so?");
//            }
//        }
        //TODO should cache this
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_NORTH, "SIDE_NORTH",CollisionSides.SIDE_NORTH, new ModelBox(xUniverseMin, yUniverseMin, xUniverseWidth, yUniverse), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_SOUTH, "SIDE_SOUTH",CollisionSides.SIDE_SOUTH, new ModelBox(xUniverseMin, size.getHeight(), xUniverseWidth, yUniverse), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_EAST, "SIDE_EAST",CollisionSides.SIDE_EAST, new ModelBox(xUniverseMin, yUniverseMin, xUniverse, yUniverseWidth), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_WEST, "SIDE_EAST",CollisionSides.SIDE_EAST, new ModelBox(size.getWidth(), yUniverseMin, xUniverse, yUniverseWidth), null, false, false, true));

        for (CollisionInfo o : evalCollisions(engine, motionObstacles, motionlessObstacles)) {
            if (o.collisionInitiator instanceof Sprite) {
                Sprite sprite = (Sprite) o.collisionInitiator;
                int spriteId = sprite.getId();
                if (o.collisionObstacle instanceof Sprite) {
                    Sprite other = (Sprite) o.collisionObstacle;
                    spriteCollisionsList.add(spriteId, new SpriteCollision(
                            engine, other, o.otherCollisionSides, sprite, o.initiatorCollisionSides, true, o.collisionTiles, o.initiatorLastPosition, o.initiatorPreferredPosition,
                            o.getOtherLastPosition(),
                            o.getOtherPreferredPosition()
                    ));
                } else if (o.collisionObstacle instanceof Tile) {
                    Tile other = (Tile) o.collisionObstacle;
                    spriteCollisionsList.add(spriteId, new TileCollision(
                            engine, other, o.otherCollisionSides, sprite, o.initiatorCollisionSides, o.initiatorLastPosition, o.initiatorPreferredPosition
                    ));
                } else if (o.collisionObstacle instanceof CollisionSides) {
                    //borderBox
                    CollisionSides border = (CollisionSides) o.collisionObstacle;
                    BorderCollision lastBorderCollision = null;
                    for (Collision collision : spriteCollisionsList.getValues(spriteId)) {
                        if (collision instanceof BorderCollision) {
                            lastBorderCollision = (BorderCollision) collision;
                            break;
                        }
                    }
                    if (lastBorderCollision != null) {
                        spriteCollisionsList.remove(spriteId, lastBorderCollision);
                        CollisionSides allBorders = border.append(lastBorderCollision.getBorderCollisionSides());
                        spriteCollisionsList.add(spriteId, new BorderCollision(
                                engine, allBorders, sprite, o.initiatorCollisionSides, o.initiatorLastPosition, o.initiatorPreferredPosition
                        ));
                    } else {
                        spriteCollisionsList.add(spriteId, new BorderCollision(
                                engine, border, sprite, o.initiatorCollisionSides, o.initiatorLastPosition, o.initiatorPreferredPosition
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
//                            (moved1 || moved2)&&
                                    //at least one obs moved
                                    o1.getBoundingBox().intersects(o2.getBoundingBox())) {
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
                        if (!o1.isCollideWith(o2)) {
                            ModelBox intersection = o1.getOriginalBoundingBox().intersect(o2.getOriginalBoundingBox());
                            CollisionSides sides = CollisionSides.of(minkowskiSum(o1, o2));
                            if (!intersection.isEmpty()) {
                                if (!sides.isNone()) {
                                    ObstacleCollInfo e1 = new ObstacleCollInfo(
                                            o1,
                                            o2, intersection,
                                            sides
                                    );
                                    o1.collidesWith.add(e1);
                                    collided.add(o1.id);
                                } else {
//                                    System.out.println("Why");
                                }
                                //reevaluate collisiontasks in the other side to help update next position or the second sprite
                                 if(!o1.motionless && !o2.motionless){
                                    eval(o2,o1);
                                }
                            }
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
//                double oX = obstacle.originalNextBox.getX();
//                double oY = obstacle.originalNextBox.getY();
//                double fX = obstacle.nextBox.getX();
//                double fY = obstacle.nextBox.getY();
//                int collisionSides = 0;
//                int otherCollisionSides = 0;
//                if (fX > oX) {
//                    collisionSides = CollisionSides.SIDE_WEST;
//                    otherCollisionSides = CollisionSides.SIDE_EAST;
//                } else if (fX < oX) {
//                    collisionSides = CollisionSides.SIDE_EAST;
//                    otherCollisionSides = CollisionSides.SIDE_WEST;
//                }
//                if (fY > oY) {
//                    collisionSides = CollisionSides.SIDE_NORTH;
//                    otherCollisionSides = CollisionSides.SIDE_SOUTH;
//                } else if (fY < oY) {
//                    collisionSides = CollisionSides.SIDE_SOUTH;
//                    otherCollisionSides = CollisionSides.SIDE_NORTH;
//                }

                for (ObstacleCollInfo other : obstacle.collidesWith) {
                    CollisionInfo i = new CollisionInfo();
                    i.collisionInitiator = obstacle.element;
                    i.collisionObstacle = other.other.element;
                    i.initiatorCollisionSides = other.meCollisionSides;
                    i.otherCollisionSides = other.otherCollisionSides;
                    i.initiatorPreferredPosition = obstacle.nextBox.getLocation();
                    if (i.collisionInitiator instanceof Sprite) {
                        Sprite s = (Sprite) i.collisionInitiator;
                        s.setCollisionSides(s.getCollisionSides().append(i.initiatorCollisionSides));
                    }
                    if (i.collisionObstacle instanceof Sprite) {
                        Sprite s = (Sprite) i.collisionObstacle;
                        s.setCollisionSides(s.getCollisionSides().append(i.otherCollisionSides));
                    }
                    if (i.collisionInitiator instanceof Sprite) {
                        Sprite s = (Sprite) i.collisionInitiator;
                        double w = s.getWidth();
                        double h = s.getHeight();
                        double a = s.getAltitude();
                        ModelDimension size = engine.getModel().getSize();
                        if (i.initiatorPreferredPosition.getX() > size.getWidth() - w) {
                            i.initiatorPreferredPosition = new ModelPoint(
                                    size.getWidth() - w,
                                    i.initiatorPreferredPosition.getY(),
                                    i.initiatorPreferredPosition.getZ()
                            );
                        } else if (i.initiatorPreferredPosition.getX() < 0) {
                            i.initiatorPreferredPosition = new ModelPoint(
                                    0,
                                    i.initiatorPreferredPosition.getY(),
                                    i.initiatorPreferredPosition.getZ()
                            );
                        }
                        if (i.initiatorPreferredPosition.getY() > size.getHeight() - h) {
                            i.initiatorPreferredPosition = new ModelPoint(
                                    i.initiatorPreferredPosition.getX(),
                                    size.getHeight() - h,
                                    i.initiatorPreferredPosition.getZ()
                            );
                        } else if (i.initiatorPreferredPosition.getY() < 0) {
                            i.initiatorPreferredPosition = new ModelPoint(
                                    i.initiatorPreferredPosition.getX(),
                                    0,
                                    i.initiatorPreferredPosition.getZ()
                            );
                        }
                        if (i.initiatorPreferredPosition.getZ() > size.getAltitude() - a) {
                            i.initiatorPreferredPosition = new ModelPoint(
                                    i.initiatorPreferredPosition.getX(),
                                    i.initiatorPreferredPosition.getY(),
                                    size.getAltitude() - a
                            );
                        } else if (i.initiatorPreferredPosition.getY() < 0) {
                            i.initiatorPreferredPosition = new ModelPoint(
                                    i.initiatorPreferredPosition.getX(),
                                    i.initiatorPreferredPosition.getY(),
                                    0
                            );
                        }
                        ModelBox obox = other.other.nextBox;
                        if((i.initiatorCollisionSides.isNorth()) && i.initiatorPreferredPosition.getY() < obox.getMaxY()){
                            i.initiatorPreferredPosition =i.initiatorPreferredPosition.copyAndSetY(obox.getMaxY());
                        }
                        if((i.initiatorCollisionSides.isSouth()) && i.initiatorPreferredPosition.getY()+h > obox.getMinY()){
                            i.initiatorPreferredPosition =i.initiatorPreferredPosition.copyAndSetY(obox.getMinY()-h);
                        }
                        if((i.initiatorCollisionSides.isWest()) && i.initiatorPreferredPosition.getX() < obox.getMaxX()){
                            i.initiatorPreferredPosition =i.initiatorPreferredPosition.copyAndSetX(obox.getMaxX());
                        }
                        if((i.initiatorCollisionSides.isEast()) && i.initiatorPreferredPosition.getX()+w > obox.getMinX()){
                            i.initiatorPreferredPosition =i.initiatorPreferredPosition.copyAndSetX(obox.getMinX()-w);
                        }
                    }
                    i.initiatorLastPosition = obstacle.oldBox.getLocation();
                    i.initiatorNextPosition = i.initiatorPreferredPosition;//obstacle.originalNextBox.getLocation();
                    i.collisionTiles = engine.findTiles(obstacle.nextBox);
                    i.collisionTiles.retainAll(engine.findTiles(other.other.nextBox));
                    collisions.add(i);
                }
            }
        }
        return collisions;
    }

    private int minkowskiSum(Obstacle o1, Obstacle o2) {

        double w = 0.5 * (o1.getBoundingBox().getWidth() + o2.getBoundingBox().getWidth());
        double h = 0.5 * (o1.getBoundingBox().getHeight() + o2.getBoundingBox().getHeight());

        double dx = o1.getBoundingBox().getCenterX() - o2.getBoundingBox().getCenterX();
        double dy = o1.getBoundingBox().getCenterY() - o2.getBoundingBox().getCenterY();

        if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
            /* collisiontasks! */
            double wy = w * dy;
            double hx = h * dx;

            if (wy > hx) {
                if (wy > -hx) {
                    return CollisionSides.SIDE_NORTH;//CollisionSides.SIDE_NORTH;
                } else {
                    return CollisionSides.SIDE_EAST;//CollisionSides.SIDE_WEST;
                }
            } else if (wy > -hx) {
                return CollisionSides.SIDE_WEST;
            } else {
                return CollisionSides.SIDE_SOUTH;
            }

        }
        return 0;
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
            //dicotomic collisiontasks detection
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
                double x = (a + b) / 2;
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
//        o1.nextBox=o1.oldBox;
        o1.collides=true;
        return true;
    }







}
