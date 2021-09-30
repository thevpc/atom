/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.gaming.atom.model;


import net.thevpc.gaming.atom.util.AtomUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ModelDimension extends Dimension {
    private double width;
    private double height;
    private double altitude;

    public ModelDimension(double width, double height) {
        this(width, height, 1);
    }

    public ModelDimension(double width, double height, double altitude) {
        this.width = width;
        this.height = height;
        this.altitude = altitude;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getAltitude() {
        return altitude;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelDimension other = (ModelDimension) obj;
        if (Double.doubleToLongBits(this.width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        if (Double.doubleToLongBits(this.altitude) != Double.doubleToLongBits(other.altitude)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.altitude) ^ (Double.doubleToLongBits(this.altitude) >>> 32));
        return hash;
    }

    public java.awt.Dimension toIDimension() {
        return new java.awt.Dimension((int) width, (int) height);
    }

    @Override
    public String toString() {
        return "ModelDimension{" + "width=" + width + ", height=" + height + ", altitude=" + altitude + '}';
    }

    public ModelDimension div(ModelDimension other) {
        return new ModelDimension(
                width / other.width,
                height / other.height,
                altitude / other.altitude
        );
    }

    public ModelDimension multiply(ModelDimension other) {
        return new ModelDimension(
                width * other.width,
                height * other.height,
                altitude * other.altitude
        );
    }

    public ModelDimension plus(ModelDimension other) {
        return new ModelDimension(
                width + other.width,
                height + other.height,
                altitude + other.altitude
        );
    }

    public ModelDimension subtract(ModelDimension other) {
        return new ModelDimension(
                width - other.width,
                height - other.height,
                altitude - other.altitude
        );
    }

    public ModelDimension boundBy(ModelDimension from, ModelDimension to) {
        return new ModelDimension(
                AtomUtils.boundBy(width, from.width, to.width),
                AtomUtils.boundBy(height, from.height, to.height),
                AtomUtils.boundBy(altitude, from.altitude, to.altitude)
        );
    }

    public static ModelDimension of(ViewDimension d){
        return new ModelDimension(d.getWidth(),d.getHeight(),d.getAltitude());
    }

}
