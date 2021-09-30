/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.shared.engine.tasks;

import net.thevpc.gaming.atom.annotations.AtomSpriteMainTask;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AbstractMainEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.Tile;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteMainTask(
        sceneEngine = "mainLocal,mainServer",
        kind = "Bomb"
)
public class BombSpriteMainTask implements SpriteMainTask {

    private int time = 100;

    @Override
    public boolean nextFrame(SceneEngine sceneEngine, Sprite bomb) {
        if (time > 0) {
            time--;
            return true;
        } else {
            bomb.die();
            Tile tile = sceneEngine.findTile(bomb.getLocation());
            Sprite e = ((AbstractMainEngine)sceneEngine).createExplosion(bomb.getPlayerId());
            e.setLocation(tile.getLocation());
            sceneEngine.addSprite(e);
            return false;
        }
    }
}
