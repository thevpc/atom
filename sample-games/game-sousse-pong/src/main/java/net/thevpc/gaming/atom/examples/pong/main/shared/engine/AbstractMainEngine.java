/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.engine;

import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Ball;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Brick;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.MainModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Paddle;
import net.thevpc.gaming.atom.engine.DefaultSceneEngine;
import net.thevpc.gaming.atom.model.ModelPoint;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class AbstractMainEngine extends DefaultSceneEngine {

    public AbstractMainEngine() {
        setModel(new MainModel());
        Paddle paddle1 = new Paddle();
        paddle1.setLocation(new ModelPoint(getSize().getWidth() / 2 - paddle1.getWidth() / 2, getSize().getHeight() - paddle1.getHeight()));
        paddle1.setPlayerId(1);
        addSprite(paddle1);

        Paddle paddle2 = new Paddle();
        paddle2.setLocation(new ModelPoint(getSize().getWidth() / 2 - paddle2.getWidth() / 2, 0));
        paddle2.setPlayerId(2);
        addSprite(paddle2);

        Ball ball = new Ball();
        ball.setLocation(new ModelPoint(getSize().getWidth() / 2, getSize().getHeight() - paddle1.getHeight() - ball.getHeight()));
        addSprite(ball);

        addBrick(4, 8);
        addBrick(4, 6);
        addBrick(6, 7);
        addBrick(8, 7);
        addBrick(10, 8);
        addBrick(10, 6);
        addPlayer();
        addPlayer();
        for (Paddle paddle : findSprites(Paddle.class)) {
            setSpriteCollisionTask(paddle,new StopSpriteCollisionTask());
        }
    }

    private void addBrick(int x, int y) {
        Brick brick = new Brick();
        brick.setLocation(new ModelPoint(x, y));
        addSprite(brick);
    }

    public abstract void moveLeft(int playerId);

    public abstract void moveRight(int playerId);

    public abstract void releaseBall(int playerId);
}
