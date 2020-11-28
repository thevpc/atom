/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import net.thevpc.gaming.atom.util.RuntimeIOException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultSceneModelReader {

    List<CellDef> currentCells = new ArrayList<CellDef>();
    List<List<TileDef>> currentCellTiles = null;
    List<List<Integer>> currentMapCells = null;
    int tileRowsPerCell = 1;
    int tileColumnsPerCell = 1;
    int currentCellType = 0;
    BufferedReader br;
    int lineno = 0;


    int[][] matrix;
    CellDef[] cells;

    public DefaultSceneModelReader(File file) throws IOException {
        this(new FileReader(file));
    }

    public DefaultSceneModelReader(URL url) throws IOException {
        this(url.openStream());
    }

    public DefaultSceneModelReader(InputStream is) {
        this.br = new BufferedReader(new InputStreamReader(is));
    }

    public DefaultSceneModelReader(Reader br) {
        if (br instanceof BufferedReader) {
            this.br = (BufferedReader) br;
        } else {
            this.br = new BufferedReader(br);
        }
    }

    public void close() throws RuntimeIOException {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        }
    }

    public void parse() throws RuntimeIOException {
        final int EXPECT_DIM = 0;
        final int EXPECT_DIM_DEF = 1;
        final int EXPECT_CELL = 2;
        final int EXPECT_CELL_HEADER = 3;
        final int EXPECT_CELL_MATRIX = 4;
        final int EXPECT_MAP = 5;
        String line;
        int status = EXPECT_DIM;
        while ((line = readNextLine()) != null) {
            line = line.trim();
            if (line.startsWith("#")) {
                line = "";
            }
            if (line.isEmpty()) {
                continue;
            }
            switch (status) {
                case EXPECT_DIM: {
                    if (line.equals("dim:")) {
                        status = EXPECT_DIM_DEF;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    break;
                }
                case EXPECT_DIM_DEF: {
                    String[] x = line.split("x");
                    tileColumnsPerCell = Integer.parseInt(x[0]);
                    tileRowsPerCell = Integer.parseInt(x[1]);
                    status = EXPECT_CELL;
                    break;
                }
                case EXPECT_CELL: {
                    if (line.equals("cell:")) {
                        consumeCell();
                        currentCellTiles = new ArrayList<List<TileDef>>();
                        status = EXPECT_CELL_HEADER;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    break;
                }
                case EXPECT_CELL_HEADER: {
                    currentCellType = Integer.parseInt(line.trim());
                    status = EXPECT_CELL_MATRIX;
                    break;
                }
                case EXPECT_CELL_MATRIX: {
                    if (line.equals("map:")) {
                        consumeCell();
                        currentMapCells = new ArrayList<List<Integer>>();
                        status = EXPECT_MAP;
                    } else if (line.equals("cell:")) {
                        consumeCell();
                        currentCellTiles = new ArrayList<List<TileDef>>();
                        status = EXPECT_CELL_HEADER;
                    } else {
                        if (currentCellTiles == null) {
                            throw new IllegalArgumentException("[line " + lineno + "] expected \"cell:\"");
                        }
                        List<TileDef> tilesLine = new ArrayList<TileDef>();
                        for (String v : readRowStrings(line)) {
                            final String[] ss = v.split(";");
                            TileDef t = new TileDef();
                            int[] z = new int[4];
                            if (ss.length == 2) {
                                int zall = Integer.parseInt(ss[0].trim());
                                for (int i = 0; i < 4; i++) {
                                    z[i] = zall;
                                }
                                t.setZ(z);
                                t.setWalls(Integer.parseInt(ss[1].trim()));
                            } else {
                                for (int i = 0; i < 4; i++) {
                                    z[i] = Integer.parseInt(ss[i].trim());
                                }
                                t.setZ(z);
                                t.setWalls(Integer.parseInt(ss[4].trim()));
                            }
                            tilesLine.add(t);
                        }
                        currentCellTiles.add(tilesLine);
                    }
                    break;
                }
                case EXPECT_MAP: {
                    if (currentMapCells == null) {
                        throw new IllegalArgumentException("[line " + lineno + "] expected \"map:\"");
                    }
                    List<Integer> cellLine = new ArrayList<Integer>();
                    for (String v : readRowStrings(line)) {
                        int cellIndex = Integer.parseInt(v.trim());
                        if (cellIndex >= 0 && cellIndex < currentCells.size()) {
                            cellLine.add(cellIndex);
                        } else {
                            throw new IllegalArgumentException("unknown cell " + cellIndex);
                        }
                    }
                    if (!currentMapCells.isEmpty() && currentMapCells.get(0).size() != cellLine.size()) {
                        throw new IllegalArgumentException("bad matrix " + currentMapCells.get(0).size() + " != " + cellLine.size());
                    }
                    currentMapCells.add(cellLine);
                    break;
                }
            }
        }
        if (currentMapCells == null) {
            currentMapCells = new ArrayList<>();
        }
        if (currentCells == null) {
            currentCells = new ArrayList<>();
        }
        int[][] map = new int[currentMapCells.size()][currentMapCells.isEmpty() ? 0 : currentMapCells.get(0).size()];
        for (int i = 0; i < map.length; i++) {
            int[] is = map[i];
            for (int j = 0; j < is.length; j++) {
                map[i][j] = currentMapCells.get(i).get(j);
            }
        }
        this.matrix=(map);
        cells=(currentCells.toArray(new CellDef[currentCells.size()]));
    }


    public int[][] getMatrix() {
        return matrix;
    }

    public CellDef[] getCells() {
        return cells;
    }

    public int getTileRowsPerCell() {
        return tileRowsPerCell;
    }

    public int getTileColumnsPerCell() {
        return tileColumnsPerCell;
    }

    protected void consumeCell() {
        if (currentCellTiles != null) {
            if (currentCellTiles.isEmpty()) {
                throw new IllegalArgumentException("Empty cell");
            }
            int newCellRows = currentCellTiles.size();
            int newCellColumns = currentCellTiles.get(0).size();
            if (tileRowsPerCell >= 0) {
                if (newCellRows != tileRowsPerCell) {
                    throw new IllegalArgumentException("incompatible CellRows. expected " + tileRowsPerCell + " and found " + newCellRows);
                }
                if (newCellColumns != tileColumnsPerCell) {
                    throw new IllegalArgumentException("incompatible CellColumns. expected " + tileColumnsPerCell + " and found " + newCellColumns);
                }
            } else {
                tileRowsPerCell = newCellRows;
                tileColumnsPerCell = newCellColumns;
            }
            TileDef[][] r = new TileDef[newCellRows][newCellColumns];
            for (int i = 0; i < newCellRows; i++) {
                for (int j = 0; j < newCellColumns; j++) {
                    final List<TileDef> dynRow = currentCellTiles.get(i);
                    if (j >= dynRow.size()) {
                        throw new IllegalArgumentException("incompatible CellColumns. expected " + tileColumnsPerCell + " and found " + dynRow.size());
                    }
                    r[i][j] = dynRow.get(j);
                }
            }
            currentCells.add(new CellDef(currentCellType, r));
        }

    }

    protected String[] readRowStrings(String line) throws RuntimeIOException {
        ArrayList<String> cols = new ArrayList<String>();
        final int EXPECT_OPEN = 0;
        final int EXPECT_V = 1;
        int status = EXPECT_OPEN;
        char[] e = line.toCharArray();
        StringBuilder cur = new StringBuilder(20);
        for (int i = 0; i < e.length; i++) {
            char c = e[i];
            switch (status) {
                case EXPECT_OPEN: {
                    switch (c) {
                        case '(': {
                            status = EXPECT_V;
                            break;
                        }
                        default: {
                            throw new RuntimeIOException("Expected (");
                        }
                    }
                    break;
                }
                case EXPECT_V: {
                    switch (c) {
                        case '(': {
                            if (cur.length() > 0) {
                                throw new RuntimeIOException("Expected )");
                            } else {
                                throw new RuntimeIOException("Expected value");
                            }
                        }
                        case ')': {
                            if (cur.length() > 0) {
                                cols.add(cur.toString());
                                cur.delete(0, cur.length());
                            } else {
                                throw new RuntimeIOException("Expected value");
                            }
                            status = EXPECT_OPEN;
                            break;
                        }
                        default: {
                            cur.append(c);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return cols.toArray(new String[cols.size()]);
    }

    private String readNextLine() throws RuntimeIOException {
        String line;
        try {
            while ((line = br.readLine()) != null) {
                lineno++;
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#")) {
                    return line;
                }
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        return null;
    }
}
