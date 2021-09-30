package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.util.AtomUtils;
import net.thevpc.gaming.atom.util.CollectionHashMap;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.sqrt;

public class DiscreteSceneCollisionManagerHelper {
    private int maxIterations;
    private double modelPrecision = 1E-3;
    private ModelDimension modelSize;

    public DiscreteSceneCollisionManagerHelper(int maxIterations,double modelPrecision,ModelDimension modelSize) {
        this.maxIterations = maxIterations;
        this.modelPrecision = modelPrecision;
        this.modelSize = modelSize;
    }

    public DiscreteSceneCollisionManager.OldCollData resolveOldCollisions(List<Obstacle> motionObstacles, List<Obstacle> allObstacles) {
        DiscreteSceneCollisionManager.OldCollData d = new DiscreteSceneCollisionManager.OldCollData();
        Set<Obstacle> ssm = new HashSet<>();
        Set<Obstacle> ssa = new HashSet<>();
        for (Obstacle o1 : motionObstacles) {
            for (Obstacle o2 : allObstacles) {
                if (o1 != o2) {
                    if (o1.oldBox.intersects(o2.oldBox)) {
                        ssm.add(o1);
                        ssa.add(o1);
                        ssa.add(o2);
                        CollisionInfo c = new CollisionInfo();
                        c.setCollisionInitiator(o1);
                        c.setCollisionObstacle(o2);
                        d.collisions.add(c);
                    }
                }
            }
        }
        d.subSetMotionObstacles.addAll(ssm);
        d.subSetAllObstacles.addAll(ssa);
        return d;
    }

    public List<CollisionInfo> evalCollisions(SceneEngine engine,
                                              List<Obstacle> motionObstacles,
                                              List<Obstacle> motionlessObstacles) {
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
                            CollisionSides sides = minkowskiSum(o1, o2);
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
                                //reevaluate collision tasks in the other side to help update next position or the second sprite
                                if (!o1.motionless && !o2.motionless) {
                                    eval(o2, o1);
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
            Obstacle initiator = allObstacles.get(integer);
            if (initiator.collides) {
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

                for (ObstacleCollInfo other : initiator.collidesWith) {
                    CollisionInfo i = new CollisionInfo();
                    i.setCollisionInitiator(initiator.element);
                    i.setCollisionObstacle(other.other.element);
                    i.setInitiatorCollisionSides(other.meCollisionSides);
                    i.setInitiatorPreferredPosition(initiator.nextBox.getLocation());

                    i.setOtherCollisionSides(other.otherCollisionSides);
                    i.setOtherLastPosition(other.other.oldBox.getLocation());
                    i.setOtherPreferredPosition(other.other.nextBox.getLocation());
                    i.setOtherNextPosition(other.other.nextBox.getLocation());

                    if (i.getCollisionInitiator() instanceof Sprite) {
                        Sprite s = (Sprite) i.getCollisionInitiator();
                        s.setCollisionSides(s.getCollisionSides().append(i.getInitiatorCollisionSides()));
                    }
                    if (i.getCollisionObstacle() instanceof Sprite) {
                        Sprite s = (Sprite) i.getCollisionObstacle();
                        s.setCollisionSides(s.getCollisionSides().append(i.getOtherCollisionSides()));
                    }
                    if (i.getCollisionInitiator() instanceof Sprite) {
                        Sprite s = (Sprite) i.getCollisionInitiator();
                        double w = s.getWidth();
                        double h = s.getHeight();
                        double a = s.getAltitude();
                        ModelDimension size = modelSize;
                        i.setInitiatorPreferredPosition(validatePos(i.getInitiatorPreferredPosition(),w,h,a,size));
                        if(i.getCollisionObstacle() instanceof Sprite){

                        }
                        i.setInitiatorPreferredPosition(validatePos(i.getInitiatorPreferredPosition(),w,h,a,size));
                        ModelBox obox = other.other.nextBox;
                        if ((i.getInitiatorCollisionSides().isNorth()) && i.getInitiatorPreferredPosition().getY() < obox.getMaxY()) {
//                            i.preferredPosition = i.preferredPosition.copyAndSetY(obox.getMaxY());
                        }
                        if ((i.getInitiatorCollisionSides().isSouth()) && i.getInitiatorPreferredPosition().getY() + h > obox.getMinY()) {
//                            i.preferredPosition = i.preferredPosition.copyAndSetY(obox.getMinY() - h);
                        }
                        if ((i.getInitiatorCollisionSides().isWest()) && i.getInitiatorPreferredPosition().getX() < obox.getMaxX()) {
//                            i.preferredPosition = i.preferredPosition.copyAndSetX(obox.getMaxX());
                        }
                        if ((i.getInitiatorCollisionSides().isEast()) && i.getInitiatorPreferredPosition().getX() + w > obox.getMinX()) {
//                            i.preferredPosition = i.preferredPosition.copyAndSetX(obox.getMinX() - w);
                        }
                    }
                    i.setInitiatorLastPosition( initiator.oldBox.getLocation());
                    i.setInitiatorNextPosition( i.getInitiatorPreferredPosition());//obstacle.originalNextBox.getLocation();
                    i.setCollisionTiles( engine==null?new ArrayList<>() : engine.findTiles(initiator.nextBox));
                    if(engine!=null) {
                        i.getCollisionTiles().retainAll(engine.findTiles(other.other.nextBox));
                    }
                    collisions.add(i);
                }
            }
        }
        return collisions;
    }

    private ModelPoint validatePos(ModelPoint p,double w,double h,double a,ModelDimension size){
        if (p.getX() > size.getWidth() - w) {
            return ( new ModelPoint(
                    size.getWidth() - w,
                    p.getY(),
                    p.getZ()
            ));
        } else if (p.getX() < 0) {
            return (new ModelPoint(
                    0,
                    p.getY(),
                    p.getZ()
            ));
        }
        if (p.getY() > size.getHeight() - h) {
            return ( new ModelPoint(
                    p.getX(),
                    size.getHeight() - h,
                    p.getZ()
            ));
        } else if (p.getY() < 0) {
            return ( new ModelPoint(
                    p.getX(),
                    0,
                    p.getZ()
            ));
        }
        if (p.getZ() > size.getAltitude() - a) {
            return (new ModelPoint(
                    p.getX(),
                    p.getY(),
                    size.getAltitude() - a
            ));
        } else if (p.getY() < 0) {
            return (new ModelPoint(
                    p.getX(),
                    p.getY(),
                    0
            ));
        }
        return p;
    }

    private CollisionSides minkowskiSum(Obstacle o1, Obstacle o2) {
        switch (o2.type){
            case BORDER_WEST:{
                return CollisionSides.WEST;
            }
            case BORDER_EAST:{
                return CollisionSides.EAST;
            }
            case BORDER_NORTH:{
                return CollisionSides.NORTH;
            }
            case BORDER_SOUTH:{
                return CollisionSides.SOUTH;
            }
        }
        switch (o1.type){
            case BORDER_WEST:{
                return CollisionSides.EAST;
            }
            case BORDER_EAST:{
                return CollisionSides.WEST;
            }
            case BORDER_NORTH:{
                return CollisionSides.SOUTH;
            }
            case BORDER_SOUTH:{
                return CollisionSides.NORTH;
            }
        }

        double w = 0.5 * (o1.getBoundingBox().getWidth() + o2.getBoundingBox().getWidth());
        double h = 0.5 * (o1.getBoundingBox().getHeight() + o2.getBoundingBox().getHeight());

        double dx = o1.getBoundingBox().getCenterX() - o2.getBoundingBox().getCenterX();
        double dy = o1.getBoundingBox().getCenterY() - o2.getBoundingBox().getCenterY();

        if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
            /* collision tasks! */
            double wy = w * dy;
            double hx = h * dx;

            if (wy > hx) {
                if (wy > -hx) {
                    return CollisionSides.NORTH;//CollisionSides.SIDE_NORTH;
                } else {
                    return CollisionSides.EAST;//CollisionSides.SIDE_WEST;
                }
            } else if (wy > -hx) {
                return CollisionSides.WEST;
            } else {
                return CollisionSides.SOUTH;
            }

        }
        if(o1.isMovingEast()){
            return CollisionSides.WEST;
        }
        if(o1.isMovingWest()){
            return CollisionSides.EAST;
        }
        if(o1.isMovingNorth()){
            return CollisionSides.SOUTH;
        }
        if(o1.isMovingSouth()){
            return CollisionSides.NORTH;
        }

        if(o2.isMovingEast()){
            return CollisionSides.EAST;
        }
        if(o2.isMovingWest()){
            return CollisionSides.WEST;
        }
        if(o2.isMovingNorth()){
            return CollisionSides.NORTH;
        }
        if(o2.isMovingSouth()){
            return CollisionSides.SOUTH;
        }
        return CollisionSides.NONE;
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
                boolean someCollision = o1.nextBox.intersects(o2.nextBox);
                //should do extra test shape based
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
        o1.collides = true;
        return true;
    }

}
