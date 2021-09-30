/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.presentation.view;

import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Ball;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Brick;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Paddle;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.examples.pong.main.shared.presentation.controller.MainController;

import java.awt.geom.AffineTransform;

import net.thevpc.gaming.atom.presentation.layers.ImageBoardLayer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainScene extends DefaultScene {
    public MainScene(int player) {
        super("MainScene",new ViewDimension(20, 20));
//        setIsometric(true);
        setSpriteView(SpriteFilter.byType(Ball.class), new ImageSpriteView("/ball.png", 8, 4));
        setSpriteView(SpriteFilter.byType(Paddle.class), new ImageSpriteView("/paddle.png", 1, 2,new SpritePlayerImageSelector()));
        setSpriteView(SpriteFilter.byType(Brick.class), new ImageSpriteView("/brick.png", 2, 2,new SpriteStyleImageSelector()));
//        addLayer(new FillBoxLayer(Color.BLACK));
        addLayer(new ImageBoardLayer("/bkg.png"));
//        addLayer(new VelocityInfoLayer());
        addLayer(new ScoreLayer());
        addLayer(new MessagesLayer());
        addController(new MainController(player));
        addLifeCycleListener(new SceneLifeCycleListener() {
            @Override
            public void sceneStarted(Scene scene) {
                scene.addControlPlayer(player);
                int player = getControlPlayer().getId();
                setTitle("AGE :: Pong " + (player == 1 ? "Server" : "Client"));
                if (player == 2) {
                    ViewBox viewPort = getCamera().getViewPort();
                    setBoardAffineTransform(AffineTransform.getRotateInstance(-Math.PI, viewPort.getWidth() * 0.5, viewPort.getHeight() * 0.5));
                }
            }
        });
    }
}
