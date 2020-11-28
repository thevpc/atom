/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.debug;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DebugVelocityLayer extends FlatBoardLayer {

    private BasicStroke simpleStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private Font verdana8 = new Font("verdana", Font.PLAIN, 8);

    public DebugVelocityLayer() {
        setLayer(SCREEN_FRONTEND_LAYER);
    }

    public DebugVelocityLayer(int zIndex) {
        this();
        setLayer(zIndex);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();

        graphics.setColor(Color.WHITE);
        graphics.setFont(verdana8);
        for (Sprite sprite : scene.getSceneEngine().getSprites()) {
            graphics.setColor(Color.WHITE);
            Stroke stroke = graphics.getStroke();
            ModelBox rectangle = sprite.getBounds();
            ModelPoint start = rectangle.getCenter();
            double speed = sprite.getSpeed();
            double dir = sprite.getDirection();
            double t = dir / Math.PI;
            ModelVector v = ModelVector.newAngular(speed, dir);
            ModelPoint end = new ModelPoint(start.getX() + v.getX() * 5, start.getY() + v.getY() * 5);

            ModelBox nextRectangle = new ModelBox(rectangle.getX() + v.getX(), rectangle.getY() + v.getY(), rectangle.getWidth(), rectangle.getHeight());
            ViewBox rectangleView = scene.toViewBox(rectangle);
            ViewBox nextRectangleView = scene.toViewBox(nextRectangle);
            ViewPoint p1 = scene.toViewPoint(start);
            ViewPoint p2 = scene.toViewPoint(end);
            graphics.setStroke(simpleStroke);
            graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            graphics.setStroke(stroke);
            graphics.drawString(speed + " * " + String.valueOf(t) + " PI", p1.getX(), p1.getY() - 20);

            graphics.setColor(Color.WHITE);
            graphics.drawRect(rectangleView.getX(), rectangleView.getY(), rectangleView.getWidth(), rectangleView.getHeight());
            graphics.setColor(Color.RED);
            graphics.drawRect(nextRectangleView.getX(), nextRectangleView.getY(), nextRectangleView.getWidth(), nextRectangleView.getHeight());
        }
    }
}
