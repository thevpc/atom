package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.util.CollectionHashMap;

import java.util.*;

import static java.lang.Math.max;

/**
 * Created with IntelliJ IDEA. User: vpc Date: 9/6/13 Time: 11:42 AM To change
 * this template use File | Settings | File Templates.
 */
public class DiscreteSceneCollisionManager implements SceneCollisionManager {

    private final int maxIterations = 10;
    double modelPrecision = 1E-3;
    CollectionHashMap<Integer, Collision> spriteCollisionsList = new CollectionHashMap<>();

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

    @Override
    public void nextFrame(SceneEngine engine) {
        spriteCollisionsList = detectCollisions(engine, null);
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
            sprite.setCollisionSides(CollisionSides.of(CollisionSides.SIDE_NONE));
//            sprite000=sprite;
            SpriteMove m = movesMap.get(sprite.getId());
            if (m == null) {
                ModelPoint lastSpritePosition = sprite.getPreviousLocation();
                if (lastSpritePosition != null) {
                    motionObstacles.add(new Obstacle(ObstacleType.SPRITE, sprite.getName(),
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
                    motionObstacles.add(new Obstacle(ObstacleType.SPRITE, sprite.getName(),
                            sprite, new ModelBox(lastSpritePosition, sprite.getSize()), new ModelBox(nextSpritePosition, sprite.getSize()), true, sprite.isCrossable(), false
                    ));
                }
            }
        }
        //TODO should cache this
        for (Tile tile : engine.getObstacleTiles()) {
            motionlessObstacles.add(new Obstacle(ObstacleType.TILE, "tile:" + tile, tile, tile.getBounds(), tile.getBounds(), false, false, true));
        }
        ModelDimension size = engine.getModel().getSize();
        double xUniverse = size.getWidth() * 100;
        double xUniverseMin = -xUniverse;
        double xUniverseWidth = size.getWidth() + 2 * xUniverse;

        double yUniverse = size.getHeight() * 100;
        double yUniverseMin = -yUniverse;
        double yUniverseWidth = size.getHeight() + 2 * yUniverse;
//        if(sprite000!=null){
//            if(sprite000.getLocation().getX()>9){
//                System.out.println("And so?");
//            }
//        }
        //TODO should cache this
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_NORTH, "SIDE_NORTH", CollisionSides.NORTH, new ModelBox(xUniverseMin, yUniverseMin, xUniverseWidth, yUniverse), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_SOUTH, "SIDE_SOUTH", CollisionSides.SOUTH, new ModelBox(xUniverseMin, size.getHeight(), xUniverseWidth, yUniverse), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_EAST, "SIDE_EAST", CollisionSides.EAST, new ModelBox(xUniverseMin, yUniverseMin, xUniverse, yUniverseWidth), null, false, false, true));
        motionlessObstacles.add(new Obstacle(ObstacleType.BORDER_WEST, "SIDE_WEST", CollisionSides.WEST, new ModelBox(size.getWidth(), yUniverseMin, xUniverse, yUniverseWidth), null, false, false, true));
        DiscreteSceneCollisionManagerHelper h=new DiscreteSceneCollisionManagerHelper(maxIterations,modelPrecision,engine.getModel().getSize());
        List<CollisionInfo> collisionInfos = h.evalCollisions(engine, motionObstacles, motionlessObstacles);
        for (CollisionInfo o : collisionInfos) {
            if (o.getCollisionInitiator() instanceof Sprite) {
                Sprite sprite = (Sprite) o.getCollisionInitiator();
                int spriteId = sprite.getId();
                if (o.getCollisionObstacle() instanceof Sprite) {
                    Sprite other = (Sprite) o.getCollisionObstacle();
                    spriteCollisionsList.add(spriteId, new SpriteCollision(
                            engine, other, o.getOtherCollisionSides(), sprite, o.getInitiatorCollisionSides(), true, o.getCollisionTiles(),
                            o.getInitiatorLastPosition(), o.getInitiatorPreferredPosition(),
                            o.getOtherLastPosition(), o.getOtherPreferredPosition()
                    ));
                } else if (o.getCollisionObstacle() instanceof Tile) {
                    Tile other = (Tile) o.getCollisionObstacle();
                    spriteCollisionsList.add(spriteId, new TileCollision(
                            engine, other, o.getOtherCollisionSides(), sprite, o.getInitiatorCollisionSides(), o.getInitiatorLastPosition(), o.getInitiatorPreferredPosition()
                    ));
                } else if (o.getCollisionObstacle() instanceof CollisionSides) {
                    //borderBox
                    CollisionSides border = (CollisionSides) o.getCollisionObstacle();
                    BorderCollision lastBorderCollision = null;
                    for (Collision collision : spriteCollisionsList.getValues(spriteId)) {
                        if (collision instanceof BorderCollision) {
                            lastBorderCollision = (BorderCollision) collision;
                            break;
                        }
                    }
                    if (lastBorderCollision != null) {
                        spriteCollisionsList.remove(spriteId, lastBorderCollision);
                        CollisionSides allBorders = lastBorderCollision.getBorderCollisionSides().append(border);
                        spriteCollisionsList.add(spriteId, new BorderCollision(
                                engine, allBorders, sprite, o.getInitiatorCollisionSides(), o.getInitiatorLastPosition(), o.getInitiatorPreferredPosition()
                        ));
                    } else {
                        spriteCollisionsList.add(spriteId, new BorderCollision(
                                engine, border, sprite, o.getInitiatorCollisionSides(), o.getInitiatorLastPosition(), o.getInitiatorPreferredPosition()
                        ));
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return spriteCollisionsList;
    }

    public List<CollisionInfo> resolveNewCollisions(SceneEngine engine, List<Obstacle> motionObstacles, List<Obstacle> allObstacles) {
        List<CollisionInfo> cc = new ArrayList<>();
        for (Obstacle o1 : motionObstacles) {
            for (Obstacle o2 : allObstacles) {
                if (o1 != o2) {
                    if (o1.nextBox.intersects(o2.nextBox)) {
                        CollisionInfo c = new CollisionInfo();
                        c.setCollisionInitiator(o1);
                        c.setCollisionObstacle(o2);
                        cc.add(c);
                    }
                }
            }
        }
        return cc;
    }



    //get all obstacles/sprites
    //return only those modified


    public static class OldCollData {
        List<CollisionInfo> collisions = new ArrayList<>();
        List<Obstacle> subSetMotionObstacles = new ArrayList<>();
        List<Obstacle> subSetAllObstacles = new ArrayList<>();
    }
}
