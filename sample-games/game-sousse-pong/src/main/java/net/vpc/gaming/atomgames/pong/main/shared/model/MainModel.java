/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.shared.model;

import net.vpc.gaming.atom.model.DefaultSceneEngineModel;
import net.vpc.gaming.atom.model.ModelPoint;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainModel extends DefaultSceneEngineModel {

    private AppRole role = AppRole.SERVER;
    private AppPhase phase = AppPhase.WAITING;

    public MainModel() {
        super(30, 20);
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
    }

    private void addBrick(int x, int y) {
        Brick brick = new Brick();
        brick.setLocation(new ModelPoint(x, y));
        addSprite(brick);
    }

    public AppPhase getPhase() {
        return phase;
    }

    public void setPhase(AppPhase phase) {
        this.phase = phase;
    }

    public AppRole getRole() {
        return role;
    }

    public void setRole(AppRole role) {
        this.role = role;
    }
}
