/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.shared.engine;

import net.vpc.gaming.atom.engine.DefaultSceneEngine;
import net.vpc.gaming.atom.engine.collision.SimpleSpriteCollisionManager;
import net.vpc.gaming.atomgames.pong.main.shared.model.MainModel;
import net.vpc.gaming.atomgames.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class AbstractMainEngine extends DefaultSceneEngine {

    public AbstractMainEngine() {
        setModel(new MainModel());
        addPlayer();
        addPlayer();
        for (Paddle paddle : findSprites(Paddle.class)) {
            setSpriteCollisionManager(paddle,new SimpleSpriteCollisionManager());
        }
    }


    public abstract void moveLeft(int playerId);

    public abstract void moveRight(int playerId);

    public abstract void relaseBall(int playerId);
}
