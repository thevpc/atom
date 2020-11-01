package net.vpc.games.atom.examples.kombla.main.shared.engine;

import net.vpc.gaming.atom.engine.DefaultSceneEngine;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Created by vpc on 10/7/16.
 */
public abstract class AbstractMainEngine extends DefaultSceneEngine {

    private int currentPlayerId;

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public Sprite findBomber() {
        return findSpriteByKind("Person", getCurrentPlayerId(), null);
    }

    public abstract void releaseBomb();

    public abstract void move(Orientation direction);

    public Sprite createExplosion(int playerId) {
        Sprite person = createSprite("Explosion");
        person.setSize(new ModelDimension(1, 1));
        person.setPlayerId(playerId);
        return person;
    }

    public static AppConfig getAppConfig(GameEngine e) {
        AppConfig v = (AppConfig) e.getProperties().getProperty(AppConfig.class.getName());
        if (v != null) {
            return v;
        }
        v = new AppConfig();
        e.getProperties().setProperty(AppConfig.class.getName(), v);
        return v;
    }
}
