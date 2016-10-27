/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.tasks;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.Sprite;

/**
 * do nothing
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class HoldPositionSpriteTask implements SpriteTask {
    public static final SpriteTask INSTANCE = new HoldPositionSpriteTask();

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        return true;
    }

    @Override
    public String toString() {
        return "Hold Position";
    }
}
