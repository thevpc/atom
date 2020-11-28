/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.physics;

import net.thevpc.gaming.atom.engine.IterableSpritesFilter;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngineTask;
import net.thevpc.gaming.atom.engine.collisiontasks.BorderCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.TileCollision;
import net.thevpc.gaming.atom.extension.DefaultSceneEngineExtension;
import net.thevpc.gaming.atom.model.ModelVector;
import net.thevpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah
 */
public class PhysicsExtension extends DefaultSceneEngineExtension
        implements SpriteCollisionTask, SceneEngineTask {

    private IterableSpritesFilter filter;
    private ModelVector gravity;

    public PhysicsExtension(double gy) {
        this(gy, null);
    }

    public PhysicsExtension(double gy, IterableSpritesFilter filter) {
        this(ModelVector.newCartesien(0, gy,0), filter);
    }

    public PhysicsExtension(ModelVector gravity) {
        this(gravity, null);
    }

    public PhysicsExtension(ModelVector gravity, IterableSpritesFilter filter) {
        this.gravity = gravity;
        this.filter = filter;
    }

    @Override
    public void install(SceneEngine sceneEngine, Sprite sprite) {

    }

    @Override
    public void uninstall(SceneEngine sceneEngine, Sprite sprite) {

    }

    @Override
    public void install(SceneEngine engine) {
        engine.addSpriteCollisionManager(this);
        engine.addSceneTask(this);
    }

    @Override
    public void uninstall(SceneEngine engine) {
        engine.removeSpriteCollisionManager(this);
        engine.removeSceneTask(this);
    }

    @Override
    public boolean nextFrame(SceneEngine sceneEngine) {
        if (filter == null) {
            for (Sprite sprite : sceneEngine.getSprites()) {
                apply(sprite);
            }
        } else {
            for (Sprite sprite : filter) {
                apply(sprite);
            }
        }
        return true;
    }

    private void apply(Sprite sprite) {
        ModelVector velocity = sprite.getVelocity();
        velocity = velocity.add(gravity);
//        velocity=ModelVector.newCartesien(velocity.getX()*0.95, velocity.getY());
        sprite.setVelocity(velocity.add(gravity));
        //sprite.setLocation(sprite.getLocation().translate(gravity));
    }

    @Override
    public void collideWithBorder(BorderCollision borderCollision) {
        if (borderCollision.isSpriteCollisionSouth()) {
            Sprite s = borderCollision.getSprite();
            ModelVector v = s.getVelocity();
            s.setVelocity(ModelVector.newCartesien(v.getX(), 0));
        }
        //
    }

    @Override
    public void collideWithSprite(SpriteCollision spriteCollision) {
        if (spriteCollision.isSpriteCollisionSouth()) {
            Sprite s = spriteCollision.getSprite();
            ModelVector v = s.getVelocity();
            s.setVelocity(ModelVector.newCartesien(v.getX(), 0));
        }
    }

    @Override
    public void collideWithTile(TileCollision tileCollision) {
        if (tileCollision.isSpriteCollisionSouth()) {
            Sprite s = tileCollision.getSprite();
            ModelVector v = s.getVelocity();
            s.setVelocity(ModelVector.newCartesien(v.getX(), 0));
        }
    }
}
