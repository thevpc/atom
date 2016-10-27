package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.SceneModel;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewPoint;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class LocationIndicatorView extends DefaultSpriteView {
    private Color color;

    public LocationIndicatorView() {
        this(Color.ORANGE);
    }


    public LocationIndicatorView(Color color) {
        this.color = color;
    }

    @Override
    public void draw(SpriteDrawingContext context) {
        Sprite sprite = context.getSprite();
        Scene scene = context.getScene();
        ViewPoint position = getLocation(sprite, scene);
        Graphics2D graphics = context.getGraphics();
        SceneModel m = scene.getModel();

        int tw = m.getTileSize().getWidth();
        int th = m.getTileSize().getHeight();
        int swidth = (int) (sprite.getWidth() * tw);
        int sheight = (int) (sprite.getHeight() * th);
        double cl = sprite.getLife();
        double ml = sprite.getMaxLife();


        int dw = (int) (swidth * cl / ml);
        int dh = (int) (sheight * cl / ml);
//        AffineTransform transform = graphics.getTransform();

        int cx = position.getX();
        int cy = position.getY();

        int ow = dw;
        int oh = dh;
        int ox = cx - ow / 2;
        int oy = cy - oh / 2;
//        graphics.setTransform(AffineTransform.getRotateInstance(Math.PI/4, ox+ow/2, oy+oh/2));
        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(3));
        graphics.setColor(color);
        graphics.drawOval(ox, oy, ow, oh);
//        graphics.draw(view.getView(sprite).getShape(sprite, view));
        graphics.setStroke(stroke);

//        graphics.drawOval(ox, oy, 1, 1);
//        graphics.setTransform(transform);

    }

    public Shape getShape(Sprite sprite, Scene view) {
        return view.toViewBox(sprite).toRectangle();
    }
}
