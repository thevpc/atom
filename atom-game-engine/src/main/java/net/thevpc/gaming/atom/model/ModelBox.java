/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ModelBox extends Box {

    /**
     * The bitmask that indicates that a point lies to the left of this
     * <code>Rectangle2D</code>.
     *
     * @since 1.2
     */
    public static final int OUT_LEFT = 1;
    /**
     * The bitmask that indicates that a point lies above this
     * <code>Rectangle2D</code>.
     *
     * @since 1.2
     */
    public static final int OUT_TOP = 2;
    /**
     * The bitmask that indicates that a point lies to the right of this
     * <code>Rectangle2D</code>.
     *
     * @since 1.2
     */
    public static final int OUT_RIGHT = 4;
    /**
     * The bitmask that indicates that a point lies below this
     * <code>Rectangle2D</code>.
     *
     * @since 1.2
     */
    public static final int OUT_BOTTOM = 8;
    /**
     * The X coordinate of this
     * <code>Rectangle2D</code>.
     *
     * @serial
     * @since 1.2
     */
    private double x;
    /**
     * The Y coordinate of this
     * <code>Rectangle2D</code>.
     *
     * @serial
     * @since 1.2
     */
    private double y;
    private double z;
    /**
     * The width of this
     * <code>Rectangle2D</code>.
     *
     * @serial
     * @since 1.2
     */
    private double width;
    /**
     * The height of this
     * <code>Rectangle2D</code>.
     *
     * @serial
     * @since 1.2
     */
    private double height;
    private double altitude;

    public ModelBox(ModelPoint p, ModelDimension d) {
        this(p.getX(), p.getY(), p.getZ(), d.getWidth(), d.getHeight(), d.getAltitude());
    }

    public ModelBox(double x, double y, double width, double height) {
        this(x, y, 0, width, height, 1);
    }

    public ModelBox(double x, double y, double z, double width, double height, double altitude) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.altitude = altitude;
    }

    public boolean isEmpty() {
        return (width <= 0.0) || (height <= 0.0);
    }

    public ModelBox intersect(ModelBox src2) {
        double x1 = Math.max(this.getMinX(), src2.getMinX());
        double y1 = Math.max(this.getMinY(), src2.getMinY());
        double z1 = Math.max(this.getMinZ(), src2.getMinZ());
        double x2 = Math.min(this.getMaxX(), src2.getMaxX());
        double y2 = Math.min(this.getMaxY(), src2.getMaxY());
        double z2 = Math.min(this.getMaxZ(), src2.getMaxZ());
        double xw = x2 - x1;
        double yw = y2 - y1;
        double zw = z2 - z1;
        if(xw<0 || yw<0 || zw<=0){
            if(xw<=0){
                xw=0;
            }
            if(yw<=0){
                yw=0;
            }
            if(zw<=0){
                zw=0;
            }
//            System.out.println("Why....");
        }
        return new ModelBox(x1, y1, z1, xw, yw, zw);
    }

    public ModelBox unionBox(ModelBox src2) {
        double x1 = Math.min(this.getMinX(), src2.getMinX());
        double y1 = Math.min(this.getMinY(), src2.getMinY());
        double z1 = Math.min(this.getMinZ(), src2.getMinZ());
        double x2 = Math.max(this.getMaxX(), src2.getMaxX());
        double y2 = Math.max(this.getMaxY(), src2.getMaxY());
        double z2 = Math.max(this.getMaxZ(), src2.getMaxZ());
        return new ModelBox(x1, y1, z1, x2 - x1, y2 - y1, z2 - z1);
    }

    public boolean inside(int x, int y) {
        double w = this.width;
        double h = this.height;
        if (w < 0 || y < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        double x0 = this.x;
        double y0 = this.y;
        if (x < x0 || y < y0) {
            return false;
        }
        w += x0;
        h += y0;
        //    overflow || intersect
        return ((w < x0 || w > x)
                && (h < y0 || h > y));
    }

    public double getZ() {
        return z;
    }

    public double getAltitude() {
        return altitude;
    }

    /**
     * Returns the smallest X coordinate of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the smallest X coordinate of the framing rectangle of *      * the <code>Shape</code>.
     * @since 1.2
     */
    public double getMinX() {
        return getX();
    }

    /**
     * Returns the smallest Y coordinate of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the smallest Y coordinate of the framing rectangle of *      * the <code>Shape</code>.
     * @since 1.2
     */
    public double getMinY() {
        return getY();
    }

    public double getMinZ() {
        return getZ();
    }

    /**
     * Returns the largest X coordinate of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the largest X coordinate of the framing rectangle of *      * the <code>Shape</code>.
     * @since 1.2
     */
    public double getMaxX() {
        return getX() + getWidth();
    }

    /**
     * Returns the largest Y coordinate of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the largest Y coordinate of the framing rectangle of *      * the <code>Shape</code>.
     * @since 1.2
     */
    public double getMaxY() {
        return getY() + getHeight();
    }

    public double getMaxZ() {
        return getZ() + getAltitude();
    }

    /**
     * Returns the X coordinate of the center of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the X coordinate of the center of the framing rectangle of *      * the <code>Shape</code>.
     * @since 1.2
     */
    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    /**
     * Returns the Y coordinate of the center of the framing rectangle of the
     * <code>Shape</code> in
     * <code>double</code> precision.
     *
     * @return the Y coordinate of the center of the framing rectangle of *      * the <code>Shape</code>.
     * @since 1.2
     */
    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    public double getCenterZ() {
        return getZ() + getAltitude() / 2.0;
    }

    public ModelPoint getCenter() {
        return new ModelPoint(getCenterX(), getCenterY(), getCenterZ());
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelBox other = (ModelBox) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
        return hash;
    }

    public boolean intersectsLine(ModelSegment line) {
        int out1, out2;
        ModelPoint a = line.getA();
        ModelPoint b = line.getB();
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();
        if ((out2 = outcode(b)) == 0) {
            return true;
        }
        while ((out1 = outcode(line.getA())) != 0) {
            if ((out1 & out2) != 0) {
                return false;
            }
            if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
                double x = getX();
                if ((out1 & OUT_RIGHT) != 0) {
                    x += getWidth();
                }
                y1 = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                x1 = x;
            } else {
                double y = getY();
                if ((out1 & OUT_BOTTOM) != 0) {
                    y += getHeight();
                }
                x1 = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                y1 = y;
            }
        }
        return true;
    }

    public int outcode(ModelPoint p) {
        double x2 = p.getX();
        double y2 = p.getY();
        int out = 0;
        if (this.width <= 0) {
            out |= OUT_LEFT | OUT_RIGHT;
        } else if (x2 < this.x) {
            out |= OUT_LEFT;
        } else if (x2 > this.x + this.width) {
            out |= OUT_RIGHT;
        }
        if (this.height <= 0) {
            out |= OUT_TOP | OUT_BOTTOM;
        } else if (y2 < this.y) {
            out |= OUT_TOP;
        } else if (y2 > this.y + this.height) {
            out |= OUT_BOTTOM;
        }
        return out;
    }

    public boolean contains(ModelPoint p) {
        double x2 = p.getX();
        double y2 = p.getY();
        double x0 = getX();
        double y0 = getY();
        return (x2 >= x0
                && y2 >= y0
                && x2 < x0 + getWidth()
                && y2 < y0 + getHeight());
    }

    public boolean borderContains(ModelPoint p) {
        double x2 = p.getX();
        double y2 = p.getY();
        double x0 = getX();
        double y0 = getY();
        double w = getWidth();
        double h = getHeight();
        return (x2 >= x0 && x2 < x0 + w && (y2 == y0 || y2 == y0 + h))
                || ((x2 == x0 || x2 == x0 + w) && y2 >= y0 && y2 < y0 + h);
    }

    public boolean intersects(ModelBox r) {
        double x2 = r.getX();
        double y2 = r.getY();
        double w2 = r.getWidth();
        double h2 = r.getHeight();
        if (isEmpty() || w2 <= 0 || h2 <= 0) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x2 + w2 > x0
                && y2 + h2 > y0
                && x2 < x0 + getWidth()
                && y2 < y0 + getHeight());
    }

    public boolean contains(ModelBox r) {
        double x2 = r.getX();
        double y2 = r.getY();
        double w2 = r.getWidth();
        double h2 = r.getHeight();
        if (isEmpty() || w2 <= 0 || h2 <= 0) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x2 >= x0
                && y2 >= y0
                && (x2 + w2) <= x0 + getWidth()
                && (y2 + h2) <= y0 + getHeight());
    }

    public double distance(ModelBox b) {
        ModelBox a = this;
        if (a.intersects(b)) {
            return 0;
        }
        double d = Double.POSITIVE_INFINITY;
        ModelSegment[] ap = getSegments();
        ModelSegment[] bp = b.getSegments();
        for (ModelSegment ai : ap) {
            for (ModelSegment bi : bp) {
                double di = ai.distance(bi);
                if (di < d) {
                    d = di;
                }
            }
        }
        return d;
    }

    public double distance(ModelPoint b) {
        double d = Double.POSITIVE_INFINITY;
        for (ModelSegment s : getSegments()) {
            double v = s.distance(b);
            if (v < d) {
                d = v;
            }
        }
        return d;
    }

    public ModelDimension getSize() {
        return new ModelDimension(width, height, altitude);
    }

    public ModelPoint getLocation() {
        return new ModelPoint(x, y, z);
    }

    public ViewBox toViewBox() {
        return new ViewBox((int) x, (int) y, (int) width, (int) height);
    }

    public Rectangle toIRectangle() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }

    public Rectangle.Double toRectangleDouble() {
        return new Rectangle.Double(x, y, width, height);
    }

    @Override
    public String toString() {
        return "[" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ']';
    }

    public java.awt.Point[] getAWTPoints() {
        int minX = (int) getMinX();
        int maxX = (int) getMaxX();
        int minY = (int) getMinY();
        int maxY = (int) getMaxY();
        return new java.awt.Point[]{
                new java.awt.Point(minX, minY),
                new java.awt.Point(maxX, minY),
                new java.awt.Point(maxX, maxY),
                new java.awt.Point(minX, maxY),};
    }

    public java.awt.Point.Double[] getAWTPointsDouble() {
        double minX = getMinX();
        double maxX = getMaxX();
        double minY = getMinY();
        double maxY = getMaxY();
        return new java.awt.Point.Double[]{
                new java.awt.Point.Double(minX, minY),
                new java.awt.Point.Double(maxX, minY),
                new java.awt.Point.Double(maxX, maxY),
                new java.awt.Point.Double(minX, maxY),};
    }

    public java.awt.Point.Float[] getAWTPointsFloat() {
        float minX = (float) getMinX();
        float maxX = (float) getMaxX();
        float minY = (float) getMinY();
        float maxY = (float) getMaxY();
        return new java.awt.Point.Float[]{
                new java.awt.Point.Float(minX, minY),
                new java.awt.Point.Float(maxX, minY),
                new java.awt.Point.Float(maxX, maxY),
                new java.awt.Point.Float(minX, maxY),};
    }

    public ModelPoint[] getModelPoints() {
        return new ModelPoint[]{
                new ModelPoint(getMinX(), getMinY(), getMinZ()),
                new ModelPoint(getMaxX(), getMinY(), getMinZ()),
                new ModelPoint(getMaxX(), getMaxY(), getMinZ()),
                new ModelPoint(getMinX(), getMaxY(), getMinZ())
        };
    }

    //    public ModelPoint[] getModelPoints() {
//        double minX = getMinX();
//        double maxX = getMaxX();
//        double minY = getMinY();
//        double maxY = getMaxY();
//        return new ModelPoint[]{
//            new ModelPoint(minX, minY),
//            new ModelPoint(maxX, minY),
//            new ModelPoint(maxX, maxY),
//            new ModelPoint(minX, maxY),
//        };
//    }
    public ViewPoint[] getViewPoints() {
        int minX = (int) getMinX();
        int maxX = (int) getMaxX();
        int minY = (int) getMinY();
        int maxY = (int) getMaxY();
        return new ViewPoint[]{
                new ViewPoint(minX, minY),
                new ViewPoint(maxX, minY),
                new ViewPoint(maxX, maxY),
                new ViewPoint(minX, maxY),};
    }

    public ModelSegment[] getSegments() {
        ModelPoint[] p = getModelPoints();
        return new ModelSegment[]{
                new ModelSegment(p[0], p[1]),
                new ModelSegment(p[1], p[2]),
                new ModelSegment(p[2], p[3]),
                new ModelSegment(p[3], p[0])
        };
    }

    public ModelBox getDimensionBox() {
        return new ModelBox(0, 0, 0,width, height,altitude);
    }

    public ModelDimension getDimension() {
        return new ModelDimension(width, height,altitude);
    }

    public ModelBox concat(ModelBox other) {
        double minx = Math.min(getMinX(), other.getMinX());
        double maxx = Math.max(getMaxX(), other.getMaxX());
        double miny = Math.min(getMinY(), other.getMinY());
        double maxy = Math.max(getMaxY(), other.getMaxY());
        return new ModelBox(
                minx, miny, (maxx - minx), (maxy - miny)
        );
    }
}
