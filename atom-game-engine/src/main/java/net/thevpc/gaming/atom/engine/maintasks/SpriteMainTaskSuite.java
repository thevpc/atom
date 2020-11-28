package net.thevpc.gaming.atom.engine.maintasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/15/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpriteMainTaskSuite implements SpriteMainTask {
    private SpriteMainTask[] suite;
    private int index;

    public SpriteMainTaskSuite(SpriteMainTask... suite) {
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
