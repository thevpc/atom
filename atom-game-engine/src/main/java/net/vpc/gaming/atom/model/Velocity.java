/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Velocity implements Serializable {

    private double theta;
    private double value;
    public static final Velocity STILL_UP = new Velocity(-Math.PI / 2, 0);
    public static final Velocity STILL_DOWN = new Velocity(Math.PI / 2, 0);
    public static final Velocity STILL_LEFT = new Velocity(Math.PI, 0);
    public static final Velocity STILL_RIGHT = new Velocity(0, 0);

    public Velocity(double angle, double value) {
        this.theta = angle;
        this.value = value;
    }

    public double getTheta() {
        return theta;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Velocity other = (Velocity) obj;
        if (Double.doubleToLongBits(this.theta) != Double.doubleToLongBits(other.theta)) {
            return false;
        }
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.theta) ^ (Double.doubleToLongBits(this.theta) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        return hash;
    }

    public ModelPoint getVector() {
        return new ModelPoint(value * Math.cos(theta), value * Math.sin(theta));
    }

    @Override
    public String toString() {
        ModelPoint v = getVector();
        return "Velocity{" + "theta=" + theta + ", value=" + value + ", x=" + v.getX() + ", y=" + v.getY() + '}';
    }

    public Velocity toHorizontalMirror() {
        return new Velocity(vv(2 * Math.PI - theta), value);
    }

    public Velocity toVerticalMirror() {
        return new Velocity(vv(Math.PI - theta), value);
    }

    public Velocity toBackward() {
        return new Velocity(vv(theta + Math.PI), value);
    }

    private double vv(double d) {
        double p2 = 2 * Math.PI;
        while (d > 0 && d > p2) {
            d = d - p2;
        }
        while (d < 0 && d < p2) {
            d = d + p2;
        }
        return d;
    }
}
