/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sprite Filter
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SpriteFilter {
    static SpriteFilter byId(int... ids) {
        return new SpritesById(ids);
    }

    static SpriteFilter byName(String... ids) {
        return new SpritesByName(ids);
    }

    static SpriteFilter byType(Class... types) {
        return new SpritesByType(types);
    }

    static SpriteFilter byPlayerIdAndKind(Integer playerId, String kind) {
        return new SpritesByPlayerIdAndKind(playerId, kind);
    }

    static SpriteFilter byKind(String... kinds) {
        return new SpritesByKind(kinds);
    }

    static SpriteFilter byPlayerId(int... players) {
        return new SpritesByPlayerId(players);
    }


    boolean accept(Sprite sprite);

    default List<Sprite> find(SceneEngine engine) {
        return engine.getSprites().stream().filter(x -> accept(x)).collect(Collectors.toList());
    }

}
