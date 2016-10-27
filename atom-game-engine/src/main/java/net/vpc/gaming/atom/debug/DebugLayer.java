/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.debug;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.engine.tasks.MotionSpriteTask;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneMouseEvent;
import net.vpc.gaming.atom.presentation.layers.BordersScrollLayer;
import net.vpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.vpc.gaming.atom.presentation.layers.Layer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DebugLayer extends FlatBoardLayer {
    public static final int SHOW_GRID = 1;
    public static final int SHOW_WALL = 2;
    public static final int SHOW_CLICK = 4;
    public static final int SHOW_SPRITE_TILES = 8;
    public static final int SHOW_SPRITE_BOUNDS = 16;
    public static final int SHOW_SCROLL_BORDERS = 32;
    public static final int SHOW_PATH = 64;
    public static final int SHOW_ALL = SHOW_GRID | SHOW_WALL | SHOW_CLICK | SHOW_SPRITE_TILES | SHOW_SPRITE_BOUNDS | SHOW_SCROLL_BORDERS|SHOW_PATH;
    private int flags;
    private Tile clickTile;
    private DecimalFormat decimalFormat=new DecimalFormat("0.00");
    private BasicStroke dashStroke = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1f, new float[]{8f, 16f}, 0);
    private BasicStroke simpleStroke = new BasicStroke(5);
    
    public DebugLayer(boolean enabled) {
        this(SHOW_ALL);
        setEnabled(enabled);
    }
    
    public DebugLayer() {
        this(SHOW_ALL);
    }

    public DebugLayer(int flags) {
        setLayer(Layer.SKY_LAYER);
        setFlags(flags);
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();
        //SceneEngine sceneEngine = scene.getSceneEngine();
        //Color d = Color.BLUE.brighter();

        final Collection<Tile> visibleTiles = scene.findDisplayTiles();
        List<Sprite> displaySprites = scene.findDisplaySprites();

        if ((flags & SHOW_GRID) != 0) {
            for (Tile tile : visibleTiles) {
                final ViewBox r = new ViewBox(scene.toViewBox(tile.getBounds()));
                graphics.setColor(Color.GRAY);
                graphics.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            }
        }

        if ((flags & SHOW_WALL) != 0) {
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

        if((flags & SHOW_SPRITE_TILES) != 0){
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

        if((flags & SHOW_SPRITE_BOUNDS) != 0){
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
                graphics.drawString("x=" + decimalFormat.format(rect.getX()) + ",y=" + decimalFormat.format(rect.getY()), (int) (shapeBounds.getX()), (int) (shapeBounds.getY()));
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

        if(clickTile!=null){
            final ViewBox r = new ViewBox(scene.toViewBox(clickTile.getBounds()));
            graphics.setColor(Color.RED);
            graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }

        if((flags & SHOW_SCROLL_BORDERS) != 0){
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

        if((flags & SHOW_PATH) != 0){
            Stroke stroke = graphics.getStroke();
            for (Sprite sprite : displaySprites) {
                ModelBox rectangle = sprite.getBounds();
                SpriteTask task = context.getSceneEngine().getSpriteTask(sprite);
                if (task != null && task instanceof MotionSpriteTask) {
                    ModelPoint[] points = ((MotionSpriteTask) task).getMovePath();
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

    @Override
    public SceneMouseEvent mouseClicked(MouseEvent e) {
        if ((flags & SHOW_CLICK) != 0) {
            ViewPoint vp2 = null;
            vp2 = getScene().toIsometricViewPoint(new ViewPoint(e.getX(), e.getY()));
            clickTile = getScene().findTile(vp2);
        }else{
            clickTile=null;
        }
        return null;
    }
}
