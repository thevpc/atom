/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.main.shared.presentation.view;

import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.layers.DefaultLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.vpc.gaming.atomgames.pong.main.shared.model.Paddle;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ScoreLayer extends DefaultLayer {

    private Font verdana14B = new Font("verdana", Font.BOLD, 10);

    public ScoreLayer() {
        setLayer(SCREEN_DASHBOARD_LAYER);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();

        int width = scene.getAbsoluteCamera().getWidth();
        int scoreWidth = 100;
        int scoreHeight = 35;
        int scoreX = width - scoreWidth - 10;
        int scoreY = 5;
        int y0 = 12;
        int x0 = scoreX + 5;
        int line1 = scoreY + y0;
        int line2 = scoreY + y0 + 15;
        int col2 = 60;
        //transparent blue mask
        Composite originalComposite = graphics.getComposite();
        graphics.setColor(Color.WHITE);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        graphics.fillRoundRect(scoreX, scoreY, scoreWidth, scoreHeight, 20, 20);
        graphics.setComposite(originalComposite);


        graphics.setColor(Color.DARK_GRAY);
        graphics.setFont(verdana14B);
        graphics.drawString("Joueur 1 :", scoreX + 5, line1);
        graphics.drawString("Joueur 2 :", scoreX + 5, line2);

        graphics.setColor(Color.RED.darker());
        graphics.setFont(verdana14B);

        Paddle paddle1 = scene.getSceneEngine().findSpriteByPlayer(Paddle.class, 1);
        Paddle paddle2 = scene.getSceneEngine().findSpriteByPlayer(Paddle.class, 2);

        if (paddle1 != null) {
            graphics.drawString("" + paddle1.getLife(), x0 + col2, line1);
        } else {
            graphics.drawString("Game Over", x0 + col2, line1);
        }
        if (paddle2 != null) {
            graphics.drawString("" + paddle2.getLife(), x0 + col2, line2);
        } else {
            graphics.drawString("Game Over", x0 + col2, line2);
        }
    }
}
