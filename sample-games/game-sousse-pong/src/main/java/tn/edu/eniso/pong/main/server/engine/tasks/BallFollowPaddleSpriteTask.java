/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.engine.tasks;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import tn.edu.eniso.pong.main.shared.model.Ball;
import tn.edu.eniso.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BallFollowPaddleSpriteTask implements SpriteTask {

    private int player;

    public BallFollowPaddleSpriteTask(int player) {
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
