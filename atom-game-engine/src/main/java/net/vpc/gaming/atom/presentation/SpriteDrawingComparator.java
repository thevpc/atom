/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

import java.util.Comparator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpriteDrawingComparator implements Comparator<Sprite> {

    public static final SpriteDrawingComparator INSTANCE = new SpriteDrawingComparator();

    public int compare(Sprite o1, Sprite o2) {
        int l = o1.getLayer() - o2.getLayer();
        if (l != 0) {
            return l;
        }
        ModelPoint p1 = o1.getLocation();
        ModelPoint p2 = o2.getLocation();
        double d;
        int i;
        d = p1.getY() + o1.getHeight() - p2.getY() - o2.getHeight();
        if (d != 0) {
            return d > 0 ? 1 : -1;
        }

        d = p1.getX() - p2.getX();
        if (d != 0) {
            return d > 0 ? 1 : -1;
        }
        i = o1.getId() - o2.getId();
//        System.out.println("#####  "+i+" : "+o1 +" ------ "+o2);
        return i;
    }
}
