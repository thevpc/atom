/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.business.tasks;


import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.tasks.FindPathToPointSpriteTask;
import net.vpc.gaming.atom.engine.tasks.MotionSpriteTask;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class BuildTask implements MotionSpriteTask {

    private ModelPoint location;
    private int buildTime = 100;
    private boolean building;
    private Sprite sprite;
    private FindPathToPointSpriteTask moveTask;

    public BuildTask() {
    }

    public ModelPoint[] getMovePath() {
        return !building? moveTask.getMovePath():null;
    }

    public boolean isMoving() {
        return !building;
    }
    
    

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        moveTask = new FindPathToPointSpriteTask(null, 10,true);
//        moveTask.setSprite(sprite);
    }

    public void setLocation(ModelPoint position) {
        this.location = position;
    }

    public boolean canBuild() {
        return location != null;
    }

    public void progressBuilding() {
        buildTime--;
    }

    public boolean buildFinished() {
        return buildTime <= 0;
    }

    public ModelPoint getLocation() {
        return location;
    }

    public abstract Sprite createBuilding();

    public boolean nextFrame(SceneEngine scene,Sprite sprite) {
        if (canBuild()) {
            moveTask.setTarget(location);
            moveTask.nextFrame(scene,sprite);
            if (sprite.getLocation().distance(location) <= moveTask.getMinDistance()) {
                moveTask.setTarget(null);
                progressBuilding();
                if (buildFinished()) {
                    Sprite a = createBuilding();
                    a.setPlayerId(sprite.getPlayerId());
                    a.setLocation(getLocation());
                    scene.getModel().addSprite(a);
                    sprite.setTask(null);
                }
            }
        }
        return true;
    }

}
