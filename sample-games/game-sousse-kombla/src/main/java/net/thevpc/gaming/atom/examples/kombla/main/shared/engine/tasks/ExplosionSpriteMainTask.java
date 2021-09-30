/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.shared.engine.tasks;

import net.thevpc.gaming.atom.annotations.AtomSpriteMainTask;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AbstractMainEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteMainTask(
        sceneEngine = "mainLocal,mainServer",
        kind = "Explosion"
)
public class ExplosionSpriteMainTask implements SpriteMainTask {

    private static int EXPLOSION_TIME = 25;
    /**
     * explosion remaining lifetime
     */
    private int time = 0;
    /**
     * explosion range
     * when positive range, it propagates over each direction
     * with a smaller range (range minus one)
     */
    private int range;

    public ExplosionSpriteMainTask() {
        this(4);
    }

    /**
     * definition if exploision sprite task
     *
     * @param range range of the explosion
     */
    public ExplosionSpriteMainTask(int range) {
        this.range = range;
        this.time = EXPLOSION_TIME;
    }

    /**
     *
     */
    @Override
    public boolean nextFrame(SceneEngine sceneEngine, Sprite explosion) {
        AbstractMainEngine main=(AbstractMainEngine) sceneEngine;
        try {
            if (time == 25) {
                if (range > 0) {
                    //when range is positive evaluate possible propagation directions
                    Tile tile = sceneEngine.findTile(explosion.getLocation());
                    ModelBox bounds = tile.getBounds();
                    for (int r = -1; r <= 1; r++) {
                        for (int c = -1; c <= 1; c++) {
                            if (r * c != 0) {
                                continue;
                            }
                            //next neighbour tile
                            Tile next = sceneEngine.findTile(new ModelPoint(bounds.getX() + c, bounds.getY() + r));
                            if (next != null) {
                                //check if the tile is NOT a wall
                                //walls are defined as any non zero kind value (Tile.getKind()!=Tile.NO_WALLS)
                                boolean explode = next.getWalls() == Tile.NO_WALLS;
                                if (explode) {
                                    //if the current tile holds already another explosion
                                    //no need to propagate
                                    for (Sprite other : sceneEngine.findSprites(next.getBounds())) {
                                        if (other.getKind().equals("Explosion")) {
                                            explode = false;
                                            break;
                                        }
                                    }
                                    if (explode) {
                                        Sprite e = main.createExplosion(explosion.getPlayerId());
                                        e.setLocation(next.getLocation());
                                        sceneEngine.addSprite(e);
                                        // create explosion sprite
                                        //with smaller range (range-1)
                                        sceneEngine.setSpriteMainTask(e, new ExplosionSpriteMainTask(range - 1));
                                        e.setMovementStyle(MovementStyles.STOPPED);
                                    }
                                }
                            }
                        }
                    }
                }
                time--;
                return true;
            } else if (time > 0) {
                time--;
                return true;
            } else {
                //when lifetime is consumed, kill sprite
                explosion.die();
                return false;
            }
        }finally{
            explosion.setProperty("explosionTime",time);
        }
    }

    /**
     * remaining explosion sprite lifetime
     *
     * @return time to live
     */
    public int getTime() {
        return time;
    }
}
