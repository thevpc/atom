/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.pathfinder.AStarTilePathFinder;
import net.thevpc.gaming.atom.engine.pathfinder.TilePathFinder;
import net.thevpc.gaming.atom.engine.pathfinder.TransitionStrategy;
import net.thevpc.gaming.atom.engine.pathfinder.TransitionStrategy8;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.util.GeometryUtils;

/**
 * Use Pathfinder algorithm to move Sprite until reaching a minimum distance toward a given Point
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class FindPathToPointSpriteMainTask implements MotionSpriteMainTask {

    private ModelPoint target;
    private boolean center;
    private TilePathFinder pathFinder = new AStarTilePathFinder();
    private int maxMovesSight = 1000;
    private int pathCalculationCacheTime = 1000;
    private int currentPathCalculationCacheIndex = -1;
    private ModelPoint[] path;
    private double minDistance;
    private int transitionsCount = 8;

    public FindPathToPointSpriteMainTask(ModelPoint point) {
        this(point, 0, true);
    }

    public FindPathToPointSpriteMainTask(ModelPoint point, double minDistance, boolean center) {
        //stick target to grid, and point the center of the cell (thas (0.5,0.5) of the cell)
        if (point != null) {
            //REMOVE ME
            point = new ModelPoint(((int) point.getX()) + 0.5, ((int) point.getY()) + 0.5, ((int) point.getZ()) + 0.5);
        }
        this.minDistance = minDistance;
        this.target = point;
        this.center = center;
    }

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        boolean terminated = false;
        if (target != null) {
            ModelPoint enModelPoint;
            if (!center) {
                enModelPoint = target;
            } else {
                enModelPoint = new ModelPoint(target.getX() - sprite.getWidth() / 2, target.getY() - sprite.getHeight() / 2, target.getZ() - sprite.getAltitude() / 2);
            }
            final ModelPoint startPoint = sprite.getLocation();
            if (!startPoint.equals(enModelPoint)) {
                if (currentPathCalculationCacheIndex <= 0) {
                    currentPathCalculationCacheIndex = pathCalculationCacheTime;
//                    TransitionStrategy transitionStrategy = new TransitionStrategyN(scene, sprite, transitionsCount, maxMovesSight);
                    TransitionStrategy transitionStrategy = new TransitionStrategy8(scene, sprite, maxMovesSight);
                    path = pathFinder.findPath(startPoint, enModelPoint, transitionStrategy);
                }
                boolean consumePath = false;
                if (path.length > 1) {
                    ModelPoint stepPoint = path[1];
                    if (minDistance <= 0 || Double.isNaN(minDistance) || stepPoint.distance(startPoint) < minDistance) {
                        if (stepPoint.equals(enModelPoint)) {
//                            sprite.setDirection(stepPoint.getAngleTo(enModelPoint));
                            consumePath = true;
                        } else {
                            double oldDirection = sprite.getDirection();
                            double nextDirection = stepPoint.getAngleTo(enModelPoint);
                            double as = sprite.getAngularSpeed();
                            if (as <= 0 || as >= Math.PI) {
                                sprite.setDirection(nextDirection);
                                consumePath = true;
                            } else {
                                double ad = GeometryUtils.getAngleDiff(oldDirection, nextDirection);
                                if (ad >= 0) {
                                    if (ad < as) {
                                        sprite.setDirection(nextDirection);
                                        consumePath = true;
                                    } else {
                                        sprite.setDirection(oldDirection + as);
                                    }
                                } else {
                                    if (-ad < as) {
                                        sprite.setDirection(nextDirection);
                                        consumePath = true;
                                    } else {
                                        sprite.setDirection(oldDirection - as);
                                    }
                                }
                            }
                        }
                    }
                }
                if (consumePath) {
                    ModelPoint stepPoint = path[1];
                    ModelPoint stepPoint2 = GeometryUtils.nextLinePoint(sprite.getLocation(), stepPoint, sprite.getSpeed());
                    sprite.setLocation(stepPoint2);
                    if (stepPoint2.equals(stepPoint)) {
                        ModelPoint[] c2 = new ModelPoint[path.length - 1];
                        System.arraycopy(path, 1, c2, 0, c2.length);
                        path = c2;
                        currentPathCalculationCacheIndex--;
                    }
                }
            } else {
                terminated = true;
            }
        } else {
            terminated = true;
        }
        if (terminated) {
        }
        return !terminated;
    }

    public ModelPoint getTarget() {
        return target;
    }

    public void setTarget(ModelPoint target) {
        ModelPoint old = this.target;
        this.target = target;
        if (!(old == target || (old != null && old.equals(target)))) {
            currentPathCalculationCacheIndex = -1;
        }
    }

    public ModelPoint[] getMovePath() {
        return path;
    }

    @Override
    public boolean isMoving() {
        return true;
    }

    @Override
    public String toString() {
        return "MoveTo";
//        return center? ("GOTO_CENTER(" + target + ")"):("GOTO(" + target + ")");
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        double old = this.minDistance;
        this.minDistance = minDistance;
        if (old != minDistance) {
            currentPathCalculationCacheIndex = -1;
        }
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        boolean old = this.center;
        this.center = center;
        if (old != center) {
            currentPathCalculationCacheIndex = -1;
        }
    }
}
