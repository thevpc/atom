/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.shared.engine.tasks;

import net.vpc.gaming.atom.annotations.AtomSpriteTask;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.games.atom.examples.kombla.main.shared.engine.AbstractMainEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteTask(
        engine = "mainLocal,mainServer",
        kind = "Bomb"
)
public class BombSpriteTask implements SpriteTask {

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
