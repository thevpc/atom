package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.model.ModelBox;
import net.thevpc.gaming.atom.util.SegmentNavigator;

import java.util.ArrayList;
import java.util.List;

public class Obstacle {

    int id;
    String name;
    ObstacleType type;
    Object element;
    ModelBox oldBox;
    ModelBox originalNextBox;
    ModelBox nextBox;
    SegmentNavigator moveNavigator;
    float moveRatio;
    boolean collides;
    boolean crossable;
    List<ObstacleCollInfo> collidesWith = new ArrayList<>();
    boolean adjustablePositioning = true;
    boolean motionless = false;

    public Obstacle(ObstacleType type, String name, Object element, ModelBox oldBox, ModelBox nextBox, boolean adjustablePositioning, boolean crossable, boolean motionless) {
        this.type = type;
        this.name = name;
        this.element = element;
        this.oldBox = oldBox;
        this.nextBox = nextBox == null ? oldBox : nextBox;
//            if(type==ObstacleType.SPRITE && oldBox.equals(nextBox)){
//                System.out.println("Why");
//            }
        this.adjustablePositioning = adjustablePositioning;
        this.crossable = crossable;
        this.motionless = motionless;
        this.originalNextBox = this.nextBox;
        moveNavigator = new SegmentNavigator(oldBox.getLocation(), originalNextBox.getLocation());
        moveRatio = 1f;
    }

    public boolean isMoving() {
        return !oldBox.equals(nextBox);
    }

    public boolean isCollideWith(Obstacle other) {
        for (ObstacleCollInfo obstacleCollInfo : collidesWith) {
            if (obstacleCollInfo.other.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public ModelBox getBoundingBox() {
        return oldBox.unionBox(nextBox);
    }

    public ModelBox getOriginalBoundingBox() {
        return oldBox.unionBox(originalNextBox);
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", element=" + element +
                ", oldBox=" + oldBox +
                ", originalNextBox=" + originalNextBox +
                ", nextBox=" + nextBox +
                ", collides=" + collides +
                ", crossable=" + crossable +
                ", collidesWith=" + collidesWith +
                ", adjustablePositioning=" + adjustablePositioning +
                ", motionless=" + motionless +
                '}';
    }

    public boolean isMovingEast() {
        if (motionless) {
            return false;
        }
        return nextBox.getX() - oldBox.getX() > 0;
    }

    public boolean isMovingWest() {
        if (motionless) {
            return false;
        }
        return nextBox.getX() - oldBox.getX() < 0;
    }

    public boolean isMovingSouth() {
        if (motionless) {
            return false;
        }
        return nextBox.getY() - oldBox.getY() > 0;
    }

    public boolean isMovingNorth() {
        if (motionless) {
            return false;
        }
        return nextBox.getY() - oldBox.getY() < 0;
    }
}
