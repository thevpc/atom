package net.vpc.gaming.atom.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/21/13
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CellDef implements Cloneable, Serializable{

    private int type;
    private TileDef[][] tiles;
    public CellDef(int type, TileDef tile) {
        this(type,new TileDef[][]{{tile}});
    }
    public CellDef(int type, TileDef[][] tiles) {
        this.tiles = tiles;
        this.type = type;
    }


    public CellDef(int type, int walls,int[] z) {
        this(type,1,1,walls,z);
    }

    public CellDef(int type, int cols, int rows,int walls,int[] z) {
        this.tiles = new TileDef[rows][cols];
        this.type = type;
        for (int i = 0; i < tiles.length; i++) {
            TileDef[] tileDefs = tiles[i];
            for (int j = 0; j < tileDefs.length; j++) {
                TileDef t = new TileDef();
                t.setWalls(walls);
                t.setWalls(walls);
                t.setZ(z);
                tileDefs[j] = t;
            }
        }
    }

    public CellDef(int type, int cols, int rows) {
        this(type,cols,rows,0,null);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void updateSize(int cols2, int rows2) {
        if (cols2 != getColumns() || rows2 != getRows()) {
            TileDef[][] tiles2;
            tiles2 = new TileDef[rows2][cols2];
            for (int i = 0; i < tiles2.length; i++) {
                TileDef[] tileDefs = tiles2[i];
                for (int j = 0; j < tileDefs.length; j++) {
                    if (i < tiles.length && j < tiles[i].length) {
                        tileDefs[j] = tiles[i][j];
                    } else {
                        tileDefs[j] = new TileDef();
                    }
                }
            }
            tiles = tiles2;
        }
    }

    public int getType() {
        return type;
    }

    public int getColumns() {
        return tiles.length == 0 ? 0 : tiles[0].length;
    }

    public int getRows() {
        return tiles.length;
    }

    public TileDef[][] getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "CellDef{" +
                "type=" + type +
                ", tiles=" + Arrays.toString(tiles) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellDef cellDef = (CellDef) o;

        if (type != cellDef.type) return false;
        if (!Arrays.deepEquals(tiles, cellDef.tiles)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return type*31+Arrays.deepHashCode(tiles);
    }
}
