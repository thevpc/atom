/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

public class TimerSpriteMainTask implements SpriteMainTask {

    private int time;
    private SpriteMainTask finalTask;

    public TimerSpriteMainTask(int time, SpriteMainTask finalTask) {
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
            sceneEngine.setSpriteMainTask(sprite,finalTask);
            return true;
        }
    }
}
