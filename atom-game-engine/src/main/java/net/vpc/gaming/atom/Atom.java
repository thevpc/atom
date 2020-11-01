/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom;

import net.vpc.gaming.atom.presentation.*;
import net.vpc.gaming.atom.engine.DefaultGameEngine;
import net.vpc.gaming.atom.engine.GameEngine;

/**
 * @author vpc
 */
public class Atom {

    public static final SpriteViewImageSelector IMAGE_SELECTOR_FRAME_ANIMATED = FrameAnimatedImageSelector.INSTANCE;
    public static final SpriteViewImageSelector IMAGE_SELECTOR_SPRITE_VARIATION = SpriteStyleImageSelector.INSTANCE;
    public static final SpriteViewImageSelector IMAGE_SELECTOR_SPRITE_PLAYER = SpritePlayerImageSelector.INSTANCE;

    public static Game startGame() {
        Game game = createGame();
        game.start();
        return game;
    }

    public static Game createGame() {
        return createGame(createGameEngine());
    }

    public static Game createGame(GameEngine engine) {
        Game game = new DefaultGame(engine);
        game.buildAssets();
        engine.setContainer(game.getContainer());
        return game;
    }

    public static GameEngine createGameEngine() {
        return new DefaultGameEngine();
    }

}
