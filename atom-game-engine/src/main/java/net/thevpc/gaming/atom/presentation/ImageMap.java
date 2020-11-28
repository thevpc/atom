package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.model.ViewBox;

public interface ImageMap {
    java.util.List<ViewBox> extract(int width, int height) ;
//    int getCount();11
//    Dimension getChunkDimension(int width, int height);
//    Point getBounds(int index,Dimension chunkDim);
}
