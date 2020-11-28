/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.server.engine.tasks;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Ball;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BallFollowPaddleSpriteMainTask implements SpriteMainTask {

    private int player;

    public BallFollowPaddleSpriteMainTask(int player) {
        this.player = player;
    }

    @Override
    public boolean nextFrame(SceneEngine scene, Sprite sprite) {
        Ball ball = (Ball) sprite;
        Paddle paddle = scene.findSpriteByPlayer(Paddle.class, player);
        if (player == 1) {
            ModelPoint p = paddle.getLocation();
            ball.setLocation(new ModelPoint(
                    p.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2,
                    p.getY() - ball.getHeight()));
        } else if (player == 2) {
            ModelPoint p = paddle.getLocation();
            ball.setLocation(new ModelPoint(
                    p.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2,
                    p.getY() + paddle.getHeight()));
        }
        return true;
    }

    public int getPlayer() {
        return player;
    }
}
