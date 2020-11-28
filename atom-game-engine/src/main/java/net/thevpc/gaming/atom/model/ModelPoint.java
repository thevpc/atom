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
public class ModelPoint extends Point {

    public static ModelPoint ORIGIN = new ModelPoint(0, 0, 0);
    private double x;
    private double y;
    private double z;

    public ModelPoint(java.awt.Point p) {
        this(p.getX(), p.getY(), 0);
    }

    public ModelPoint(double x, double y) {
        this(x, y, 0);
    }

    public ModelPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ModelPoint copyAndSetX(double x){
        return new ModelPoint(x,y,z);
    }
    public ModelPoint copyAndSetY(double y){
        return new ModelPoint(x,y,z);
    }
    public ModelPoint copyAndSetZ(double z){
        return new ModelPoint(x,y,z);
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

    public double distance(ModelPoint pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        double pz = pt.getZ() - this.getZ();
        return Math.sqrt(px * px + py * py + pz * pz);
    }

    public double distanceSq(ModelPoint pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        double pz = pt.getZ() - this.getZ();
        return (px * px + py * py + pz * pz);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelPoint other = (ModelPoint) obj;
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

    public ViewPoint toViewPoint() {
        return new ViewPoint((int) x, (int) y);
    }

    public java.awt.Point toAWTPoint() {
        return new java.awt.Point((int) x, (int) y);
    }

    public java.awt.Point.Double toAWTPointDouble() {
        return new java.awt.Point.Double(x, y);
    }

    public java.awt.Point.Float toAWTPointFloat() {
        return new java.awt.Point.Float((float) x, (float) y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ')';
    }

    public double getAngleTo(ModelPoint other) {
        return Math.atan2(other.y - y, other.x - x);
    }

    public ModelPoint translate(ModelVector v){
        return translate(v.getX(),v.getY(),v.getZ());
    }
    public ModelPoint translate(double dx,double dy,double dz){
        return new ModelPoint(x+dx,y+dy,z+dz);
    }
}
