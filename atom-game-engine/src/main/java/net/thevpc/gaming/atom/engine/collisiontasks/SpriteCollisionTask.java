/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.collisiontasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * SpriteCollisionTask Handles collisiontasks logic for a given Sprite.
 * Collision it self is detected in SceneCollisionManager.
 * SpriteCollisionTask implements behavior of the Sprite when Collision is detected.
 * Three Collision types are supported :
 * <ul>
 * <li>Collision with border : When Sprite moves outside the Game Area</li>
 * <li>Collision with sprite : When Sprite overlaps with another Sprite (while other.isCrossable()=false) </li>
 * <li>Collision with Tile   : When Sprite overlaps with a Wall Tile (tile.getWalls()!=Tile.NO_WALLS) </li>
 * </ul>
 * Usually it is not necessary to have all three method implementations, consider extending the Adapter class DefaultSpriteCollisionTask or SimpleSpriteCollisionTask.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SpriteCollisionTask {

    default void install(SceneEngine sceneEngine, Sprite sprite){

    }

    default void uninstall(SceneEngine sceneEngine, Sprite sprite){

    }

    /**
     * Called when Sprite moves outside the Game Area
     *
     * @param borderCollision collisiontasks info
     */
    default void collideWithBorder(BorderCollision borderCollision){

    }

    /**
     * Called when Sprite overlaps with another Sprite (while other.isCrossable()=false)
     *
     * @param spriteCollision collisiontasks info
     */
    default void collideWithSprite(SpriteCollision spriteCollision){

    }

    /**
     * Called when Sprite overlaps with a Wall Tile (tile.getWalls()!=Tile.NO_WALLS)
     *
     * @param tileCollision collisiontasks info
     */
    default void collideWithTile(TileCollision tileCollision){

    }
}
