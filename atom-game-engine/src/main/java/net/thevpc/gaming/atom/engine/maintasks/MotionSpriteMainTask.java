/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.ModelPoint;

/**
 * Base Move Task
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface MotionSpriteMainTask extends SpriteMainTask {

    /**
     * return the next points defining the path that would be followed by the
     * sprite May return null if there is no guess of next path
     *
     * @return expected path for sprite travel (meaningful for move maintasks
     * mainly)
     */
    default ModelPoint[] getMovePath() {
        return null;
    }

    /**
     * @return true if the sprite is actually moving
     */
    default boolean isMoving() {
        return true;
    }
}
