/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.debug;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.Collision;
import net.thevpc.gaming.atom.engine.maintasks.MotionSpriteMainTask;
import net.thevpc.gaming.atom.extension.heatmap.HeatMapSceneExtension;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneMouseEvent;
import net.thevpc.gaming.atom.presentation.layers.BordersScrollLayer;
import net.thevpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.thevpc.gaming.atom.presentation.layers.Layer;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DebugLayer extends FlatBoardLayer {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final BasicStroke dashStroke = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1f, new float[]{8f, 16f}, 0);
    private final BasicStroke simpleStroke = new BasicStroke(5);
    private final BasicStroke simpleStrokeVelocity = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private final Font verdana8 = new Font("verdana", Font.PLAIN, 8);
    private boolean showGrid = true;
    private boolean showWall = true;
    private boolean showClick = true;
    private boolean showSpriteTiles = true;
    private boolean showSpriteBounds = true;
    private boolean showScrollBorders = true;
    private boolean showSpritePath = true;
    private boolean showSpriteVelocity = true;
    private boolean showHeatMap = true;
    private Tile clickTile;
    private Composite[] heatMapSceneExtensionCompositeArray;

    public DebugLayer(boolean enabled) {
        setEnabled(enabled);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();
        //SceneEngine sceneEngine = scene.getSceneEngine();
        //Color d = Color.BLUE.brighter();

        final Collection<Tile> visibleTiles = scene.findDisplayTiles();
        List<Sprite> displaySprites = scene.findDisplaySprites();

        if (isShowGrid()) {
            for (Tile tile : visibleTiles) {
                final ViewBox r = new ViewBox(scene.toViewBox(tile.getBounds()));
                graphics.setColor(Color.GRAY);
                graphics.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            }
        }

        if (isShowWall()) {
            drawWalls(graphics, scene, visibleTiles, displaySprites);
        }

        if (isShowSpriteTiles()) {
            drawSpriteTiles(graphics, scene, visibleTiles, displaySprites);
        }

        if (isShowSpriteBounds()) {
            drawSpriteBounds(context, graphics, scene, visibleTiles, displaySprites);
        }
        if (isShowSpriteVelocity()) {
            drawSpriteVelocity(context, graphics, scene, visibleTiles, displaySprites);
        }

        if (clickTile != null) {
            drawClickTile(context, graphics, scene, visibleTiles, displaySprites);
        }

        if (isShowScrollBorders()) {
            drawScrollBorders(context, graphics, scene, visibleTiles, displaySprites);
        }

        if (isShowSpritePath()) {
            drawSpritePath(context, graphics, scene, visibleTiles, displaySprites);

        }
        if (isShowHeatMap()) {
            drawHeatMap(context, graphics, scene, visibleTiles, displaySprites);

        }

//        for (Sprite sp : sceneEngine.getSprites()) {
//            graphics.setColor(Color.BLUE);
//            final ViewBox r0 = scene.toViewBox(sp);
//            graphics.drawRect(r0.getX() + 1, r0.getY() + 1, r0.getWidth() - 2, r0.getHeight() - 2);
//            graphics.setColor(d);
//            final Collection<Tile> spCells = sceneEngine.findTiles(sp.getBounds());
//            for (Tile cell : spCells) {
//                final ViewBox r = scene.toViewBox(cell);
//                graphics.drawRect(r.getX() + 4, r.getY() + 4, r.getWidth() - 8, r.getHeight() - 8);
//            }
//        }
//        graphics.setColor(Color.BLUE);
//        Stroke oldStroke = graphics.getStroke();
//        graphics.setStroke(simpleStroke);
//        graphics.draw(scene.getPolygonAbsoluteCamera());
//        graphics.setStroke(oldStroke);
    }

    private void drawHeatMap(LayerDrawingContext context, Graphics2D graphics, Scene scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        SceneEngine sceneEngine = scene.getSceneEngine();
        Player player = scene.getControlPlayer();
        HeatMapSceneExtension heatMapSceneExtension = sceneEngine.getExtension(HeatMapSceneExtension.class);
        if(heatMapSceneExtension==null){
            return;
        }
        double[][] hm = heatMapSceneExtension.getHeatMap(player);
        if (hm != null) {
            double max = 0;
            for (double[] ds : hm) {
                for (double d : ds) {
                    if (!Double.isInfinite(d)) {
                        if (d > max) {
                            max = d;
                        }
                    }
                }
            }
            if (heatMapSceneExtensionCompositeArray == null || heatMapSceneExtensionCompositeArray.length != 4) {
                heatMapSceneExtensionCompositeArray = new Composite[4];
                for (int i = 0; i < heatMapSceneExtensionCompositeArray.length; i++) {
                    heatMapSceneExtensionCompositeArray[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (i + 1F) / heatMapSceneExtensionCompositeArray.length);
                }
            }
            Composite composite = graphics.getComposite();
            graphics.setColor(Color.RED);
            for (Tile tile : visibleTiles) {
                final ViewBox r = scene.toViewBox(tile.getBounds());
                int col = tile.getColumn();
                int row = tile.getRow();
                double v = hm[row][col];
                if (v > 0) {
                    if (Double.isInfinite(v)) {
                        graphics.setComposite(heatMapSceneExtensionCompositeArray[heatMapSceneExtensionCompositeArray.length - 1]);
                        graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                    } else {
                        double part = max / heatMapSceneExtensionCompositeArray.length;
                        int ii = (int) (v / part);
                        if (ii >= heatMapSceneExtensionCompositeArray.length) {
                            ii = heatMapSceneExtensionCompositeArray.length - 1;
                        }
                        graphics.setComposite(heatMapSceneExtensionCompositeArray[ii]);
                        graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                    }
                }
            }
            graphics.setComposite(composite);
        }
    }

    private void drawSpriteVelocity(LayerDrawingContext context, Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(verdana8);
        for (Sprite sprite : displaySprites) {
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
            graphics.setStroke(simpleStrokeVelocity);
            graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            graphics.setStroke(stroke);
            graphics.drawString(speed + " * " + t + " PI", p1.getX(), p1.getY() - 20);

            graphics.setColor(Color.WHITE);
            graphics.drawRect(rectangleView.getX(), rectangleView.getY(), rectangleView.getWidth(), rectangleView.getHeight());
            graphics.setColor(Color.RED);
            graphics.drawRect(nextRectangleView.getX(), nextRectangleView.getY(), nextRectangleView.getWidth(), nextRectangleView.getHeight());
        }
    }

    private void drawSpritePath(LayerDrawingContext context, Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        Stroke stroke = graphics.getStroke();
        for (Sprite sprite : displaySprites) {
            ModelBox rectangle = sprite.getBounds();
            SpriteMainTask task = context.getSceneEngine().getSpriteMainTask(sprite);
            if (task != null && task instanceof MotionSpriteMainTask) {
                ModelPoint[] points = ((MotionSpriteMainTask) task).getMovePath();
                if (points != null && points.length > 1) {
//                    g2d.setColor(Color.ORANGE);
                    int c = points.length;
                    int[] x = new int[c];
                    int[] y = new int[c];
                    for (int i = 0; i < y.length; i++) {
                        //path visualized from CENTER point
                        ViewPoint p = scene.toViewPoint(new ModelPoint(points[i].getX() + rectangle.getWidth() / 2.0, points[i].getY() + rectangle.getHeight() / 2.0));
                        x[i] = p.getX();
                        y[i] = p.getY();
                    }
                    graphics.setColor(Color.WHITE);
                    graphics.setStroke(simpleStroke);
                    graphics.drawPolyline(x, y, c);
                    graphics.setColor(Color.ORANGE);
                    graphics.setStroke(dashStroke);
                    graphics.drawPolyline(x, y, c);
                }
            }
        }
        graphics.setStroke(stroke);
    }

    private void drawScrollBorders(LayerDrawingContext context, Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        for (Layer gameLayer : scene.getLayers()) {
            if (gameLayer instanceof BordersScrollLayer) {
                BordersScrollLayer s = (BordersScrollLayer) gameLayer;
                for (Orientation autoScroll : Orientation.values()) {
                    ViewBox rect = s.getScrollZone(autoScroll);
                    if (rect != null) {
                        graphics.setColor(Color.CYAN);

                        // setTransparentBounce
                        Composite c = graphics.getComposite();
                        int M = 50;
                        long time = (int) ((getScene().getFrame() / 1) % M);
                        float transparency = time < (M / 2) ? ((float) time / M) : ((float) (M - time) / M);
                        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

                        //dessiner carre
                        graphics.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                        // unsetTransparentBounce
                        graphics.setComposite(c);
                    }
                }
                return;
            }
        }
    }

    private void drawClickTile(LayerDrawingContext context, Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        final ViewBox r = new ViewBox(scene.toViewBox(clickTile.getBounds()));
        graphics.setColor(Color.RED);
        graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    private void drawSpriteBounds(LayerDrawingContext context, Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        AffineTransform oldTransform = graphics.getTransform();
        graphics.setTransform(context.getScreenTransform());
        for (Sprite sprite : displaySprites) {
            graphics.setColor(Color.YELLOW);
            Color d2 = Color.YELLOW.brighter();
            ModelBox rect = sprite.getBounds();
            Rectangle shapeBounds = scene.getSpriteView(sprite).getShape(sprite, scene).getBounds();
//            final Rectangle r0 = screen.toView(rect);
//            ViewBox zRect = new ViewBox(shapeBounds);
            graphics.draw(shapeBounds);
            String collisionSides = String.valueOf(sprite.getCollisionSides());
            if (collisionSides.length() > 0) {
                collisionSides = ",collisionSides=" + collisionSides;
            }
            graphics.drawString("x=" + decimalFormat.format(rect.getX()) + ",y=" + decimalFormat.format(rect.getY()) + collisionSides, (int) (shapeBounds.getX()), (int) (shapeBounds.getY()));
        }

        graphics.setTransform(context.getBoardTransform());
        for (Sprite sprite : displaySprites) {
            graphics.setColor(Color.RED);
            Color d2 = Color.RED.darker();
            ModelBox rect = sprite.getBounds();
            Rectangle shapeBounds = scene.getSpriteView(sprite).getShape(sprite, scene).getBounds();
//            final Rectangle r0 = screen.toView(rect);
//            ViewBox zRect = new ViewBox(shapeBounds);
            graphics.draw(shapeBounds);
            graphics.drawString("x=" + decimalFormat.format(rect.getX()) + ",y=" + decimalFormat.format(rect.getY()), (int) (shapeBounds.getX()), (int) (shapeBounds.getY()));
        }
        graphics.setTransform(oldTransform);
    }

    private void drawSpriteTiles(Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        Color d = Color.BLUE.brighter();
        for (Sprite sp : displaySprites) {
            graphics.setColor(Color.BLUE);
            final ViewBox r0 = scene.toViewBox(sp);
            graphics.drawRect(r0.getX() + 1, r0.getY() + 1, r0.getWidth() - 2, r0.getHeight() - 2);
            graphics.setColor(d);
            final Collection<Tile> spCells = scene.findTiles(sp.getBounds());
            for (Tile cell : spCells) {
                final ViewBox r = scene.toViewBox(cell);
                graphics.drawRect(r.getX() + 4, r.getY() + 4, r.getWidth() - 8, r.getHeight() - 8);
            }
        }
    }

    private void drawWalls(Graphics2D graphics, Scene
            scene, Collection<Tile> visibleTiles, List<Sprite> displaySprites) {
        for (Tile cell : visibleTiles) {
            final ViewBox r = scene.toViewBox(cell.getBounds());
            int type = cell.getWalls();
            if (type != 0) {
                graphics.setColor(Color.RED);
                int x1 = r.getX() + 2;
                int y1 = r.getY() + 2;
                int x2 = r.getX() + r.getWidth() - 4;
                int y2 = r.getY() + r.getHeight() - 4;

                if ((type & Tile.WALL_NORTH) != 0) {
                    graphics.drawLine(x1, y1, x2, y1);
                }
                if ((type & Tile.WALL_EAST) != 0) {
                    graphics.drawLine(x2, y1, x2, y2);
                }
                if ((type & Tile.WALL_SOUTH) != 0) {
                    graphics.drawLine(x2, y2, x1, y2);
                }
                if ((type & Tile.WALL_WEST) != 0) {
                    graphics.drawLine(x1, y2, x1, y1);
                }
            }
            graphics.setColor(Color.RED);
            int x1 = r.getX() + 2;
            int y1 = r.getY() + 15;
            int[] z = cell.getZ();
            String zstr = "" + z[0] + z[1] + z[2] + z[3];
            if (!zstr.equals("0000")) {
                graphics.drawString(zstr, x1, y1);
            }
        }
    }

    @Override
    public SceneMouseEvent mouseClicked(MouseEvent e) {
        if (isShowClick()) {
            ViewPoint vp2 = null;
            vp2 = getScene().toIsometricViewPoint(new ViewPoint(e.getX(), e.getY()));
            clickTile = getScene().findTile(vp2);
        } else {
            clickTile = null;
        }
        return null;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public DebugLayer setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        return this;
    }

    public boolean isShowWall() {
        return showWall;
    }

    public DebugLayer setShowWall(boolean showWall) {
        this.showWall = showWall;
        return this;
    }

    public boolean isShowClick() {
        return showClick;
    }

    public DebugLayer setShowClick(boolean showClick) {
        this.showClick = showClick;
        if (!showClick) {
            clickTile = null;
        }
        return this;
    }

    public boolean isShowSpriteTiles() {
        return showSpriteTiles;
    }

    public DebugLayer setShowSpriteTiles(boolean showSpriteTiles) {
        this.showSpriteTiles = showSpriteTiles;
        return this;
    }

    public boolean isShowSpriteBounds() {
        return showSpriteBounds;
    }

    public DebugLayer setShowSpriteBounds(boolean showSpriteBounds) {
        this.showSpriteBounds = showSpriteBounds;
        return this;
    }

    public boolean isShowScrollBorders() {
        return showScrollBorders;
    }

    public DebugLayer setShowScrollBorders(boolean showScrollBorders) {
        this.showScrollBorders = showScrollBorders;
        return this;
    }

    public boolean isShowSpritePath() {
        return showSpritePath;
    }

    public DebugLayer setShowSpritePath(boolean showSpritePath) {
        this.showSpritePath = showSpritePath;
        return this;
    }

    public boolean isShowSpriteVelocity() {
        return showSpriteVelocity;
    }

    public DebugLayer setShowSpriteVelocity(boolean showSpriteVelocity) {
        this.showSpriteVelocity = showSpriteVelocity;
        return this;
    }

    public boolean isShowHeatMap() {
        return showHeatMap;
    }

    public DebugLayer setShowHeatMap(boolean showHeatMap) {
        this.showHeatMap = showHeatMap;
        return this;
    }
}
