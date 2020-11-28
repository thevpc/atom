/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Kills the sprite (<code> addLife(-1) </code>) step by step
 * Interesting in animation sprites that must die after a fixed amount of time.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SuicideSpriteMainTask implements SpriteMainTask {
    private int suicideVelocity;

    public SuicideSpriteMainTask() {
        this(1);
    }

    public SuicideSpriteMainTask(int suicideVelocity) {
        if (suicideVelocity <= 0) {
            throw new IllegalArgumentException("Suicide velocity must be positive");
        }
        this.suicideVelocity = suicideVelocity;
    }

    @Override
    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        if (sprite.getLife() > 0) {
            sprite.addLife(-suicideVelocity);
            return true;
        }
        return false;
    }

}
