/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Tile implements Cloneable, Serializable {

    public static final int NO_WALLS = 0;
    public static final int WALL_NORTH = 1;
    public static final int WALL_EAST = 2;
    public static final int WALL_SOUTH = 4;
    public static final int WALL_WEST = 8;
    public static final int WALL_BOX = WALL_NORTH + WALL_EAST + WALL_SOUTH + WALL_WEST;
    private int id;
    private ModelBox rectangle;
    private int column;
    private int row;
    /**
     * elevation
     */
    private int[] z = new int[4];
    /**
     * obstacle or crossable etc...
     */
    private int walls;
    private int kind;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Tile(int id, ModelBox rectangle, int column, int row, int[] z, int walls, int kind) {
        this.id = id;
        this.rectangle = rectangle;
        this.column = column;
        this.row = row;
        this.z = z;
        this.walls = walls;
        this.kind = kind;
    }

    public Tile() {
    }

    public static String getWallsString(int walls) {
        if (walls == 0) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        if ((walls & Tile.WALL_NORTH) != 0) {
            s.append('N');
        }
        if ((walls & Tile.WALL_EAST) != 0) {
            s.append('E');
        }
        if ((walls & Tile.WALL_SOUTH) != 0) {
            s.append('S');
        }
        if ((walls & Tile.WALL_WEST) != 0) {
            s.append('W');
        }
        walls &= ~Tile.WALL_BOX;
        if (walls != 0) {
            s.append(walls);
        }
        return s.toString();
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int gridx) {
        this.column = gridx;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int gridy) {
        this.row = gridy;
    }

    public double getWidth() {
        return getBounds().getWidth();
    }

    public double getHeight() {
        return getBounds().getHeight();
    }

    public double getX() {
        return getLocation().getX();
    }

    public double getY() {
        return getLocation().getY();
    }

    public ModelPoint getLocation() {
        return getBounds().getLocation();
    }

    public ModelBox getBounds() {
        return rectangle;
    }

    public void setBounds(ModelBox rectangle) {
        this.rectangle = rectangle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalls() {
        return walls;
    }

    public void setWalls(int type) {
        int old = this.walls;
        this.walls = type;
        propertyChangeSupport.firePropertyChange("walls", old, walls);
    }

    public int[] getZ() {
        return z;
    }

    public void setZ(int[] z) {
        if (z == null) {
            z = new int[4];
        }
        if (!Objects.deepEquals(z, this.z)) {
            int[] old = this.z;
            this.z = new int[4];
            for (int i = 0; i < this.z.length; i++) {
                if (i < z.length) {
                    this.z[i] = z[i];
                }
            }
            propertyChangeSupport.firePropertyChange("z", old, z);
        }
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        int old=this.kind;
        this.kind = kind;
        propertyChangeSupport.firePropertyChange("kind", old, kind);
    }

    public void addPropertyChangeListener(String property,PropertyChangeListener propertyChangeListener){
        propertyChangeSupport.addPropertyChangeListener(property,propertyChangeListener);
    }

    public void removePropertyChangeListener(String property,PropertyChangeListener propertyChangeListener){
        propertyChangeSupport.removePropertyChangeListener(property,propertyChangeListener);
    }

    @Override
    public Tile clone() {
        try {
            return (Tile) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        if (column != tile.column) return false;
        if (id != tile.id) return false;
        if (row != tile.row) return false;
        if (kind != tile.kind) return false;
        if (walls != tile.walls) return false;
        if (rectangle != null ? !rectangle.equals(tile.rectangle) : tile.rectangle != null) return false;
        if (!Arrays.equals(z, tile.z)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rectangle != null ? rectangle.hashCode() : 0);
        result = 31 * result + column;
        result = 31 * result + row;
        result = 31 * result + (z != null ? Arrays.hashCode(z) : 0);
        result = 31 * result + walls;
        result = 31 * result + kind;
        return result;
    }

    @Override
    public String toString() {
        return "Tile{" + id + "[" + column + "," + row + "," + rectangle + "], z=" + Arrays.toString(z) + ", walls=" + getWallsString(walls) + ", type=" + kind + '}';
    }
}
