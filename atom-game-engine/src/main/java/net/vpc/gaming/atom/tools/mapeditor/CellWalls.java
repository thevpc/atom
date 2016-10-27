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
public class CellWalls extends JPanel {

    int cw;
    int ch;
    CellDef map;
    AtomMapEditor editor;

    public CellWalls(AtomMapEditor editor, int iw, int ih) {
        this.editor = editor;
        this.cw = iw;
        this.ch = ih;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickedImpl(e);
            }
        });
        setMap(new CellDef(0,0, 0));
    }

    public CellDef getMap() {
        return map;
    }

    public void setMap(CellDef map) {
        this.map = map;
        if (map.getColumns() > 0) {
            super.setPreferredSize(new Dimension(map.getColumns() * cw, map.getRows() * ch));
        } else {
            super.setPreferredSize(new Dimension(1 * cw, map.getRows() * ch));
        }
        repaint();
    }

    public void mouseClickedImpl(MouseEvent e) {
        Point p = e.getPoint();
        int c = p.x / cw;
        int r = p.y / ch;
        CellClicked(map.getTiles()[r][c], e);
        repaint();
    }

    protected void CellClicked(TileDef a, MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int min = Tile.NO_WALLS;
            int max = Tile.WALL_BOX;
            if (e.isControlDown()) {
                int q = a.getWalls();
                q--;
                if (q < min) {
                    q = max;
                }
                a.setWalls(q);
            } else {
                int q = a.getWalls();
                q++;
                if (q > max) {
                    q = min;
                }
                a.setWalls(q);
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

    private void paintCell(Graphics g, TileDef z, int i, int j) {
        int walls = z.getWalls();
        int l = 2;
        int m = 5;
        int x0 = j * cw + m;
        int x1 = j * cw + cw - m;
        int y0 = i * ch + m;
        int y1 = i * ch + ch - m;
        Stroke s = ((Graphics2D) g).getStroke();
        switch (walls) {
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
                if ((walls & 1) != 0) {
                    g.drawLine(x0, y0, x1, y0);
                }
                if ((walls & 2) != 0) {
                    g.drawLine(x1, y0, x1, y1);
                }
                if ((walls & 4) != 0) {
                    g.drawLine(x0, y1, x1, y1);
                }
                if ((walls & 8) != 0) {
                    g.drawLine(x0, y0, x0, y1);
                }
            }
        }
        ((Graphics2D) g).setStroke(s);
        g.drawString(String.valueOf(walls), x0 + 10, (y0 + y1) / 2);
    }
}
