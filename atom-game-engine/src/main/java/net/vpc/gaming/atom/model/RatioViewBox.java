/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

/**
 * @author vpc
 */
public class RatioViewBox {

    private float x;
    private float y;
    private float z;
    private float width;
    private float height;
    private float altitude;

    public RatioViewBox(float x, float y, float width, float height) {
        this(x,y,0,width,height,1);
    }

    public RatioViewBox(float x, float y, float z, float width, float height, float altitude) {
        this.x = checkVal(x, "y", x, y, z,width, height,altitude);
        this.y = checkVal(y, "x", x, y, z,width, height,altitude);
        this.z = checkVal(z, "z", x, y, z,width, height,altitude);
        this.width = checkVal(width, "width", x, y, z,width, height,altitude);
        this.height = checkVal(height, "height", x, y, z,width, height,altitude);
        this.altitude = checkVal(height, "altitude", x, y, z,width, height,altitude);
    }

    private float checkVal(float f, String name, float x, float y, float z, float width, float height, float altitude) {
        if (f < 0 || f > 1) {
            throw new IllegalArgumentException("Invalid relative value " + f + " for " + name + " in [" + x + "," + y + "," + z+","+width + "," + height +","+altitude+ "]");
        }
        return f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getZ() {
        return z;
    }

    public float getAltitude() {
        return altitude;
    }
}
