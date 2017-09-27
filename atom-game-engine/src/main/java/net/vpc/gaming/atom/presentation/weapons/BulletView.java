package net.vpc.gaming.atom.presentation.weapons;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.presentation.DefaultSpriteView;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteDrawingContext;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class BulletView extends DefaultSpriteView {

    public void draw(SpriteDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        graphics.setColor(Color.red);
        ViewBox bounds = context.getSpriteBounds();
        graphics.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public Shape getShape(Sprite sprite, Scene view) {
        return view.toViewBox(sprite).toRectangle();
    }

}
