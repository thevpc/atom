/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.shared.presentation.view;

import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SequenceGenerator;
import net.vpc.gaming.atom.presentation.layers.DefaultLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.vpc.gaming.atom.util.AtomUtils;
import net.vpc.gaming.atomgames.pong.main.server.engine.tasks.BallFollowPaddleSpriteTask;
import net.vpc.gaming.atomgames.pong.main.shared.model.AppPhase;
import net.vpc.gaming.atomgames.pong.main.shared.model.Ball;
import net.vpc.gaming.atomgames.pong.main.shared.model.MainModel;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MessagesLayer extends DefaultLayer {

    private Font verdana20B = new Font("verdana", Font.BOLD, 20);
    private Font verdana14B = new Font("verdana", Font.BOLD, 14);
    private SequenceGenerator blink10 = SequenceGenerator.createUnsignedSequence(10, 10);

    public MessagesLayer() {
        setLayer(SCREEN_DASHBOARD_LAYER);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();

        graphics.setColor(Color.YELLOW);
        ViewBox viewBox = scene.getCameraScreen();
        int viewBoxWidth = viewBox.getWidth();
        graphics.drawRect(0, 0, viewBoxWidth - 1, viewBoxWidth - 1);
        MainModel model = (MainModel) scene.getSceneEngine().getModel();
        AppPhase phase = model.getPhase();


        switch (phase) {
            case WAITING: {
                //transparent grey mask
                Composite originalComposite = graphics.getComposite();
                graphics.setColor(Color.GRAY);
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                graphics.fillRect(0, 0, viewBoxWidth - 1, viewBoxWidth - 1);
                graphics.setComposite(originalComposite);

                //waiting message
                graphics.setFont(verdana20B);
                graphics.setColor(Color.WHITE);
                switch (model.getRole()) {
                    case SERVER: {
                        AtomUtils.drawCenteredString(graphics, "Waiting For Client...", viewBox);
                        break;
                    }
                    case CLIENT: {
                        AtomUtils.drawCenteredString(graphics, "Waiting For Server...", viewBox);
                        break;
                    }
                }
                break;
            }
            case GAMING: {
                Ball ball = scene.getSceneEngine().findSprite(Ball.class);
                if (ball != null) {
                    SpriteTask t = scene.getSceneEngine().getSpriteTask(ball);
                    if (t instanceof BallFollowPaddleSpriteTask) {
                        BallFollowPaddleSpriteTask f = (BallFollowPaddleSpriteTask) t;
                        if (blink10.next(scene.getFrame()) == 1 && f.getPlayer() == scene.getControlPlayer().getId()) {
                            graphics.setColor(Color.WHITE);
                            graphics.setFont(verdana14B);
                            AtomUtils.drawCenteredString(graphics, "Press Space...", viewBox);
                        }
                    }
                }
                break;
            }
            case GAMEOVER: {
                //transparent grey mask
                Composite originalComposite = graphics.getComposite();
                graphics.setColor(Color.GRAY);
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                graphics.fillRect(0, 0, viewBoxWidth - 1, viewBoxWidth - 1);
                graphics.setComposite(originalComposite);

                //gameover message
                graphics.setFont(verdana20B);
                graphics.setColor(Color.WHITE);

                graphics.drawString("GAME OVER", 100, 100);
                break;
            }
        }
    }
}
