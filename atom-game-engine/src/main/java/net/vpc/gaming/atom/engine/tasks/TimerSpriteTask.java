/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.tasks;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.Sprite;

public class TimerSpriteTask implements SpriteTask {

    private int time;
    private SpriteTask finalTask;

    public TimerSpriteTask(int time, SpriteTask finalTask) {
        this.time = time;
        this.finalTask = finalTask;
    }

    @Override
    public boolean nextFrame(SceneEngine sceneEngine, Sprite sprite) {
        if (time > 0) {
            time--;
            return true;
        } else {
            if(finalTask==null){
                return false;
            }
            sceneEngine.setSpriteTask(sprite,finalTask);
            return true;
        }
    }
}
