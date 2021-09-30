/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import net.thevpc.gaming.atom.util.AtomUtils;

/**
 * @author vpc
 */
public class ViewDimension extends Dimension {

    private final int width;
    private final int height;
    private final int altitude;

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
        return this.altitude == other.altitude;
    }

    @Override
    public String toString() {
        return "ViewDimension{" + "width=" + width + ", height=" + height + ", altitude=" + altitude + '}';
    }

    public ViewDimension div(ViewDimension other) {
        return new ViewDimension(
                width / other.width,
                height / other.height,
                altitude / other.altitude
        );
    }

    public ViewDimension multiply(ViewDimension other) {
        return new ViewDimension(
                width * other.width,
                height * other.height,
                altitude * other.altitude
        );
    }

    public ViewDimension plus(ViewDimension other) {
        return new ViewDimension(
                width + other.width,
                height + other.height,
                altitude + other.altitude
        );
    }

    public ViewDimension subtract(ViewDimension other) {
        return new ViewDimension(
                width - other.width,
                height - other.height,
                altitude - other.altitude
        );
    }

    public ViewDimension boundBy(ViewDimension from, ViewDimension to) {
        return new ViewDimension(
                AtomUtils.boundBy(width, from.width, to.width),
                AtomUtils.boundBy(height, from.height, to.height),
                AtomUtils.boundBy(altitude, from.altitude, to.altitude)
        );
    }

    /**
     * convert to mode by rounding doubles to ints
     * @param d dim
     * @return rounded dim
     */
    public static ViewDimension of(ModelDimension d) {
        return new ViewDimension((int) d.getWidth(), (int) d.getHeight(), (int) d.getAltitude());
    }

}
