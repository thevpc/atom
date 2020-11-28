/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.io.Serializable;

/**
 * @author vpc
 */
public class RatioPoint extends Point {
    private float x;
    private float y;
    private float z;

    public RatioPoint(float x, float y) {
        this(x, y, 0);
    }

    public RatioPoint(float x, float y, float z) {
        this.x = checkVal(x);
        this.y = checkVal(y);
        this.z = checkVal(z);
    }

    private float checkVal(float f) {
        if (f < 0 || f > 1) {
            throw new IllegalArgumentException("Invalid relative value " + f);
        }
        return f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Float.floatToIntBits(this.x);
        hash = 59 * hash + Float.floatToIntBits(this.y);
        hash = 59 * hash + Float.floatToIntBits(this.z);
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
        final RatioPoint other = (RatioPoint) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RatioPoint{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

}
