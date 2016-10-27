package net.vpc.gaming.atom.engine.tasks;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/15/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpriteTaskSuite implements SpriteTask {
    private SpriteTask[] suite;
    private int index;

    public SpriteTaskSuite(SpriteTask... suite) {
        this.suite = suite;
        index = 0;
    }

    @Override
    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        while (index < suite.length) {
            if (!suite[index].nextFrame(scene, sprite)) {
                return true;
            } else {
                index++;
            }
        }
        return false;
    }
}
