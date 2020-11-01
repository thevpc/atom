/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.components;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.extension.strategy.Entity;
import net.vpc.gaming.atom.presentation.DefaultSceneComponent;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Collection;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.model.ViewPoint;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MiniMap extends DefaultSceneComponent {
    private Color borderColor = Color.YELLOW;
    private Color backgroundColor = Color.BLACK;
    private Color cameraColor = Color.YELLOW.darker();

    public MiniMap(ViewDimension size) {
        this(null, size);
    }

    public MiniMap(String name) {
        this(name, new ViewDimension(100, 100));
    }

    public MiniMap(String name, ViewDimension size) {
        super(name);
        setSize(size);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public MiniMap setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public MiniMap setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Color getCameraColor() {
        return cameraColor;
    }

    public MiniMap setCameraColor(Color cameraColor) {
        this.cameraColor = cameraColor;
        return this;
    }

    @Override
    public void draw(LayerDrawingContext context) {
//        if(true){
//            Graphics2D graphics = context.getGraphics();
//            graphics.setTransform(context.getBoardTransform());
//            Scene scene = context.getScene();
//
//            final Collection<Tile> visibleCells = scene.findDisplayTiles();
//            for (Tile tile : scene.getSceneEngine().getTiles()) {
//                final ViewBox r = new ViewBox(scene.toViewBox(tile.getBounds()));
//                graphics.setColor(Color.GRAY);
//                graphics.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
//            }
//            graphics.setColor(Color.BLUE);
//            Stroke oldStroke = graphics.getStroke();
//            graphics.setStroke(new BasicStroke(20));
//            graphics.draw(scene.getPolygonAbsoluteCamera());
//            graphics.setStroke(oldStroke);
//            return;
//        }
        if (!isVisible()) {
            return;
        }
        Graphics2D g = context.getGraphics();
        Scene scene = getScene();
        SceneEngine sceneEngine = scene.getSceneEngine();
        ModelDimension sceneEngineSize = sceneEngine.getSize();
        int W = (int) sceneEngineSize.getWidth();
        int H = (int) sceneEngineSize.getHeight();
        int w = getSize().getWidth();
        int h = getSize().getHeight();
        ViewPoint position = getLocation();
        if (backgroundColor != null) {
            g.setColor(Color.BLACK);
            g.fillRoundRect(position.getX(), position.getY(), w - 1, h - 1, 5, 5);
        }
        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawRoundRect(position.getX(), position.getY(), w - 1, h - 1, 5, 5);
        }
        //paintSprites((Graphics2D) g);

        double wr = ((double) w) / W;
        double hr = ((double) h) / H;
        Collection<Sprite> sprites = sceneEngine.getSprites();

        for (Sprite sprite : sprites) {
            int p = sprite.getPlayerId();
            if (p >= 0 && sprite instanceof Entity) {
                Color c = context.getScene().getSceneEngine().getPlayer(p).getColor();
                if (c == null) {
                    c = Color.YELLOW;
                }
                final ViewPoint pp = sprite.getLocation().toViewPoint();
                g.setColor(c);
                ModelBox r = sprite.getBounds();
                int xx = (int) (r.getWidth() * wr);
                int yy = (int) (r.getHeight() * hr);
                if (xx < 2) {
                    xx = 2;
                }
                if (yy < 2) {
                    yy = 2;
                }
                g.fillOval(position.getX() + (int) (pp.getX() * wr) - 1, position.getY() + (int) (pp.getY() * hr) - 1, xx, yy);
            }
        }
        if (cameraColor != null) {
            ViewBox vp = scene.getCamera().getViewBounds();
            Path2D polygonCamera = scene.getCamera().getViewPolygon();
            ViewDimension m = scene.getModel().getTileSize();
            ViewPoint zeroPoint = scene.toViewPoint(new ModelPoint(0, 0));
            //AffineTransform s = AffineTransform.getScaleInstance();
            //zeroPoint.getX()
            //zeroPoint.getY()
//            AffineTransform s = AffineTransform.getTranslateInstance(position.getX(), position.getY());
//            s.concatenate(AffineTransform.getScaleInstance(m.getWidth()/(double)vp.getWidth(), m.getHeight()/(double)vp.getHeight()));
            ModelBox rect = new ModelBox(
                    vp.getX() / m.getWidth(),
                    vp.getY() / m.getHeight(),
                    vp.getWidth() / m.getWidth(),
                    vp.getHeight() / m.getHeight());
            g.setColor(Color.YELLOW);
            int dx = (int) (rect.getX() * wr);
            int dy = (int) (rect.getY() * hr);
            int dw = (int) (rect.getWidth() * wr);
            int dh = (int) (rect.getHeight() * hr);
            g.drawRect(position.getX() + dx, position.getY() + dy, dw, dh);
//            AffineTransform t = g.getTransform();
//            AffineTransform t2 = (AffineTransform)t.clone();
//            t2.concatenate(s);
//            g.setTransform(s);
////            g.setColor(Color.BLUE);
//            Stroke oldStroke = g.getStroke();
//            g.setStroke(new BasicStroke(20));
//            g.drawRect(0, 0, 100, 100);
//            g.draw(polygonCamera);
//            g.setStroke(oldStroke);
            //g.setTransform(t);
        }

    }
}
