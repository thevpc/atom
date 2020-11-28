/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * do nothing
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class HoldPositionSpriteMainTask implements SpriteMainTask {
    public static final SpriteMainTask INSTANCE = new HoldPositionSpriteMainTask();

    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        return true;
    }

    @Override
    public String toString() {
        return "Hold Position";
    }
}
