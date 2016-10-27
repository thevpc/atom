/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.collision;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.ModelSegment;
import net.vpc.gaming.atom.model.ModelVector;
import net.vpc.gaming.atom.model.PolygonIntersect;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneCollisionManager implements SceneCollisionManager {

    @Override
    public void nextFrame(SceneEngine engine) {
        //
    }

    @Override
    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
        return detectCollisions(engine,sprite,engine.getLastSpritePosition(sprite.getId()), sprite.getLocation(),borderCollision, tileCollision, spriteCollision);
    }

    private static double[][] getMovingRectangleIntersection(ModelBox A, ModelVector velocity, ModelBox B, double borderDelta, double intersectionThreshold) {
        ModelPoint[] points = A.getModelPoints();
        double[][] ret = new double[4][];
        ModelBox TOP = new ModelBox(B.getLocation(), new ModelDimension(B.getWidth(), B.getHeight() * borderDelta, B.getAltitude()));
        ModelBox LEFT = new ModelBox(B.getLocation(), new ModelDimension(B.getWidth() * borderDelta, B.getHeight(), B.getAltitude()));
        ModelBox DOWN = new ModelBox(new ModelPoint(B.getX(), B.getMaxY() - B.getHeight() * borderDelta, B.getAltitude()), new ModelDimension(B.getWidth(), B.getHeight() * borderDelta, B.getAltitude()));
        ModelBox RIGHT = new ModelBox(new ModelPoint(B.getMaxX() - B.getWidth() * borderDelta, B.getY(), B.getAltitude()), new ModelDimension(B.getWidth() * borderDelta, B.getHeight(), B.getAltitude()));
        for (int i = 0; i < ret.length - 1; i++) {
            ret[i] = getMovingSegmentIntersection(new ModelSegment(points[i], points[i + 1]), velocity, TOP, RIGHT, DOWN, LEFT, intersectionThreshold);
        }
        ret[3] = getMovingSegmentIntersection(new ModelSegment(points[3], points[0]), velocity, TOP, RIGHT, DOWN, LEFT, intersectionThreshold);
        return ret;
    }

    private static double[] getMovingSegmentIntersection(ModelSegment s, ModelVector velocity, ModelBox TOP, ModelBox RIGHT, ModelBox DOWN, ModelBox LEFT, double intersectionThreshold) {
        ModelPoint p1 = s.getA();
        ModelPoint p2 = s.getB();
        ModelPoint p3 = new ModelPoint(p1.getX() + velocity.getX(), p1.getY() + velocity.getY(), p1.getZ() + velocity.getZ());
        ModelPoint p4 = new ModelPoint(p2.getX() + velocity.getX(), p2.getY() + velocity.getY(), p2.getZ() + velocity.getZ());


        double topIntersection = Math.abs(PolygonIntersect.intersectionArea(new ModelPoint[]{
                p1, p2, p4, p3,}, TOP.getModelPoints()) / TOP.getWidth() / TOP.getHeight());
        if (topIntersection < intersectionThreshold) {
            topIntersection = 0;
        }
        double downIntersection = Math.abs(PolygonIntersect.intersectionArea(new ModelPoint[]{
                p1, p2, p4, p3,}, DOWN.getModelPoints()) / DOWN.getWidth() / DOWN.getHeight());
        if (downIntersection < intersectionThreshold) {
            downIntersection = 0;
        }
        double leftIntersection = Math.abs(PolygonIntersect.intersectionArea(new ModelPoint[]{
                p1, p2, p4, p3,}, LEFT.getModelPoints()) / LEFT.getWidth() / LEFT.getHeight());
        if (leftIntersection < intersectionThreshold) {
            leftIntersection = 0;
        }
        double rightIntersection = Math.abs(PolygonIntersect.intersectionArea(new ModelPoint[]{
                p1, p2, p4, p3,}, RIGHT.getModelPoints()) / RIGHT.getWidth() / RIGHT.getHeight());
        if (rightIntersection < intersectionThreshold) {
            rightIntersection = 0;
        }
        return new double[]{topIntersection, rightIntersection, downIntersection, leftIntersection};
    }

    private static String ttt(ModelBox A, ModelBox B) {
        double w = 0.5 * (A.getWidth() + B.getWidth());
        double h = 0.5 * (A.getHeight() + B.getHeight());
        double dx = A.getCenterX() - B.getCenterX();
        double dy = A.getCenterY() - B.getCenterY();

        if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
            /* collision! */
            double wy = w * dy;
            double hx = h * dx;

            if (wy > hx) {
                if (wy > -hx) /* collision at the top */ {
                    return "top";
                } else /* on the left */ {
                    return "left";
                }
            } else if (wy > -hx) /* on the right */ {
                return "right";

            } else /* at the bottom */ {
                return "bottom";
            }
        }
        return null;
    }

    public List<Collision> detectCollisions(SceneEngine engine, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation, boolean borderCollision, boolean tileCollision, boolean spriteCollision) {
        newLocation = sprite.validatePosition(newLocation);
        ModelVector velocity = oldLocation == null ? null : ModelVector.newVector(oldLocation, newLocation);
        List<Collision> collisions = new ArrayList<Collision>();
        if (!sprite.isDead()) {
            double mwidth = engine.getSize().getWidth();
            double mheight = engine.getSize().getHeight();
            ModelBox nextBound = new ModelBox(newLocation, sprite.getSize());
            int borderCollisionSides = 0;
            int spriteCollisionSides = 0;
            boolean borderCollisionNorth = false;
            boolean borderCollisionWest = false;
            boolean borderCollisionEast = false;
            boolean borderCollisionSouth = false;
            if (nextBound.getMinX() < 0) {
                borderCollisionWest = true;
                borderCollisionSides |= Collision.SIDE_WEST;
                spriteCollisionSides |= Collision.SIDE_WEST;
            }
            if (nextBound.getMaxX() > mwidth) {
                borderCollisionEast = true;
                borderCollisionSides |= Collision.SIDE_EAST;
                spriteCollisionSides |= Collision.SIDE_EAST;
            }
            if (nextBound.getMinY() < 0) {
                borderCollisionNorth = true;
                borderCollisionSides |= Collision.SIDE_NORTH;
                spriteCollisionSides |= Collision.SIDE_NORTH;
            }
            if (nextBound.getMaxY() > mheight) {
                borderCollisionSouth = true;
                borderCollisionSides |= Collision.SIDE_SOUTH;
                spriteCollisionSides |= Collision.SIDE_SOUTH;
            }
            if (borderCollisionSides != 0) {
                if (borderCollision) {

                    ModelPoint p = sprite.getLocation();
                    double nextx = p.getX();
                    double nexty = p.getY();
                    double swidth = sprite.getWidth();
                    double sheight = sprite.getHeight();
                    if (borderCollisionWest) {
                        nextx = 0;
                    }
                    if (borderCollisionEast) {
                        nextx = (mwidth - swidth);
                    }
                    if (borderCollisionNorth) {
                        nexty = 0;
                    }
                    if (borderCollisionSouth) {
                        nexty = (mheight - sheight);
                    }

                    ModelPoint nextPoint = new ModelPoint(nextx, nexty, sprite.getAltitude());
                    collisions.add(new BorderCollision(engine, borderCollisionSides, sprite, spriteCollisionSides, oldLocation, nextPoint));
                }
            } else {
                boolean change = false;
                for (Tile tile : engine.findTiles(nextBound)) {
                    if (tile.getWalls() != Tile.NO_WALLS) {
                        if (tileCollision) {
                            int[] sides = getCollisionSides(nextBound, oldLocation, velocity, tile.getBounds());
                            if (sides[0] == 0 && sides[1] == 0) {
                                //could not find out colision sides with the given precision, increase precision
                                sides = getCollisionSides(nextBound, oldLocation, velocity, tile.getBounds(), 0);
                            }
                            if (sides[0] != 0 || sides[1] != 0) {
                                int tileSides = sides[1];

                                Tile _tile = tile;
                                ModelPoint loc0 = oldLocation;
                                ModelPoint loc1 = sprite.getLocation();
                                ModelPoint nextValidLocation = null;
                                if (loc0 != null) {
                                    double x0 = loc0.getX();
                                    double y0 = loc0.getY();
                                    double x1 = loc1.getX();
                                    double y1 = loc1.getY();
                                    double z1 = loc1.getZ();
                                    double x2 = x1;
                                    double y2 = y1;
                                    double z2 = z1;
                                    if (tileSides == Collision.SIDE_EAST) {
                                        x2 = _tile.getX() + _tile.getWidth();
                                    } else if (tileSides == Collision.SIDE_WEST) {
                                        x2 = _tile.getX() - sprite.getWidth();
                                    } else if (tileSides == Collision.SIDE_NORTH) {
                                        y2 = _tile.getY() - sprite.getHeight();
                                    } else if (tileSides == Collision.SIDE_SOUTH) {
                                        y2 = _tile.getY() + _tile.getHeight();
                                    } else if ((tileSides & (Collision.SIDE_NORTH | Collision.SIDE_WEST)) == (Collision.SIDE_NORTH | Collision.SIDE_WEST)) {
                                        if (Math.abs(x2 - x0) > Math.abs(y2 - y0)) {
                                        }
                                    } else {
                                        if (Math.abs(x2 - x0) > Math.abs(y2 - y0)) {
                                        }
                                    }
                                    nextValidLocation = new ModelPoint(x2, y2, z2);
                                    if (nextValidLocation.equals(loc1) || nextValidLocation.equals(loc0)) {
                                        nextValidLocation = null;
                                    } else {
                                        //check if new is valid
                                        ModelBox nextBound2 = new ModelBox(nextValidLocation, sprite.getSize());
                                        int[] sides2 = getCollisionSides(nextBound2, oldLocation, oldLocation == null ? null : ModelVector.newVector(oldLocation, nextValidLocation), tile.getBounds());
                                        if (sides2[0] != 0 || sides2[1] != 0) {
                                            //some collision
                                            nextValidLocation = null;
                                        }
                                    }
                                }
                                if (nextValidLocation == null) {
                                    nextValidLocation = oldLocation;
                                }
                                if (nextValidLocation == null) {
                                    nextValidLocation = sprite.getLocation();
                                }
                                collisions.add(new TileCollision(engine, tile, tileSides, sprite, sides[0], oldLocation, nextValidLocation));
                            }
                        }
                        change = true;
                        break;
                    }
                }
                if (!change) {
                    if (spriteCollision) {
                        for (Sprite other : engine.findSprites(nextBound)) {
                            if (!other.isDead() && other.getId() != sprite.getId() && other.getLocation().getIntZ() == sprite.getLocation().getIntZ()) {
                                Collection<Tile> collisionTiles = engine.findTiles(nextBound);
                                collisionTiles.retainAll(engine.findTiles(other.getBounds()));
                                int[] sides = getCollisionSides(nextBound, oldLocation, sprite.getVelocity(), other.getBounds());
                                if (sides[0] != 0 || sides[1] != 0) {
                                    getCollisionSides(nextBound, oldLocation, sprite.getVelocity(), other.getBounds());
                                    int spriteSides = sides[0];
                                    int otherSides = sides[1];

                                    ModelPoint loc0 = sprite.getLocation();
                                    double x = loc0.getX();
                                    double y = loc0.getY();
                                    double z = loc0.getZ();
                                    if ((otherSides & Collision.SIDE_EAST) != 0) {
                                        x = other.getLocation().getX() + other.getWidth();
                                    } else if ((otherSides & Collision.SIDE_WEST) != 0) {
                                        x = other.getLocation().getX() - sprite.getWidth();
                                    }
                                    if ((otherSides & Collision.SIDE_NORTH) != 0) {
                                        y = other.getLocation().getY() - sprite.getHeight();
                                    } else if ((otherSides & Collision.SIDE_SOUTH) != 0) {
                                        y = other.getLocation().getY() + other.getHeight();
                                    }
                                    ModelPoint nextValidLocation = new ModelPoint(x, y, z);

                                    if (nextValidLocation.equals(oldLocation) || nextValidLocation.equals(loc0)) {
                                        nextValidLocation = null;
                                    } else {
                                        //check if new location is valid
                                        ModelBox nextBound2 = new ModelBox(nextValidLocation, sprite.getSize());
                                        int[] sides2 = getCollisionSides(nextBound2, oldLocation, oldLocation == null ? null : ModelVector.newVector(oldLocation, nextValidLocation), other.getBounds());
                                        if (sides2[0] != 0 || sides2[1] != 0) {
                                            //some collision
                                            nextValidLocation = null;
                                        }
                                    }

                                    if (nextValidLocation == null) {
                                        nextValidLocation = oldLocation;
                                    }
                                    if (nextValidLocation == null) {
                                        nextValidLocation = sprite.getLocation();
                                    }

                                    collisions.add(new SpriteCollision(engine, other, otherSides, sprite, spriteSides, true, collisionTiles, oldLocation, nextValidLocation));
                                }
                                //cc.getCollisionManager().collideWithSprite(new SpriteCollision(sprite, sCollisionSides, cc, ccCollisionSides, false, collisionTiles, oldPosition));
                            }
                        }
                    }
                }
            }
        }
        return collisions;
    }

    private int[] getCollisionSides(ModelBox colliderBounds, ModelPoint colliderOldLocation, ModelVector colliderVelocity, ModelBox collidedBounds) {
        return getCollisionSides(colliderBounds, colliderOldLocation, colliderVelocity, collidedBounds, 0.1);
    }

    private int[] getCollisionSides(ModelBox colliderBounds, ModelPoint colliderOldLocation, ModelVector colliderVelocity, ModelBox collidedBounds, double intersectionThreshold) {
        if (colliderOldLocation == null) {
            return new int[]{
                    Collision.SIDE_NORTH | Collision.SIDE_EAST | Collision.SIDE_SOUTH | Collision.SIDE_WEST
                    , Collision.SIDE_NORTH | Collision.SIDE_EAST | Collision.SIDE_SOUTH | Collision.SIDE_WEST
            };
        } else {
            if (colliderOldLocation != null) {
                colliderVelocity = ModelVector.newCartesien(colliderBounds.getX() - colliderOldLocation.getX(), colliderBounds.getY() - colliderOldLocation.getY());
            }
            double[][] d = getMovingRectangleIntersection(new ModelBox(colliderOldLocation, colliderBounds.getSize()), colliderVelocity, collidedBounds, 0.1, intersectionThreshold);

//            DVector colliderVelocity2 = DVector.newCartesien(0, 0.40000000000000036);
//            DPoint colliderOldLocation2 = new DPoint(8, 9.8);
//            DRectangle collidedBounds2 = new DRectangle(8, 12, 1, 1);
//            double[][] d2 = getMovingRectangleIntersection(new DRectangle(colliderOldLocation2, colliderBounds.getSize()), colliderVelocity2, collidedBounds2, 0.1, 0.1);

            int[] sides = new int[2];
            for (int i = 0; i < d.length; i++) {
                boolean sideCollides = false;
                for (int j = 0; j < d[i].length; j++) {
                    if (d[i][j] > 0) {
                        sideCollides = true;
                        switch (j) {
                            case 0: {
                                sides[1] |= Collision.SIDE_NORTH;
                                break;
                            }
                            case 1: {
                                sides[1] |= Collision.SIDE_EAST;
                                break;
                            }
                            case 2: {
                                sides[1] |= Collision.SIDE_SOUTH;
                                break;
                            }
                            case 3: {
                                sides[1] |= Collision.SIDE_WEST;
                                break;
                            }
                        }
                    }
                }

                if (sideCollides) {
                    switch (i) {
                        case 0: {
                            sides[0] |= Collision.SIDE_NORTH;
                            break;
                        }
                        case 1: {
                            sides[0] |= Collision.SIDE_EAST;
                            break;
                        }
                        case 2: {
                            sides[0] |= Collision.SIDE_SOUTH;
                            break;
                        }
                        case 3: {
                            sides[0] |= Collision.SIDE_WEST;
                            break;
                        }
                    }
                }
            }
            return sides;
        }
//        if (colliderOldLocation == null) {
//            //add newly
//            return new int[]{Collision.SIDE_NORTH, Collision.SIDE_SOUTH};
//        }
////        DRectangle ccbounds = sprite.getBounds();
////        DRectangle sbounds = collider.getBounds();
//        double ax0 = colliderOldLocation.getX();
//        double ay0 = colliderOldLocation.getY();
//
//        double ax = colliderBounds.getMinX();
//        double ay = colliderBounds.getMinY();
//        double aw = colliderBounds.getWidth();
//        double ah = colliderBounds.getHeight();
//
////        double bx = collidedBounds.getMinX();
////        double by = collidedBounds.getMinY();
////        double bw = collidedBounds.getWidth();
////        double bh = collidedBounds.getHeight();
//
//        DRectangle oldColliderBounds = new DRectangle(ax0, ay0, aw, ah);
//        return intersectionSides(oldColliderBounds, ax - ax0, ay - ay0, collidedBounds);
////        int collidedSides = firstCollidedBySecond(collidedBounds, colliderBounds);
////        int colliderSides = firstCollidedBySecond(colliderBounds, collidedBounds);
////        return new int[]{colliderSides, collidedSides};
////        if (ax0 != ax) {
////            // x movement
////            if (ay0 == ay) {
////                if (ax > ax0) {
////                    return new int[]{Collision.SIDE_EAST, Collision.SIDE_WEST};
////                } else if (ax < ax0) {
////                    return new int[]{Collision.SIDE_WEST, Collision.SIDE_EAST};
////                }
////            }
////        } else {
////            //y movement
////            if (ay > ay0) {
////                return new int[]{Collision.SIDE_SOUTH, Collision.SIDE_NORTH};
////            } else if (ay < ay0) {
////                return new int[]{Collision.SIDE_NORTH, Collision.SIDE_SOUTH};
////            }
////        }
////        int colliderSides = 0;
////        int collidedSides = 0;
////        switch (resolveJump(ay0, ay, by, by + bh)) {
//////            case REMAIN_INSIDE:
////            case FORWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_SOUTH;
////                collidedSides |= Collision.SIDE_NORTH;
////                break;
////            }
////            case BACKWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_NORTH;
////                collidedSides |= Collision.SIDE_SOUTH;
////                break;
////            }
////        }
////        switch (resolveJump(ay0 + ah, ay + ah, by, by + bh)) {
//////            case REMAIN_INSIDE:
////            case FORWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_SOUTH;
////                collidedSides |= Collision.SIDE_NORTH;
////                break;
////            }
////            case BACKWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_NORTH;
////                collidedSides |= Collision.SIDE_SOUTH;
////                break;
////            }
////        }
////
////        switch (resolveJump(ax0, ax, bx, bx + bw)) {
//////            case REMAIN_INSIDE:
////            case FORWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_EAST;
////                collidedSides |= Collision.SIDE_WEST;
////                break;
////            }
////            case BACKWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_WEST;
////                collidedSides |= Collision.SIDE_EAST;
////                break;
////            }
////        }
////
////        switch (resolveJump(ax0 + aw, ax + aw, bx, bx + bw)) {
//////            case REMAIN_INSIDE:
////            case FORWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_EAST;
////                collidedSides |= Collision.SIDE_WEST;
////                break;
////            }
////            case BACKWARD_INSIDE: {
////                colliderSides |= Collision.SIDE_WEST;
////                collidedSides |= Collision.SIDE_EAST;
////                break;
////            }
////        }
////
////        if (collidedSides == 0) {
////            System.out.println("{{{{???? collider : " + colliderOldLocation + " ==> " + colliderBounds.getLocation() + " collides with " + collidedBounds);
////        }
////
////        System.out.println("colliderSides : " + Collision.getSideString(colliderSides));
////        return new int[]{colliderSides, collidedSides};
    }

    private int[] intersectionSides(ModelBox r1, double dx, double dy, ModelBox r2) {
        int collided = intersectionSides(r1, new ModelPoint(r1.getLocation().getX() + dx, r1.getLocation().getY() + dy, r1.getLocation().getZ()), r2);
        int collider = intersectionSides(r2, new ModelPoint(r2.getLocation().getX() - dx, r2.getLocation().getY() - dy, r2.getLocation().getZ()), r1);

        if ((collider & Collision.SIDE_NORTH) != 0) {
            collided |= Collision.SIDE_SOUTH;
        }
        if ((collider & Collision.SIDE_SOUTH) != 0) {
            collided |= Collision.SIDE_NORTH;
        }
        if ((collider & Collision.SIDE_EAST) != 0) {
            collided |= Collision.SIDE_WEST;
        }
        if ((collider & Collision.SIDE_WEST) != 0) {
            collided |= Collision.SIDE_EAST;
        }

        if ((collided & Collision.SIDE_NORTH) != 0) {
            collider |= Collision.SIDE_SOUTH;
        }
        if ((collided & Collision.SIDE_SOUTH) != 0) {
            collider |= Collision.SIDE_NORTH;
        }
        if ((collided & Collision.SIDE_EAST) != 0) {
            collider |= Collision.SIDE_WEST;
        }
        if ((collided & Collision.SIDE_WEST) != 0) {
            collider |= Collision.SIDE_EAST;
        }
        return new int[]{collider, collided};
    }

    private int intersectionSides(ModelBox r1, ModelPoint newLocation, ModelBox r2) {
        int collided = 0;

        double rx1 = r1.getMinX();
        double ry1 = r1.getMinY();
        double rx2 = r1.getMaxX();
        double ry2 = r1.getMaxY();

        double zx1 = newLocation.getX();
        double zy1 = newLocation.getY();
        double zx2 = rx1 + r1.getWidth();
        double zy2 = ry1 + r1.getHeight();

        int s2 = intersectionSides(new ModelSegment(rx1, ry1, zx1, zy1), r2);
        if (s2 != 0) {
            collided |= s2;
        }

        s2 = intersectionSides(new ModelSegment(rx1, ry2, zx1, zy2), r2);
        if (s2 != 0) {
            collided |= s2;
        }

        s2 = intersectionSides(new ModelSegment(rx2, ry1, zx2, zy1), r2);
        if (s2 != 0) {
            collided |= s2;
        }

        s2 = intersectionSides(new ModelSegment(rx2, ry2, zx2, zy2), r2);
        if (s2 != 0) {
            collided |= s2;
        }

        return collided;
    }

    private int intersectionSides(ModelSegment s, ModelBox r) {
        double sx1 = s.getA().getX();
        double sy1 = s.getA().getY();
        double sx2 = s.getB().getX();
        double sy2 = s.getB().getY();
        double rx1 = r.getMinX();
        double ry1 = r.getMinY();
        double rx2 = r.getMaxX();
        double ry2 = r.getMaxY();
        int x = 0;
        if (sy1 != sy2 && GeometryUtils.isLineIntersectingLine(sx1, sy1, sx2, sy2, rx1, ry1, rx2, ry1)) {
            x += Collision.SIDE_NORTH;
        }
        if (sx1 != sx2 && GeometryUtils.isLineIntersectingLine(sx1, sy1, sx2, sy2, rx1, ry1, rx1, ry2)) {
            x += Collision.SIDE_WEST;
        }
        if (sy1 != sy2 && GeometryUtils.isLineIntersectingLine(sx1, sy1, sx2, sy2, rx1, ry2, rx2, ry2)) {
            x += Collision.SIDE_SOUTH;
        }
        if (sx1 != sx2 && GeometryUtils.isLineIntersectingLine(sx1, sy1, sx2, sy2, rx2, ry1, rx2, ry2)) {
            x += Collision.SIDE_EAST;
        }
        return x;
    }

    private POS_TYPE pos(double v1, double min, double max) {
        if (v1 < min) {
            return POS_TYPE.BEFORE;
        } else if (v1 == min) {
            return POS_TYPE.START;
        } else if (v1 < max) {
            return POS_TYPE.INTO;
        } else if (v1 == max) {
            return POS_TYPE.END;
        } else {
            return POS_TYPE.AFTER;
        }
    }

    private JUMP_TYPE resolveJump(double v1, double v2, double min, double max) {
        POS_TYPE p1 = pos(v1, min, max);
        POS_TYPE p2 = pos(v2, min, max);
        switch (p1) {
            case BEFORE: {
                switch (p2) {
                    case BEFORE: {
                        return JUMP_TYPE.REMAIN_OUTSIDE;
                    }
                    case START: {
                        return JUMP_TYPE.FORWARD_INSIDE;
                    }
                    case INTO: {
                        return JUMP_TYPE.FORWARD_INSIDE;
                    }
                    case END: {
                        return JUMP_TYPE.FORWARD_INSIDE;
                    }
                    case AFTER: {
                        return JUMP_TYPE.FORWARD_INSIDE;
                    }
                }
                break;
            }
            case START: {
                switch (p2) {
                    case BEFORE: {
                        return JUMP_TYPE.REMAIN_OUTSIDE;
                    }
                    case START: {
                        return JUMP_TYPE.REMAIN_OUTSIDE;
                    }
                    case INTO: {
                        return JUMP_TYPE.FORWARD_INSIDE;
                    }
                    case END: {
                        return JUMP_TYPE.FORWARD_INSIDE;
                    }
                    case AFTER: {
                        return JUMP_TYPE.FORWARD_OUTSIDE;
                    }
                }
                break;
            }
            case INTO: {
                switch (p2) {
                    case BEFORE: {
                        return JUMP_TYPE.BACKWARD_OUTSIDE;
                    }
                    case START: {
                        return JUMP_TYPE.REMAIN_INSIDE;
                    }
                    case INTO: {
                        return JUMP_TYPE.REMAIN_INSIDE;
                    }
                    case END: {
                        return JUMP_TYPE.REMAIN_INSIDE;
                    }
                    case AFTER: {
                        return JUMP_TYPE.FORWARD_OUTSIDE;
                    }
                }
                break;
            }
            case END: {
                switch (p2) {
                    case BEFORE: {
                        return JUMP_TYPE.BACKWARD_OUTSIDE;
                    }
                    case START: {
                        return JUMP_TYPE.REMAIN_INSIDE;
                    }
                    case INTO: {
                        return JUMP_TYPE.REMAIN_INSIDE;
                    }
                    case END: {
                        return JUMP_TYPE.REMAIN_INSIDE;
                    }
                    case AFTER: {
                        return JUMP_TYPE.REMAIN_OUTSIDE;
                    }
                }
                break;
            }
            case AFTER: {
                switch (p2) {
                    case BEFORE: {
                        return JUMP_TYPE.BACKWARD_OUTSIDE;
                    }
                    case START: {
                        return JUMP_TYPE.BACKWARD_INSIDE;
                    }
                    case INTO: {
                        return JUMP_TYPE.BACKWARD_INSIDE;
                    }
                    case END: {
                        return JUMP_TYPE.REMAIN_OUTSIDE;
                    }
                    case AFTER: {
                        return JUMP_TYPE.REMAIN_OUTSIDE;
                    }
                }
                break;
            }
        }
        throw new RuntimeException("Impossible");
    }

    private static enum POS_TYPE {

        BEFORE,
        START,
        INTO,
        END,
        AFTER
    }

    private static enum JUMP_TYPE {

        REMAIN_INSIDE,
        REMAIN_OUTSIDE,
        FORWARD_INSIDE,
        FORWARD_OUTSIDE,
        BACKWARD_INSIDE,
        BACKWARD_OUTSIDE
    }

    private int firstCollidedBySecond(ModelBox a, ModelBox b) {
        double aw = a.getWidth();
        double ah = a.getHeight();
        double aa = a.getAltitude();
        double ax = a.getX();
        double ay = a.getY();
        double az = a.getZ();
        double dx = aw / 100;
        double dy = ah / 100;
        double dz = az / 100;
        double dx2 = dx + dx;
        double dy2 = dy + dy;
        double dz2 = dz + dz;

        ModelBox n = new ModelBox(ax + dx, ay, az, aw - dx2, dy, aa);
        ModelBox s = new ModelBox(ax + dx, ay + ah - dy, az, aw - dx2, dy, aa);
        ModelBox e = new ModelBox(ax + aw - dx2, ay + dy, dx, az, dy + ah - dy2, aa);
        ModelBox w = new ModelBox(ax, ay + dy, az, dx, dy + ah - dy2, aa);

        ModelBox nw = new ModelBox(ax, ay, az, dx, dy, aa);
        ModelBox ne = new ModelBox(ax + aw - dx, az, ay, dx, dy, aa);

        ModelBox sw = new ModelBox(ax, ay + ah - dy, az, dx, dy, aa);
        ModelBox se = new ModelBox(ax + aw - dx, ay + ah - dy, az, dx, dy, aa);
        int c = 0;
        boolean i_e = e.intersects(b);
        boolean i_n = n.intersects(b);
        boolean i_w = w.intersects(b);
        boolean i_s = s.intersects(b);

        boolean i_nw = nw.intersects(b);
        boolean i_ne = ne.intersects(b);
        boolean i_sw = sw.intersects(b);
        boolean i_se = se.intersects(b);

        if (i_e) {
            c |= Collision.SIDE_EAST;
            i_ne = false;
            i_se = false;
        }

        if (i_w) {
            c |= Collision.SIDE_WEST;
            i_nw = false;
            i_sw = false;
        }

        if (i_n) {
            c |= Collision.SIDE_NORTH;
            i_nw = false;
            i_ne = false;
        }

        if (i_s) {
            c |= Collision.SIDE_SOUTH;
            i_sw = false;
            i_se = false;
        }

        return c;
    }


//    public static void main(String[] args) {
//        DVector colliderVelocity2 = DVector.newCartesien(0, 0.40000000000000036);
//        DPoint colliderOldLocation2 = new DPoint(8, 9.8);
//        DRectangle collidedBounds2 = new DRectangle(8, 12, 1, 1);
//        double[][] d2 = getMovingRectangleIntersection(new DRectangle(colliderOldLocation2, collidedBounds2.getSize()), colliderVelocity2, collidedBounds2, 0.1, 0.1);
//        System.out.println(Arrays.deepToString(d2));
//        //System.exit(0);
//        SceneEngine sceneEngine = new DefaultSceneEngine();
//        SceneModel model = new TileBasedSceneModel(4, 4);
//        sceneEngine.setModel(model);
//
//        DefaultSprite obstacle = new DefaultSprite();
//        obstacle.setName("obstacle");
//        obstacle.setSize(new DDimension(2, 1));
//        obstacle.setLocation(new DPoint(1, 2));
//
//        DefaultSprite sprite = new DefaultSprite();
//        sprite.setName("sprite");
//        sprite.setSize(new DDimension(1, 1));
//        sprite.setLocation(new DPoint(1, 0));
//
//        DPoint next = new DPoint(1.5, 1.5);
//
//        DRectangle r1 = obstacle.getBounds();
//        DRectangle r2 = new DRectangle(new DPoint(1, 1.5), sprite.getSize());
//        DVector move = DVector.newCartesien(next.getX() - sprite.getX(), next.getY() - sprite.getY());
//        System.out.println("move " + move);
//        double[][] gg = getMovingRectangleIntersection(sprite.getBounds(), move, obstacle.getBounds(), 0.1, 0.1);
//        System.out.println(Arrays.deepToString(gg));
//
//        System.out.println(ttt(r1, r2));
//        System.out.println(ttt(r2, r1));
//
//        model.addSprite(obstacle);
//        model.addSprite(sprite);
//
//        DefaultSceneCollisionManager c = new DefaultSceneCollisionManager();
////        DPoint newPosition = new DPoint(2,1);
//        Collision[] collisions = c.detectCollisions(sceneEngine, sprite, sprite.getLocation(), next, false, false, true);
//        for (Collision collision : collisions) {
//            System.out.println(collision);
//        }
//
//    }
}
