package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpritesByPlayerIdAndKind implements SpriteFilter {
    private final Integer playerId;
    private final String spriteKind;

    public SpritesByPlayerIdAndKind(Integer playerId, String spriteKind) {
        this.playerId = playerId;
        this.spriteKind = spriteKind;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public String getSpriteKind() {
        return spriteKind;
    }

    @Override
    public boolean accept(Sprite sprite) {
        if (sprite != null) {
            if (playerId != null) {
                if (!Objects.equals(sprite.getPlayerId(), playerId)) {
                    return false;
                }
            }
            if (spriteKind != null) {
                return Objects.equals(sprite.getKind(), spriteKind);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Sprite> find(SceneEngine engine) {
        if (playerId == null && spriteKind == null) {
            return engine.getSprites();
        }
        if (playerId == null) {
            return engine.findSpritesByKind(spriteKind);
        }
        if (spriteKind == null) {
            return engine.findSpritesByPlayer(playerId);
        }
        return engine.findSpritesByKind(spriteKind)
                .stream()
                .filter(
                        x -> playerId == x.getPlayerId()
                )
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, spriteKind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        net.thevpc.gaming.atom.engine.SpritesByPlayerIdAndKind that = (net.thevpc.gaming.atom.engine.SpritesByPlayerIdAndKind) o;
        return Objects.equals(playerId, that.playerId) && Objects.equals(spriteKind, that.spriteKind);
    }
}
