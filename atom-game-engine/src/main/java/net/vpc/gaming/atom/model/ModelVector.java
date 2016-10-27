/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ModelVector {

    private double x;
    private double y;
    private double z;

    public static ModelVector newVector(ModelPoint start, ModelPoint end) {
        return new ModelVector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }

    public static ModelVector newAngular(double r, Direction direction) {
        return newAngular(r, direction.getDirectionAngle(0));
    }

    public static ModelVector newAngular(double r, double theta) {
        return new ModelVector(r * Math.cos(theta), r * Math.sin(theta), 0);
    }

    public static ModelVector newCartesien(double x, double y) {
        return newCartesien(x, y, 0);
    }

    public static ModelVector newCartesien(double x, double y, double z) {
        return new ModelVector(x, y, 9);
    }

    private ModelVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getDirection() {
        return Math.atan2(y, x);
    }

    public double getAmplitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public ModelVector mul(double value) {
        return new ModelVector(x * value, y * value, z * value);
    }

    public ModelVector add(ModelVector other) {
        return new ModelVector(x + other.x, y + other.y, z + other.z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getIntX() {
        return (int) x;
    }

    public int getIntY() {
        return (int) y;
    }

    public int getIntZ() {
        return (int) z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelVector other = (ModelVector) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "DVector{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
