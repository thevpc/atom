/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.io.Serializable;

/**
 * @author vpc
 */
public class ViewDimension extends Dimension {

    private int width;
    private int height;
    private int altitude;

    public ViewDimension(int width) {
        this(width, width, 1);
    }

    public ViewDimension(int width, int height) {
        this(width, height, 1);
    }

    public ViewDimension(int width, int height, int altitude) {
        this.width = width;
        this.height = height;
        this.altitude = altitude;
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
        int hash = 5;
        hash = 79 * hash + this.width;
        hash = 79 * hash + this.height;
        hash = 79 * hash + this.altitude;
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
        final ViewDimension other = (ViewDimension) obj;
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
        return "ViewDimension{" + "width=" + width + ", height=" + height + ", altitude=" + altitude + '}';
    }
}
