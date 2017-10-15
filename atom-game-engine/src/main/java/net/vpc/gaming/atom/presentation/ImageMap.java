package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ViewBox;

import java.awt.*;
import java.util.Iterator;

public interface ImageMap {
    java.util.List<ViewBox> extract(int width,int height) ;
//    int getCount();11
//    Dimension getChunkDimension(int width, int height);
//    Point getBounds(int index,Dimension chunkDim);
}
