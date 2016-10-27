package net.vpc.gaming.atom.model;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/21/13
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cell {

    private CellDef def;
    private Tile[][] tiles;
    public Cell(CellDef def, Tile[][] tiles) {
        this.tiles = tiles;
        this.def = def;
    }

    public int getType() {
        return def.getType();
    }

    public int getColumns() {
        return tiles.length == 0 ? 0 : tiles[0].length;
    }

    public int getRows() {
        return tiles.length;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "type=" + getType() +
                ", tiles=" + Arrays.toString(tiles) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (def != null ? !def.equals(cell.def) : cell.def != null) return false;
        if (!Arrays.deepEquals(tiles, cell.tiles)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (def != null ? def.hashCode() : 0)*31+Arrays.deepHashCode(tiles);
    }
}
