package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SpritesByName implements SpriteFilter {
    private final Set<String> names;

    public SpritesByName(String... names) {
        this.names = Arrays.stream(names).collect(Collectors.toSet());
    }

    @Override
    public boolean accept(Sprite sprite) {
        return sprite != null && names.contains(sprite.getName());
    }

    public String[] getNames() {
        return names.toArray(new String[0]);
    }

    @Override
    public int hashCode() {
        return Objects.hash(names);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        net.thevpc.gaming.atom.engine.SpritesByName that = (net.thevpc.gaming.atom.engine.SpritesByName) o;
        return Objects.equals(names, that.names);
    }

    @Override
    public List<Sprite> find(SceneEngine engine) {
        return names.stream()
                .distinct()
                .flatMap(
                        x -> engine.findSpritesByName(x).stream()
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
