/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Tile;

import java.util.Comparator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TileDrawingComparator implements Comparator<Tile> {

    public static final TileDrawingComparator INSTANCE = new TileDrawingComparator();

    public int compare(Tile o1, Tile o2) {
        ModelPoint p1 = o1.getLocation();
        ModelPoint p2 = o2.getLocation();
        double d;
        int i;
        d = p1.getY() - p2.getY();
        if (d != 0) {
            return d > 0 ? 1 : -1;
        }

        d = p1.getX() - p2.getX();
        if (d > 0) {
            return -1;
        } else if (d < 0) {
            return 1;
        }
        i = o1.getId() - o2.getId();
//        System.out.println("#####  "+i+" : "+o1 +" ------ "+o2);
        return i;
    }
}
