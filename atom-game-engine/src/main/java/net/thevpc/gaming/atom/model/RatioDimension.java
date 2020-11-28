/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.io.Serializable;

/**
 * @author vpc
 */
public class RatioDimension extends Dimension {

    private float width;
    private float height;
    private float altitude;

    public RatioDimension(float width) {
        this(width,width,1);
    }

    public RatioDimension(float width, float height) {
        this(width,height,1);
    }

    public RatioDimension(float width, float height, float altitude) {
        this.width = width<=0?1:width;
        this.height = height<=0?this.width:height;
        this.altitude = altitude<=0?1:altitude;
        if (this.width < 0 || this.width > 1) {
            throw new IllegalArgumentException("Invalid relative value");
        }
        if (this.height < 0 || this.height > 1) {
            throw new IllegalArgumentException("Invalid relative value");
        }
        if (this.altitude < 0 || this.altitude > 1) {
            throw new IllegalArgumentException("Invalid relative value");
        }
    }

    private float checkVal(float f) {
        if (f < 0 || f > 1) {
            throw new IllegalArgumentException("Invalid relative value " + f);
        }
        return f;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAltitude() {
        return altitude;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Float.floatToIntBits(this.width);
        hash = 19 * hash + Float.floatToIntBits(this.height);
        hash = 19 * hash + Float.floatToIntBits(this.altitude);
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
        final RatioDimension other = (RatioDimension) obj;
        if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width)) {
            return false;
        }
        if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height)) {
            return false;
        }
        if (Float.floatToIntBits(this.altitude) != Float.floatToIntBits(other.altitude)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ViewDimension{" + "width=" + width + ", height=" + height + ", altitude=" + altitude + '}';
    }
}
