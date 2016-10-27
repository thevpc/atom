/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.tools.mapeditor;

import net.vpc.gaming.atom.model.CellDef;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.TileDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class CellElevation extends JPanel {

    int cw;
    int ch;
    CellDef map;
    AtomMapEditor editor;

    public CellElevation(AtomMapEditor editor, int iw, int ih) {
        this.editor = editor;
        this.cw = iw;
        this.ch = ih;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickedImpl(e);
            }
        });
        setMap(null);
    }

    public CellDef getMap() {
        return map;
    }

    public void setMap(CellDef map) {
        if (map == null) {
            map = new CellDef(0, 0, 0);
        }
        this.map = map;
        if (map.getRows() > 0) {
            super.setPreferredSize(new Dimension(map.getColumns() * cw + 5, map.getRows() * ch + 5));
        } else {
            super.setPreferredSize(new Dimension(cw + 5, map.getRows() * ch + 5));
        }
        repaint();
    }

    public void mouseClickedImpl(MouseEvent e) {
        Point p = e.getPoint();
        int c = p.x / cw;
        int r = p.y / ch;
        double x0 = (p.x - (c * cw)) * 1.0 / cw;
        double y0 = (p.y - (r * ch)) * 1.0 / ch;
        double delta = 0.35;
        int edge = -1;
        if (x0 <= delta && y0 <= delta) {
            edge = 0;
        } else if (x0 >= (1 - delta) && y0 <= delta) {
            edge = 1;
        } else if (x0 >= (1 - delta) && y0 >= (1 - delta)) {
            edge = 2;
        } else if (x0 <= (delta) && y0 >= (1 - delta)) {
            edge = 3;
        }
        if (edge >= 0) {
            CellClicked(map.getTiles()[r][c], e, edge);
        }
        repaint();
    }

    protected void CellClicked(TileDef a, MouseEvent e, int edge) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!e.isControlDown()) {
                a.getZ()[edge]++;
            } else {
                a.getZ()[edge]--;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                TileDef z = map.getTiles()[i][j];
                if (z != null) {
                    paintCell(g, z, i, j);
                }
            }
        }
    }

    private void paintCell(Graphics g, TileDef tile, int i, int j) {
        int type = tile.getWalls();
        int l = 2;
        int m = 5;
        int x0 = j * cw + m;
        int x1 = j * cw + cw - m;
        int y0 = i * ch + m;
        int y1 = i * ch + ch - m;
        int cw0 = x1 - x0;
        int ch0 = y1 - y0;

        Stroke s = ((Graphics2D) g).getStroke();
        int[] z = tile.getZ();
        Color c = null;
        if (z[0] == z[1] && z[0] == z[1] && z[0] == z[1] && z[0] == z[3]) {
            if (z[0] == 0) {
                c = Color.DARK_GRAY;
            } else if (z[0] > 0) {
                c = Color.RED;
            } else if (z[0] > 0) {
                c = Color.BLUE;
            }
        } else {
            c = Color.GREEN;
        }
        if (c != null) {
            int dw = (int) (cw0 * 0.35);
            int dh = (int) (ch0 * 0.35);
            g.setColor(Color.gray);
            g.fillRect(j * cw, i * ch, cw, ch);
//            g.setColor(Color.red);
//            g.fillRect(x0, y0, x1 - x0, y1 - y0);
            g.setColor(Color.black);
            g.drawRect(j * cw, i * ch, cw, ch);
            ((Graphics2D) g).setStroke(new BasicStroke(l));
//            g.setColor(Color.red.darker());
//            g.drawRect(x0, y0, x1 - x0, y1 - y0);

            g.setColor(Color.WHITE);
            g.fillRect(x0, y0, dw, dh);
            g.fillRect(x0 + cw0 - dw, y0, dw, dh);
            g.fillRect(x0 + cw0 - dw, y0 + ch0 - dh, dw, dh);
            g.fillRect(x0, y0 + ch0 - dh, dw, dh);
        }

        g.setColor(Color.BLACK);
        ((Graphics2D) g).setStroke(s);
        int xk = g.getFontMetrics().stringWidth("AA");
        int yk = g.getFontMetrics().getHeight();
        if (z[0] != 0) {
            g.drawString(String.valueOf(z[0]), x0, y0 + 10);
        }
        if (z[1] != 0) {
            g.drawString(String.valueOf(z[1]), x1 - xk + 2, y0 + 10);
        }
        if (z[2] != 0) {
            g.drawString(String.valueOf(z[2]), x1 - xk + 2, y1 - 2);
        }
        if (z[3] != 0) {
            g.drawString(String.valueOf(z[3]), x0, y1 - 2);
        }
    }
}
