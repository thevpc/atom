/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.SceneModel;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneMouseEvent;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class BordersScrollLayer extends DefaultLayer implements InteractiveLayer {

    private Orientation direction = null;
    private double tileXStep = 1;
    private double tileYStep = 1;
    private int margin = 30;
    private boolean running = false;

    public BordersScrollLayer() {
        setLayer(SCREEN_FRONTEND_LAYER);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        //draw nothing    
    }

    @Override
    public void nextFrame() {
        if (isEnabled()) {
            doScroll();
        }
    }

    @Override
    public SceneMouseEvent mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        evalScroll(new ViewPoint(p.x, p.y, 0));
        return null;
    }

    @Override
    public SceneMouseEvent mouseEntered(MouseEvent e) {
        Point p = e.getPoint();
        evalScroll(new ViewPoint(p.x, p.y, 0));
        return null;
    }

    @Override
    public SceneMouseEvent mouseExited(MouseEvent e) {
        Point p = e.getPoint();
        evalScroll(new ViewPoint(p.x, p.y, 0));
        return null;
    }

    private Orientation getAutoScroll(ViewPoint point) {
        for (Orientation autoScroll : Orientation.values()) {
            ViewBox r = getScrollZone(autoScroll);
            if (r != null) {
                if (r.contains(point)) {
                    return autoScroll;
                }
            }
        }
        return null;
    }

    public ViewBox getScrollZone(Orientation scroll) {
        Scene view = getScene();
        if (view.isIsometric()) {
            switch (scroll) {
                case NORTH_WEST: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(0, margin, 0, margin, v.getHeight() - 2 * margin, 0);
                }
                case SOUTH_EAST: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(v.getWidth() - margin, margin, 0, margin, v.getHeight() - 2 * margin, 0);
                }
                case NORTH_EAST: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(margin, 0, 0, v.getWidth() - 2 * margin, margin, 0);
                }
                case SOUTH_WEST: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(margin, v.getHeight() - margin, 0, v.getWidth() - 2 * margin, margin, 0);
                }
            }
        } else {
            switch (scroll) {
                case WEST: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(0, margin, 0, margin, v.getHeight() - 2 * margin, 0);
                }
                case EAST: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(v.getWidth() - margin, margin, 0, margin, v.getHeight() - 2 * margin, 0);
                }
                case NORTH: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(margin, 0, 0, v.getWidth() - 2 * margin, margin, 0);
                }
                case SOUTH: {
                    ViewBox v = view.getAbsoluteCamera();
                    return new ViewBox(margin, v.getHeight() - margin, 0, v.getWidth() - 2 * margin, margin, 0);
                }
            }
        }
        return null;
    }

    private void doScroll() {
        Scene scene = getScene();
        Orientation dd = direction;
        if (dd == null) {
            return;
        }
        SceneModel m = scene.getModel();
        int w = (int) (tileXStep * m.getTileSize().getWidth());
        int h = (int) (tileYStep * m.getTileSize().getHeight());
        double dx = 0;
        double dy = 0;
        double scrollSpeed = 0.1;
        final double north = -1 * scrollSpeed;
        final double south = 1 * scrollSpeed;
        final double west = -1 * scrollSpeed;
        final double east = 1 * scrollSpeed;
        switch (dd) {
            case EAST: {
                dx = east;
                break;
            }
            case NORTH_EAST: {
                dx = east;
                dy = north;
                break;
            }
            case SOUTH_EAST: {
                dx = east;
                dy = south;
                break;
            }
            case WEST: {
                dx = west;
                break;
            }
            case NORTH_WEST: {
                dx = west;
                dy = north;
                break;
            }
            case SOUTH_WEST: {
                dx = west;
                dy = south;
                break;
            }
            case NORTH: {
                dy = north;
                break;
            }
            case SOUTH: {
                dy = south;
                break;
            }
        }
        //dy = -dy; // drawing axis is inverted (y>0 for south)
        if (!scene.moveAbsoluteCameraBy((int) (dx * w), (int) (dy * h))) {
            setScroll(null);
        }
    }

    public void evalScroll(ViewPoint point) {
        setScroll(getAutoScroll(point));
    }

    private void setScroll(Orientation scroll) {
        this.direction = scroll;
        if (scroll == null) {
            if (isRunning()) {
                stop();
            }
        } else {
            if (!isRunning()) {
                start();
            }
        }
    }

    private boolean isRunning() {
        return running;
    }

    private void start() {
        running = true;
    }

    private void stop() {
        running = false;
    }
}
