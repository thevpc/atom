/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.business.tasks;


import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.maintasks.FindPathToPointSpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.MotionSpriteMainTask;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class BuildMainTask implements MotionSpriteMainTask {

    private ModelPoint location;
    private int buildTime = 100;
    private boolean building;
    private Sprite sprite;
    private FindPathToPointSpriteMainTask moveTask;

    public BuildMainTask() {
    }

    public ModelPoint[] getMovePath() {
        return !building? moveTask.getMovePath():null;
    }

    public boolean isMoving() {
        return !building;
    }
    
    

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        moveTask = new FindPathToPointSpriteMainTask(null, 10,true);
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
                    sprite.setMainTask(null);
                }
            }
        }
        return true;
    }

}
