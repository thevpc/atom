package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SpritesByType implements SpriteFilter {
    private final Set<Class> types;

    public SpritesByType(Class... types) {
        this.types = Arrays.stream(types).collect(Collectors.toSet());
    }

    public Class[] getTypes() {
        return types.toArray(new Class[0]);
    }

    @Override
    public boolean accept(Sprite sprite) {
        if(sprite!=null){
            for (Class type : types) {
                if(type.isAssignableFrom(sprite.getClass())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Sprite> find(SceneEngine engine) {
        return types.stream()
                .distinct()
                .flatMap(
                        x -> ((List<Sprite>)engine.findSprites(x)).stream()
                ).filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(types);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpritesByType that = (SpritesByType) o;
        return Objects.equals(types, that.types);
    }

}
