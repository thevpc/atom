/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.collision;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.Sprite;

/**
 * SpriteCollisionManager Handles collision logic for a given Sprite.
 * Collision it self is detected in SceneCollisionManager.
 * SpriteCollisionManager implements behavior of the Sprite when Collision is detected.
 * Three Collision types are supported :
 * <ul>
 * <li>Collision with border : When Sprite moves outside the Game Area</li>
 * <li>Collision with sprite : When Sprite overlaps with another Sprite (while other.isCrossable()=false) </li>
 * <li>Collision with Tile   : When Sprite overlaps with a Wall Tile (tile.getWalls()!=Tile.NO_WALLS) </li>
 * </ul>
 * Usually it is not necessary to have all three method implementations, consider extending the Adapter class DefaultSpriteCollisionManager or SimpleSpriteCollisionManager.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SpriteCollisionManager {

    public void install(SceneEngine sceneEngine, Sprite sprite);

    public void uninstall(SceneEngine sceneEngine, Sprite sprite);

    /**
     * Called when Sprite moves outside the Game Area
     *
     * @param borderCollision collision info
     */
    public void collideWithBorder(BorderCollision borderCollision);

    /**
     * Called when Sprite overlaps with another Sprite (while other.isCrossable()=false)
     *
     * @param spriteCollision collision info
     */
    public void collideWithSprite(SpriteCollision spriteCollision);

    /**
     * Called when Sprite overlaps with a Wall Tile (tile.getWalls()!=Tile.NO_WALLS)
     *
     * @param tileCollision collision info
     */
    public void collideWithTile(TileCollision tileCollision);
}
