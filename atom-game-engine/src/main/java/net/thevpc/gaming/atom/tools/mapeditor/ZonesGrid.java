/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.tools.mapeditor;

import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.model.TileDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ZonesGrid extends JPanel {

    int iw;
    int ih;
    int[][] mapInts;
    TileCellView[][] map;
    AtomMapEditor editor;

    public ZonesGrid(AtomMapEditor editor, TileCellView[][] map, int[][] mapInts, int iw, int ih) {
        this.editor = editor;
        this.map = map;
        this.iw = iw;
        this.ih = ih;
        this.mapInts = mapInts;
        super.setPreferredSize(new Dimension((map.length == 0 ? 0 : map[0].length) * iw, map.length * ih));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickedImpl(e);
            }
        });
    }

    public void mouseClickedImpl(MouseEvent e) {
        Point p = e.getPoint();
        int c = p.x / editor.imageWidth;
        int r = p.y / editor.imageHeight;
        if (r < 0 || r >= map.length) {
            return;
        }
        if (c < 0 || c >= map[0].length) {
            return;
        }
        map[r][c] = editor.getSelectedMapZone();
        if (map[r][c] != null) {
            mapInts[r][c] = editor.getSelectedMapZoneIndex();
        }
        invalidate();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                TileCellView z = map[i][j];
                if (z != null) {
                    g.drawImage(z.getImage(), j * iw, i * ih, this);
                }
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                TileCellView z = map[i][j];
                g.setColor(Color.RED);
                g.drawRect(i * iw, j * ih, iw, ih);
                g.setColor(Color.BLUE);
                TileDef[][] cells = z.getCell().getTiles();
                for (int k = 0; k < cells.length; k++) {
                    TileDef[] c = cells[k];
                    for (int l = 0; l < c.length; l++) {
                        TileDef cell = c[l];
                        if (cell.getWalls() == Tile.WALL_BOX) {
                            g.drawRect(j * iw + l * iw / c.length + 1, i * ih + k * ih / cells.length + 1, iw / c.length - 2, ih / cells.length - 2);
                        }
                    }
                }
            }
        }
    }
}
