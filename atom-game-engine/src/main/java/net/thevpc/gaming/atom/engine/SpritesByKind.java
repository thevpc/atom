package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SpritesByKind implements SpriteFilter {
    private final Set<String> kinds;

    public SpritesByKind(String... kinds) {
        this.kinds = Arrays.stream(kinds).collect(Collectors.toSet());
    }

    @Override
    public boolean accept(Sprite sprite) {
        return sprite != null && kinds.contains(sprite.getKind());
    }

    public String[] getKinds() {
        return kinds.toArray(new String[0]);
    }

    @Override
    public List<Sprite> find(SceneEngine engine) {
        return kinds.stream()
                .distinct()
                .flatMap(
                        x -> engine.findSpritesByKind(x).stream()
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(kinds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        net.thevpc.gaming.atom.engine.SpritesByKind that = (net.thevpc.gaming.atom.engine.SpritesByKind) o;
        return Objects.equals(kinds, that.kinds);
    }
}
