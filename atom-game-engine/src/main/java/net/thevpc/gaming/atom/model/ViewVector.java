/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ViewVector {

    private int x;
    private int y;

    public static ViewVector newCartesien(int x, int y) {
        return new ViewVector(x, y);
    }

    private ViewVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ViewVector add(ViewVector other) {
        return new ViewVector(x + other.x, y + other.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.x;
        hash = 59 * hash + this.y;
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
        final ViewVector other = (ViewVector) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "ViewVector{" + "x=" + x + ", y=" + y + '}';
    }
}
