package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SpritesByPlayerId implements SpriteFilter {
    private final Set<Integer> playerIds;

    public SpritesByPlayerId(int... playerIds) {
        this.playerIds = Arrays.stream(playerIds).boxed().collect(Collectors.toSet());
    }

    public int[] getPlayerIds() {
        return playerIds.stream().mapToInt(x -> x).toArray();
    }

    @Override
    public boolean accept(Sprite sprite) {
        return sprite != null && playerIds.contains(sprite.getPlayerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        net.thevpc.gaming.atom.engine.SpritesByPlayerId that = (net.thevpc.gaming.atom.engine.SpritesByPlayerId) o;
        return Objects.equals(playerIds, that.playerIds);
    }

    @Override
    public List<Sprite> find(SceneEngine engine) {
        return playerIds.stream()
                .distinct()
                .flatMap(
                        x -> engine.findSpritesByPlayer(x).stream()
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
