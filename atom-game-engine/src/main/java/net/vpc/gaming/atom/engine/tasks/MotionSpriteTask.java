/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.tasks;

import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.ModelPoint;

/**
 * Base Move Task
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface MotionSpriteTask extends SpriteTask {

    /**
     * return the next points defining the path that would be followed by the sprite
     * May return null if there is no guess of next path
     *
     * @return expected path for sprite travel (meaningful for move tasks mainly)
     */
    public ModelPoint[] getMovePath();

    /**
     * @return true if the sprite is actually moving
     */
    public boolean isMoving();
}
