package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SpritesById implements SpriteFilter {
    private final Set<Integer> ids;

    public SpritesById(int... ids) {
        this.ids = Arrays.stream(ids).boxed().collect(Collectors.toSet());
    }

    public int[] getIds() {
        return ids.stream().mapToInt(x -> x).toArray();
    }

    @Override
    public boolean accept(Sprite sprite) {
        return sprite != null && ids.contains(sprite.getId());
    }

    @Override
    public List<Sprite> find(SceneEngine engine) {
        return ids.stream()
                .distinct().map(
                        x -> engine.getSprite(x)
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        net.thevpc.gaming.atom.engine.SpritesById that = (net.thevpc.gaming.atom.engine.SpritesById) o;
        return Objects.equals(ids, that.ids);
    }

}
