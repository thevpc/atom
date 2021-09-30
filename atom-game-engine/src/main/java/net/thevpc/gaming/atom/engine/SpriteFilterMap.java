package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.util.ClassMap;

import java.util.*;

public class SpriteFilterMap<T> {
    Map<Integer, SpriteFilter> ids = new LinkedHashMap<>();
    ClassMap<SpriteFilter> spriteTypes = new ClassMap<SpriteFilter>(Object.class,SpriteFilter.class);
    Map<Integer, SpriteFilter> playerIds = new LinkedHashMap<>();
    Map<String, SpriteFilter> names = new LinkedHashMap<>();
    Map<String, SpriteFilter> kinds = new LinkedHashMap<>();
    Map<SpriteFilter, T> all = new LinkedHashMap<>();
    List<SpriteFilter> ordered = new ArrayList<>();

    public void put(SpriteFilter s, T t) {
        if (all.containsKey(s)) {
            T q = all.get(s);
            if (!Objects.equals(q, t)) {
                all.put(s, t);
                rebuild();
            }
        } else {
            all.put(s, t);
            rebuild();
        }
    }

    public T get(Sprite s) {
        {
            SpriteFilter t = ids.get(s.getId());
            if (t != null) {
                if (t.accept(s)) {
                    return all.get(t);
                }
            }
        }
        {
            SpriteFilter t = names.get(s.getName());
            if (t != null) {
                if (t.accept(s)) {
                    return all.get(t);
                }
            }
        }
        {
            SpriteFilter t = kinds.get(s.getKind());
            if (t != null) {
                if (t.accept(s)) {
                    return all.get(t);
                }
            }
        }
        {
            SpriteFilter t = playerIds.get(s.getPlayerId());
            if (t != null) {
                if (t.accept(s)) {
                    return all.get(t);
                }
            }
        }
        {
            SpriteFilter t = spriteTypes.get(s.getClass());
            if (t != null) {
                if (t.accept(s)) {
                    return all.get(t);
                }
            }
        }
        for (Map.Entry<SpriteFilter, T> e : all.entrySet()) {
            if (e.getKey().accept(s)) {
                return e.getValue();
            }
        }
        return null;
    }

    public void remove(SpriteFilter s) {
        if (all.containsKey(s)) {
            all.remove(s);
            rebuild();
        }
    }

    private void rebuild() {
        ids.clear();
        names.clear();
        kinds.clear();
        playerIds.clear();
        spriteTypes.clear();
        for (SpriteFilter filter : all.keySet()) {
            if (filter instanceof SpritesById) {
                for (int id : ((SpritesById) filter).getIds()) {
                    ids.put(id, filter);
                }
            } else if (filter instanceof SpritesByName) {
                for (String name : ((SpritesByName) filter).getNames()) {
                    names.put(name, filter);
                }
            } else if (filter instanceof SpritesByKind) {
                for (String kind : ((SpritesByKind) filter).getKinds()) {
                    kinds.put(kind, filter);
                }
            } else if (filter instanceof SpritesByPlayerId) {
                for (int playerId : ((SpritesByPlayerId) filter).getPlayerIds()) {
                    playerIds.put(playerId, filter);
                }
            } else if (filter instanceof SpritesByPlayerIdAndKind) {
                SpritesByPlayerIdAndKind f = (SpritesByPlayerIdAndKind) filter;
                if (f.getPlayerId() != null) {
                    playerIds.put(f.getPlayerId(), filter);
                }
                if (f.getSpriteKind() != null) {
                    kinds.put(f.getSpriteKind(), filter);
                }
            } else if (filter instanceof SpritesByType) {
                SpritesByType f = (SpritesByType) filter;
                for (Class type : f.getTypes()) {
                    spriteTypes.put(type,filter);
                }
            }
        }
        ordered.clear();
        ordered.addAll(all.keySet());
        ordered.sort(new Comparator<SpriteFilter>() {
            private int typeOrder(SpriteFilter o1) {
                return o1 instanceof SpritesById ? 1
                        : o1 instanceof SpritesByName ? 2
                        : o1 instanceof SpritesByKind ? 3
                        : o1 instanceof SpritesByPlayerId ? 4
                        : o1 instanceof SpritesByPlayerIdAndKind ? 5 :
                        6;
            }

            public int compare(SpriteFilter o1, SpriteFilter o2) {
                int x = Integer.compare(typeOrder(o1), typeOrder(o2));
                return x;
            }
        });
    }
}
