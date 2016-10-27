/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;
import net.vpc.gaming.atom.engine.collision.SceneCollisionManager;
import net.vpc.gaming.atom.*;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.presentation.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Taha Ben Salah
 */
public final class FilteredSpritesCollection implements SceneEngineModelListener, SceneEngineChangeListener, IterableSpritesFilter {

    private SpriteFilter filter;
    private HashMap<Integer, Sprite> sprites = new HashMap<Integer, Sprite>();
    private SceneEngine engine;

    public FilteredSpritesCollection(SceneEngine engine, SpriteFilter filter) {
        this.filter = filter;
        this.engine = engine;
        SceneEngineModel m = engine.getModel();
        if (m != null) {
            m.addSceneEngineModelListener(this);
        }
    }

    @Override
    public boolean accept(Sprite sprite) {
        return filter.accept(sprite);
    }

    @Override
    public Iterator<Sprite> iterator() {
        return new ArrayList<Sprite>(getSprites()).iterator();
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }


    public void dispose() {
        SceneEngineModel m = engine.getModel();
        if (m != null) {
            m.removeSceneEngineModelListener(this);
        }
    }


    @Override
    public void modelChanged(SceneEngine sceneEngine, SceneEngineModel oldValue, SceneEngineModel newValue) {
        //
        if (oldValue != null) {
            oldValue.removeSceneEngineModelListener(this);
        }
        if (newValue != null) {
            newValue.addSceneEngineModelListener(this);
        }
    }

    @Override
    public void spriteAdded(SceneEngineModel model, Sprite sprite) {
        if (filter.accept(sprite)) {
            sprites.put(sprite.getId(), sprite);
        }
        //
    }

    @Override
    public void spriteRemoved(SceneEngineModel model, Sprite sprite) {
        sprites.remove(sprite.getId());
    }

    @Override
    public void spriteUpdated(SceneEngineModel model, Sprite sprite, PropertyChangeEvent event) {
        if (sprites.containsKey(sprite.getId())) {
            if (!filter.accept(sprite)) {
                sprites.remove(sprite.getId());
            }
        } else {
            if (filter.accept(sprite)) {
                sprites.put(sprite.getId(), sprite);
            }
        }
    }

    public Collection<Sprite> getSprites() {
        return sprites.values();
    }


    @Override
    public void collisionManagerChanged(SceneEngine sceneEngine, SceneCollisionManager oldValue, SceneCollisionManager newValue) {
        //
    }

    @Override
    public void fpsChanged(SceneEngine sceneEngine, int oldValue, int newValue) {
        //
    }

    @Override
    public void playerFactoryChanged(SceneEngine sceneEngine, PlayerFactory oldValue, PlayerFactory newValue) {
        //
    }

    @Override
    public void spriteMoved(SceneEngineModel model, Sprite sprite, ModelPoint oldLocation, ModelPoint newLocation) {
        //
    }

    @Override
    public void spriteRemoving(SceneEngineModel model, Sprite sprite) {

    }

    @Override
    public void playerAdded(SceneEngineModel model, Player player) {
        //
    }

    @Override
    public void playerRemoved(SceneEngineModel model, Player player) {
        //
    }
}
