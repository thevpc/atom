/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.shared.presentation.view;

import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.presentation.DefaultScene;
import net.vpc.gaming.atomgames.pong.main.shared.model.Ball;
import net.vpc.gaming.atomgames.pong.main.shared.model.Brick;
import net.vpc.gaming.atomgames.pong.main.shared.model.Paddle;
import net.vpc.gaming.atomgames.pong.main.shared.presentation.controller.MainController;

import java.awt.geom.AffineTransform;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.SpritePlayerImageSelector;
import net.vpc.gaming.atom.presentation.SpriteStyleImageSelector;
import net.vpc.gaming.atom.presentation.layers.ImageBoardLayer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainScene extends DefaultScene {
    public MainScene(int player) {
        super("MainScene",new ViewDimension(20, 20));
//        setIsometric(true);
        setSpriteView(Ball.class, new ImageSpriteView("/ball.png", 8, 4));
        setSpriteView(Paddle.class, new ImageSpriteView("/paddle.png", 1, 2,new SpritePlayerImageSelector()));
        setSpriteView(Brick.class, new ImageSpriteView("/brick.png", 2, 2,new SpriteStyleImageSelector()));
//        addLayer(new FillBoxLayer(Color.BLACK));
        addLayer(new ImageBoardLayer("/bkg.png"));
//        addLayer(new VelocityInfoLayer());
        addLayer(new ScoreLayer());
        addLayer(new MessagesLayer());
        addSceneController(new MainController(player));
    }

    @Override
    protected void sceneStarted() {
        int player = getControlPlayer().getId();
        setTitle("AGE :: Pong " + (player == 1 ? "Server" : "Client"));
        if (player == 2) {
            ViewBox viewPort = getAbsoluteCamera();
            setBoardAffineTransform(AffineTransform.getRotateInstance(-Math.PI, viewPort.getWidth() * 0.5, viewPort.getHeight() * 0.5));
        }
    }
}
