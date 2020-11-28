/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TileDef implements Cloneable, Serializable {

    private int[] z = new int[4];
    /**
     * obstacle or crossable etc...
     */
    private int walls;

    public TileDef() {
    }

    public TileDef(int[] z, int walls) {
        setZ(z);
        setWalls(walls);
    }

    public int[] getZ() {
        return z;
    }

    public void setZ(int[] z) {
        if (z == null) {
            z = new int[4];
        }
        this.z = new int[4];
        for (int i = 0; i < this.z.length; i++) {
            if (i < z.length) {
                this.z[i] = z[i];
            }
        }
    }

    public int getWalls() {
        return walls;
    }

    public void setWalls(int walls) {
        this.walls = walls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileDef tileDef = (TileDef) o;

        if (walls != tileDef.walls) return false;
        if (!Arrays.equals(z, tileDef.z)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = z != null ? Arrays.hashCode(z) : 0;
        result = 31 * result + walls;
        return result;
    }
}
