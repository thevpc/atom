/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.shared.presentation.view;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import net.vpc.gaming.atom.annotations.AtomSpriteView;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.ImageSpriteView;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteDrawingContext;
import net.vpc.gaming.atom.presentation.SpriteViewImageSelector;


/**
 *
 * @author Taha Ben Salah
 */
@AtomSpriteView(
        kind = "Tank"
)
public class TankView extends ImageSpriteView {

    public TankView() {
        super("/net/vpc/games/atom/examples/tanks/images/tank.png", 8, 8);
        setFramesPerImage(5);
        setImageSelector(new SpriteViewImageSelector() {
            @Override
            public int getImageIndex(Sprite sprite, Scene scene, long frame, int imagesCount) {
                final int player = sprite.getPlayerId();
                return ((7 - (int) (frame % 8))) + (player - 1) * 8;
            }
        });
    }

    @Override
    public void draw(SpriteDrawingContext context) {
        Graphics2D stylo = context.getGraphics();
        Sprite s = context.getSprite();
        double d = s.getDirection();
        Rectangle r = context.getSpriteBounds().toRectangle();
        stylo.rotate(d, r.getCenterX(), r.getCenterY());
        super.draw(context);
        stylo.rotate(-d, r.getCenterX(), r.getCenterY());

    }
}
