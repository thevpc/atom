/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.awt.*;
import java.io.Serializable;

/**
 * @author vpc
 */
public class ViewBox extends Box {

    private int x;
    private int y;
    private int z;
    private int width;
    private int height;
    private int altitude;

    public ViewBox(Rectangle box) {
        this(box.x, box.y, box.width, box.height);
    }

    public ViewBox(ViewPoint p, ViewDimension d) {
        this(p.getX(), p.getY(), d.getWidth(), d.getHeight());
    }

    public ViewBox(ViewBox box) {
        this(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    }

    public ViewBox(int x, int y, int width, int height) {
        this(x, y, 0, width, height, 1);
    }

    public ViewBox(int x, int y, int z, int width, int height, int altitude) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.altitude = altitude;
    }

    public ViewDimension getSize() {
        return new ViewDimension(width, height);
    }

    public ViewBox getDimensionBox() {
        return new ViewBox(0, 0, width, height);
    }

    public java.awt.Point[] getAWTPoints() {
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        return new java.awt.Point[]{
                new java.awt.Point(minX, minY),
                new java.awt.Point(maxX, minY),
                new java.awt.Point(maxX, maxY),
                new java.awt.Point(minX, maxY),};
    }

    public java.awt.Point.Double[] getAWTPointsDouble() {
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        return new java.awt.Point.Double[]{
                new java.awt.Point.Double(minX, minY),
                new java.awt.Point.Double(maxX, minY),
                new java.awt.Point.Double(maxX, maxY),
                new java.awt.Point.Double(minX, maxY),};
    }

    public java.awt.Point.Float[] getAWTPointsFloat() {
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        return new java.awt.Point.Float[]{
                new java.awt.Point.Float(minX, minY),
                new java.awt.Point.Float(maxX, minY),
                new java.awt.Point.Float(maxX, maxY),
                new java.awt.Point.Float(minX, maxY),};
    }

    public ModelPoint[] getModelPoints() {
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        return new ModelPoint[]{
                new ModelPoint(minX, minY),
                new ModelPoint(maxX, minY),
                new ModelPoint(maxX, maxY),
                new ModelPoint(minX, maxY),};
    }

    public ViewPoint[] getViewPoints() {
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        return new ViewPoint[]{
                new ViewPoint(minX, minY),
                new ViewPoint(maxX, minY),
                new ViewPoint(maxX, maxY),
                new ViewPoint(minX, maxY),};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getMinX() {
        return x;
    }

    public int getMinY() {
        return y;
    }

    public int getMinZ() {
        return z;
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMaxY() {
        return y + height;
    }

    public int getMaxZ() {
        return z + altitude;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAltitude() {
        return altitude;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        hash = 97 * hash + this.z;
        hash = 97 * hash + this.width;
        hash = 97 * hash + this.height;
        hash = 97 * hash + this.altitude;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ViewBox other = (ViewBox) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (this.altitude != other.altitude) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ViewBox{" + "x=" + x + ", y=" + y + ", z=" + z + ", width=" + width + ", height=" + height + ", altitude=" + altitude + '}';
    }

    public ViewPoint getLocation() {
        return new ViewPoint(x, y, z);
    }

    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    /**
     * Returns the Y coordinate of the center of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the Y coordinate of the center of the framing rectangle of
     *         the <code>Shape</code>.
     * @since 1.2
     */
    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    public double getCenterZ() {
        return getZ() + getAltitude() / 2.0;
    }

    public Rectangle toRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle.Double toRectangleDouble() {
        return new Rectangle.Double(x, y, width, height);
    }

    public boolean contains(ViewPoint p) {
        int x2 = p.getX();
        int y2 = p.getY();
        int x0 = getX();
        int y0 = getY();
        return (x2 >= x0
                && y2 >= y0
                && x2 < x0 + getWidth()
                && y2 < y0 + getHeight());
    }

    public boolean intersects(ViewBox r) {
        int x2 = r.getX();
        int y2 = r.getY();
        int w2 = r.getWidth();
        int h2 = r.getHeight();
        if (isEmpty() || w2 <= 0 || h2 <= 0) {
            return false;
        }
        int x0 = getX();
        int y0 = getY();
        return (x2 + w2 > x0
                && y2 + h2 > y0
                && x2 < x0 + getWidth()
                && y2 < y0 + getHeight());
    }

    public boolean isEmpty() {
        return (width <= 0) || (height <= 0);
    }
}
