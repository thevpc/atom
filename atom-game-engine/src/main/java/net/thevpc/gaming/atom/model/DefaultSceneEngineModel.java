/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import net.thevpc.gaming.atom.presentation.ImageProducer;
import net.thevpc.gaming.atom.presentation.MapFileImageProducer;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneEngineModel extends AbstractSceneEngineModel {

    /**
     * number of tiles per scene column
     * this is actually
     */
    private int tileColumns;
    private int tileRows;
    /**
     * number of cells in a row
     */
    private int cellColumns;
    /**
     * number of cells in a column
     */
    private int cellRows;
    private int[][] boardCellsMatrix;
    private CellDef[] boardCellDefinitions;
    private Tile[][] tileMatrix;
    /**
     * tile rows per cell
     */
    private int cellHeight;
    /**
     * tile columns per cell
     */
    private int cellWidth;
    private String mapUrlPrefix;
    private PropertyChangeListener tileValidUpdates = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {

        }
    };
    private PropertyChangeListener tileInvalidUpdates = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            throw new IllegalArgumentException("Invalid Modification");
        }
    };


    public DefaultSceneEngineModel() {
        this(new int[1][1]);
    }

    public DefaultSceneEngineModel(int tileColumns, int tileRows) {
        this(new int[tileRows][tileColumns]);
    }

    public DefaultSceneEngineModel(File file) {
        this(AtomUtils.changeFileExtension(AtomUtils.toURL(file).toString(), null));
    }

    public DefaultSceneEngineModel(CellDef[][] cells) {
        this(cells, -1, -1);
    }

    public DefaultSceneEngineModel(CellDef[][] cells, int cellWidth, int cellHeight) {
        setMap(cells, cellWidth, cellHeight);
    }

    public DefaultSceneEngineModel(int[][] map, CellDef[] cells, int cellWidth, int cellHeight) {
        setMap(map, cells, cellWidth, cellHeight);
    }

    public DefaultSceneEngineModel(int[][] cells, int cellWidth, int cellHeight) {
        setMap(cells, cellWidth, cellHeight);
    }

    public DefaultSceneEngineModel(String streamURI) {
        this.mapUrlPrefix = streamURI;
        DefaultSceneModelReader r = null;
        try {
            r = new DefaultSceneModelReader(new InputStreamReader(AtomUtils.createStream(streamURI, getClass())));
            r.parse();
            setMap(r.getMatrix(), r.getCells(), r.getTileColumnsPerCell(), r.getTileRowsPerCell());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }
    public DefaultSceneEngineModel(InputStream is) {
        this.mapUrlPrefix = "";
        DefaultSceneModelReader r = null;
        try {
            r = new DefaultSceneModelReader(new InputStreamReader(is));
            r.parse();
            setMap(r.getMatrix(), r.getCells(), r.getTileColumnsPerCell(), r.getTileRowsPerCell());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    /**
     * 0,1 boardCellsMatrix 0 no wall 1 box wall
     *
     * @param boardCellsMatrix
     */
    public DefaultSceneEngineModel(int[][] boardCellsMatrix) {
        this(boardCellsMatrix, -1, -1);
    }

    /**
     * 0,1 boardCellsMatrix 0 no wall 1 box wall
     *
     * @param boardCellsMatrix
     */
    public DefaultSceneEngineModel(boolean[][] boardCellsMatrix) {
        int[][] map = new int[boardCellsMatrix.length][boardCellsMatrix[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = boardCellsMatrix[i][j] ? 1 : 0;
            }
        }
        setMap(map, -1, -1);
    }

    public DefaultSceneEngineModel(int mapCols, int mapRows, int cellCols, int cellRows, int cellCounts) {
        setMap(mapCols, mapRows, cellCols, cellRows, cellCounts);
    }

    public CellDef getCellDef(int column, int row) {
        return boardCellDefinitions[boardCellsMatrix[row][column]];
    }

    public void setCellDefs(CellDef cell, int colMin, int colMax, int rowMin, int rowMax) {
        for (int row = rowMin; row < rowMax; row++) {
            for (int col = colMin; col < colMax; col++) {
                setCellDef(cell, col, row);
            }
        }
    }

    public void setCellDef(CellDef cell, int column, int row) {
        Map<CellDef, Integer> existing = new HashMap<>();
        for (int i = 0; i < boardCellDefinitions.length; i++) {
            existing.put(boardCellDefinitions[i], i);
        }
        Integer pos = existing.get(cell);
        if (pos == null) {
            pos = boardCellDefinitions.length;
            List<CellDef> l = new ArrayList<>(Arrays.asList(boardCellDefinitions));
            l.add(cell);
            boardCellDefinitions = l.toArray(new CellDef[l.size()]);
        }
        boardCellsMatrix[row][column] = pos;
    }

    public Tile[][] getCellTiles(int column, int row) {
        Tile[][] tiles = new Tile[cellHeight][cellWidth];
        for (int i = 0; i < cellHeight; i++) {
            for (int j = 0; j < cellWidth; j++) {
                Tile[][] m = getTileMatrix();
                tiles[i][j] = m[row * cellHeight + i][column * cellWidth + j];
            }
        }
        return tiles;
    }

    public void setMap(int[][] cells, int cellWidth, int cellHeight) {
        if (cellWidth <= 0) {
            cellWidth = 1;
        }
        if (cellHeight <= 0) {
            cellHeight = 1;
        }
        List<CellDef> cellDefs = new ArrayList<>();
        Map<Integer, Integer> visited = new HashMap<>();

        int[][] mapInts = new int[cells.length][cells.length == 0 ? 0 : cells[0].length];
        for (int i = 0; i < mapInts.length; i++) {
            for (int j = 0; j < mapInts[i].length; j++) {
                int cellType = cells[i][j];

                Integer pos = visited.get(cellType);
                if (pos == null) {
                    pos = cellDefs.size();
                    visited.put(cellType, pos);
                    TileDef[][] tiles = new TileDef[cellHeight][cellWidth];
                    for (int k = 0; k < cellHeight; k++) {
                        for (int l = 0; l < cellWidth; l++) {
                            tiles[k][l] = new TileDef(null, cellType == 0 ? 0 : Tile.WALL_BOX);
                        }
                    }
                    cellDefs.add(new CellDef(cellType, tiles));
                }
                mapInts[i][j] = pos;

            }
        }
        setMap(mapInts, cellDefs.toArray(new CellDef[cellDefs.size()]), cellWidth, cellHeight);
    }

    public void setMap(CellDef[][] cells, int cellWidth, int cellHeight) {
        List<CellDef> cellDefs = new ArrayList<>();
        Map<CellDef, Integer> visited = new HashMap<>();
        int[][] mapInts = new int[cells.length][cells.length == 0 ? 0 : cells[0].length];
        for (int i = 0; i < mapInts.length; i++) {
            for (int j = 0; j < mapInts[i].length; j++) {
                CellDef c = cells[i][j];
                Integer found = visited.get(c);
                if (found != null) {
                    mapInts[i][j] = found;
                } else {
                    found = cellDefs.size();
                    cellDefs.add(c);
                    visited.put(c, found);
                }
            }
        }
        setMap(mapInts, cellDefs.toArray(new CellDef[cellDefs.size()]), cellWidth, cellHeight);
    }

    public void setMap(int[][] map, CellDef[] cells, int cellWidth, int cellHeight) {
        boardCellsMatrix = map;
        boardCellDefinitions = cells;
        if (cellWidth <= 0) {
            if (cells.length > 0) {
                cellWidth = cells[0].getColumns();
            } else {
                cellWidth = 0;
            }
        }
        if (cellHeight <= 0) {
            if (cells.length > 0) {
                cellHeight = cells[0].getRows();
            } else {
                cellHeight = 0;
            }
        }
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        cellRows = boardCellsMatrix.length;
        cellColumns = cellRows == 0 ? 0 : boardCellsMatrix[0].length;
        int[][] biMap = getBoardCellsMatrix();
        int mapRows = biMap.length;
        int mapColumns = mapRows == 0 ? 0 : biMap[0].length;
        CellDef[] biCells = getBoardCellDefinitions();
        int cellColumns = getCellWidth();
        int cellRows = getCellHeight();
        setSize(new ModelDimension(cellColumns * mapColumns, cellRows * mapRows));
        for (int cr = 0; cr < mapRows; cr++) {
            for (int cc = 0; cc < mapColumns; cc++) {
                int cellId = biMap[cr][cc];
                final CellDef ci = biCells[cellId];
                final TileDef[][] ciTiles = ci.getTiles();
                for (int tr = 0; tr < ciTiles.length; tr++) {
                    TileDef[] ciTilesRow = ciTiles[tr];
                    for (int tc = 0; tc < ciTilesRow.length; tc++) {
                        TileDef tileDef = ciTilesRow[tc];
                        int gtr = cr * cellRows + tr;
                        int gtc = cc * cellColumns + tc;
                        Tile gtile = tileMatrix[gtr][gtc];
                        gtile.setWalls(tileDef.getWalls());
                        gtile.setZ(tileDef.getZ());

                        gtile.setColumn(gtc);
                        gtile.setRow(gtr);
                        gtile.setId(gtr * tileMatrix.length + gtc);
                        gtile.setBounds(new ModelBox(gtc, gtr, 1, 1));
                    }
                }
            }
        }
    }

    public void setMap(int mapCols, int mapRows, int cellCols, int cellRows, int cellCounts) {
        int[][] map = new int[mapRows][mapCols];
        CellDef[] cells = new CellDef[cellCounts];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new CellDef(0, cellCols, cellRows);
        }
        setMap(map, cells, cellCols, cellRows);
    }

    public int getCellsCount() {
        return boardCellDefinitions.length;
    }

    public void setCellsCount(int cellsCount) {
        final int oldCount = getCellsCount();
        if (oldCount != cellsCount) {
            CellDef[] c2 = new CellDef[cellsCount];
            for (int i = 0; i < c2.length; i++) {
                if (i < boardCellDefinitions.length) {
                    c2[i] = boardCellDefinitions[i];
                } else {
                    c2[i] = new CellDef(0, getCellWidth(), getCellHeight());
                }
            }
            boardCellDefinitions = c2;
            if (cellsCount < oldCount) {
                for (int i = 0; i < boardCellsMatrix.length; i++) {
                    int[] is = boardCellsMatrix[i];
                    for (int j = 0; j < is.length; j++) {
                        int k = is[j];
                        if (k >= cellsCount) {
                            is[j] = 0;
                        }
                    }
                }
            }
        }
    }

    public int getMapColumns() {
        return boardCellsMatrix.length == 0 ? 0 : boardCellsMatrix[0].length;
    }

    public void setMapColumns(int mapCols) {
        if (mapCols != getMapColumns()) {
            int[][] matrix2 = new int[getMapRows()][mapCols];
            for (int i = 0; i < boardCellsMatrix.length; i++) {
                int[] is = boardCellsMatrix[i];
                for (int j = 0; j < is.length; j++) {
                    int k = is[j];
                    if (i < matrix2.length && j < matrix2[i].length) {
                        matrix2[i][j] = k;
                    }
                }
            }
            boardCellsMatrix = matrix2;
        }
    }

    public int getMapRows() {
        return boardCellsMatrix.length;
    }

    public void setMapRows(int mapRows) {
        if (mapRows != getMapRows()) {
            int[][] matrix2 = new int[mapRows][getMapColumns()];
            for (int i = 0; i < boardCellsMatrix.length; i++) {
                int[] is = boardCellsMatrix[i];
                for (int j = 0; j < is.length; j++) {
                    int k = is[j];
                    if (i < matrix2.length && j < matrix2[i].length) {
                        matrix2[i][j] = k;
                    }
                }
            }
            boardCellsMatrix = matrix2;
        }
    }

    private TileDef empty() {
        TileDef empty = new TileDef();
        empty.setWalls(Tile.NO_WALLS);
        return empty;
    }

    private TileDef box() {
        TileDef empty = new TileDef();
        empty.setWalls(Tile.WALL_BOX);
        return empty;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        if (cellWidth != getCellWidth()) {
            for (int i = 0; i < boardCellDefinitions.length; i++) {
                final CellDef c = boardCellDefinitions[i];
                c.updateSize(cellWidth, c.getRows());
            }
            this.cellWidth = cellWidth;
        }
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        if (cellHeight != getCellHeight()) {
            for (int i = 0; i < boardCellDefinitions.length; i++) {
                final CellDef c = boardCellDefinitions[i];
                c.updateSize(c.getColumns(), cellHeight);
            }
            this.cellHeight = cellHeight;
        }
    }

    public CellDef[] getBoardCellDefinitions() {
        return boardCellDefinitions;
    }

    public int[][] getBoardCellsMatrix() {
        return boardCellsMatrix;
    }

//    public void setBoardCellDefinitions(CellDef[] boardCellDefinitions) {
//        this.boardCellDefinitions = boardCellDefinitions;
//    }
//
//    public void setBoardCellsMatrix(int[][] map) {
//        this.boardCellsMatrix = map;
//    }

    public ImageProducer getImageProducer() {
        return mapUrlPrefix == null ? null : new MapFileImageProducer(mapUrlPrefix);
    }

    protected void setSizeImpl(ModelDimension dimension) {
        this.tileRows = (int) dimension.getHeight();
        this.tileColumns = (int) dimension.getWidth();
        if (this.tileMatrix != null) {
            for (Tile[] tiles : this.tileMatrix) {
                if (tiles != null) {
                    for (Tile tile : tiles) {
                        if (tile != null) {
                            removeTileMonitor(tile);
                        }
                    }
                }
            }
        }
        this.tileMatrix = new Tile[tileRows][tileColumns];
        int tileId = 0;
        for (int r = 0; r < tileRows; r++) {
            for (int c = 0; c < tileColumns; c++) {
                Tile tile = new Tile();
                tile.setId(tileId);
                tileId++;
                tile.setColumn(c);
                tile.setRow(r);
                tile.setBounds(new ModelBox(c, r, 1, 1));
                tileMatrix[r][c] = tile;
                addTileMonitor(tile);
            }
        }
        super.setSizeImpl(new ModelDimension(tileColumns, tileRows));
    }

    private void addTileMonitor(Tile tile) {
        tile.addPropertyChangeListener("z", tileValidUpdates);
        tile.addPropertyChangeListener("kind", tileValidUpdates);
        tile.addPropertyChangeListener("column", tileInvalidUpdates);
        tile.addPropertyChangeListener("row", tileInvalidUpdates);
        tile.addPropertyChangeListener("bounds", tileInvalidUpdates);
        tile.addPropertyChangeListener("id", tileInvalidUpdates);
    }

    private void removeTileMonitor(Tile tile) {
        tile.removePropertyChangeListener("z", tileValidUpdates);
        tile.removePropertyChangeListener("kind", tileValidUpdates);
        tile.removePropertyChangeListener("column", tileInvalidUpdates);
        tile.removePropertyChangeListener("row", tileInvalidUpdates);
        tile.removePropertyChangeListener("bounds", tileInvalidUpdates);
        tile.removePropertyChangeListener("id", tileInvalidUpdates);
    }

    public int getColumns() {
        return tileColumns;
    }

    public int getRows() {
        return tileRows;
    }

    public Tile[][] getTileMatrix() {
        return tileMatrix;
    }

}
