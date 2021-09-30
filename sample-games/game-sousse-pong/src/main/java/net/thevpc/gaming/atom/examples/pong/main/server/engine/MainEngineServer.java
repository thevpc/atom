/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.server.engine;

import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.DALServer;
import net.thevpc.gaming.atom.examples.pong.main.server.dal.DALServerListener;
import net.thevpc.gaming.atom.examples.pong.main.server.engine.collisions.BallCollisionTask;
import net.thevpc.gaming.atom.examples.pong.main.server.engine.tasks.BallFollowPaddleSpriteMainTask;
import net.thevpc.gaming.atom.model.MovementStyles;
import net.thevpc.gaming.atom.model.Orientation;
import net.thevpc.gaming.atom.examples.pong.hello.business.WelcomeEngine;
import net.thevpc.gaming.atom.examples.pong.hello.model.WelcomeModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.DALFactory;
import net.thevpc.gaming.atom.examples.pong.main.shared.engine.AbstractMainEngine;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.AppPhase;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Ball;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.MainModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainEngineServer extends AbstractMainEngine implements DALServerListener {

    private DALServer dal;

    public MainEngineServer() {
        super();
    }

    @Override
    public void clientConnected() {
        MainModel m = getModel();
        m.setPhase(AppPhase.GAMING);
    }

    private WelcomeModel getHelloModel() {
        return getGameEngine().getSceneEngine(WelcomeEngine.class).getModel();
    }

    //    @Override
//    public PlayScreenModel getModel() {
//        return (PlayScreenModel) super.getModel();
//    }
    @Override
    public void sceneActivating() {
        MainModel m = getModel();
        m.setRole(getHelloModel().getRole());
        m.setPhase(AppPhase.WAITING);
        dal = DALFactory.createServer(getHelloModel().getTransport());

        Ball ball = findSprite(Ball.class);
        ball.setDirection(Orientation.NORTH_EAST);
        setSpriteCollisionTask(ball, new BallCollisionTask());
        setSpriteMainTask(ball, new BallFollowPaddleSpriteMainTask(1));
        ball.setMovementStyle(MovementStyles.STOPPED);

        for (Paddle paddle : findSprites(Paddle.class)) {
            setSpriteMainTask(paddle, null);
            paddle.setMovementStyle(MovementStyles.STOPPED);
        }
        dal.start("localhost", 1050, this);
    }

    @Override
    public void updateModel() {
        MainModel m = getModel();
        if (findSprites(Paddle.class).size() < 2) {
            m.setPhase(AppPhase.GAMEOVER);
        }
    }

    @Override
    protected void modelUpdated() {
        MainModel m = getModel();
        switch (m.getPhase()) {
            case WAITING: {
                //do nothing
                break;
            }
            case GAMING: {
                //do nothing
                dal.sendModelChanged(this);
                break;
            }
            case GAMEOVER: {
                //do nothing
                dal.sendModelChanged(this);
                break;
            }
        }
    }


    public void move(int playerId, Orientation direction) {
        MainModel m = getModel();
        if (m.getPhase() == AppPhase.GAMING) {
            Paddle paddle = findSpriteByPlayer(Paddle.class, playerId);
            if(paddle!=null) {
                paddle.setDirection(direction);
                SpriteMainTask task = getSpriteMainTask(paddle);
                if (task == null || !(task instanceof MoveSpriteMainTask)) {
                    setSpriteMainTask(paddle, new MoveSpriteMainTask());
                    paddle.setMovementStyle(MovementStyles.MOVING_SLOW);
                }
            }
        }
    }

    public void moveLeft(int playerId) {
        move(playerId, Orientation.WEST);
    }

    public void moveRight(int playerId) {
        move(playerId, Orientation.EAST);
    }

    public void releaseBall(int playerId) {
        MainModel m = getModel();
        if (m.getPhase() == AppPhase.GAMING) {
            Ball ball = findSprite(Ball.class);
            //ball is null if game over
            if (ball != null) {
                SpriteMainTask task = getSpriteMainTask(ball);
                if (task instanceof BallFollowPaddleSpriteMainTask) {
                    BallFollowPaddleSpriteMainTask followBarTask = (BallFollowPaddleSpriteMainTask) task;
                    if (followBarTask.getPlayer() == playerId) {
                        setSpriteMainTask(ball, new MoveSpriteMainTask());
                        ball.setMovementStyle(MovementStyles.MOVING_SLOW);
                    }
                }
            }
        }
    }

    @Override
    public void recieveKeyLeft() {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                //left pour le joueur 2 == right pour le joueur 1
                moveRight(2);
            }
        });
    }

    @Override
    public void recieveKeyRight() {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                moveLeft(2);
            }
        });
    }

    @Override
    public void recieveKeySpace() {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                releaseBall(2);
            }
        });
    }
}
