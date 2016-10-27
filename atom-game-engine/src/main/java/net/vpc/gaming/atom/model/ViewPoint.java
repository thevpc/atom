/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.awt.*;
import java.io.Serializable;

/**
 * @author vpc
 */
public class ViewPoint implements Serializable {

    private int x;
    private int y;
    private int z;

    public ViewPoint(int x, int y) {
        this(x, y, 0);
    }

    public ViewPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ViewPoint(Point point) {
        this(point.x, point.y, 0);
    }

    public Point toPoint() {
        return new Point(x, y);
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.x;
        hash = 83 * hash + this.y;
        hash = 83 * hash + this.z;
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
        final ViewPoint other = (ViewPoint) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ViewPoint{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
