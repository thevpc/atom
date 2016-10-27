/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.tools.mapeditor;

import net.vpc.gaming.atom.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class CellsGrid extends JPanel {

    int cw;
    int ch;
    Tile[][] map;
    AtomMapEditor editor;

    public CellsGrid(AtomMapEditor editor, int iw, int ih) {
        this.editor = editor;
        this.cw = iw;
        this.ch = ih;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickedImpl(e);
            }
        });
        setMap(new Tile[0][]);
    }

    public Tile[][] getMap() {
        return map;
    }

    public void setMap(Tile[][] map) {
        this.map = map;
        if (map.length > 0) {
            super.setPreferredSize(new Dimension(map[0].length * cw, map.length * ch));
        } else {
            super.setPreferredSize(new Dimension(1 * cw, map.length * ch));
        }
        repaint();
    }

    public void mouseClickedImpl(MouseEvent e) {
        Point p = e.getPoint();
        int c = p.x / cw;
        int r = p.y / ch;
        int x0 = (p.x - (c * cw)) / cw;
        int y0 = (p.x - (c * cw)) / cw;
        double delta = 0.25;
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
            CellClicked(map[r][c], e, edge);
        }
        repaint();
    }

    protected void CellClicked(Tile a, MouseEvent e, int edge) {
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
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Tile z = map[i][j];
                if (z != null) {
                    paintCell(g, z, i, j);
                }
            }
        }
    }

    private void paintCell(Graphics g, Tile tile, int i, int j) {
        int type = tile.getWalls();
        int l = 2;
        int m = 5;
        int x0 = j * cw + m;
        int x1 = j * cw + cw - m;
        int y0 = i * ch + m;
        int y1 = i * ch + ch - m;
        Stroke s = ((Graphics2D) g).getStroke();
        int[] z = tile.getZ();
        Color c = null;
        if (z[0] == z[1] && z[0] == z[1] && z[0] == z[2] && z[0] == z[3]) {
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
            g.setColor(Color.gray);
            g.fillRect(j * cw, i * ch, cw, ch);
            g.setColor(Color.red);
            g.fillRect(x0, y0, x1 - x0, y1 - y0);
            g.setColor(Color.black);
            g.drawRect(j * cw, i * ch, cw, ch);
            ((Graphics2D) g).setStroke(new BasicStroke(l));
            g.setColor(Color.red.darker());
            g.drawRect(x0, y0, x1 - x0, y1 - y0);

        }
        switch (type) {
            case Tile.NO_WALLS: {
                g.setColor(Color.gray);
                g.fillRect(j * cw, i * ch, cw, ch);
                g.setColor(Color.black);
                g.drawRect(j * cw, i * ch, cw, ch);
                break;
            }
            case Tile.WALL_BOX: {
                g.setColor(Color.gray);
                g.fillRect(j * cw, i * ch, cw, ch);
                g.setColor(Color.red);
                g.fillRect(x0, y0, x1 - x0, y1 - y0);
                g.setColor(Color.black);
                g.drawRect(j * cw, i * ch, cw, ch);
                ((Graphics2D) g).setStroke(new BasicStroke(l));
                g.setColor(Color.red.darker());
                g.drawRect(x0, y0, x1 - x0, y1 - y0);
                break;
            }
            default: {
                g.setColor(Color.gray);
                g.fillRect(j * cw, i * ch, cw, ch);
                g.setColor(Color.gray);
                g.drawRect(j * cw, i * ch, cw, ch);
                ((Graphics2D) g).setStroke(new BasicStroke(l));
                g.setColor(Color.black);
            }
        }
        ((Graphics2D) g).setStroke(s);
        g.drawString("T=" + type + ";Z=" + z, x0 + 10, (y0 + y1) / 2);
    }
}
