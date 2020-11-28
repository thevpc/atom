package net.thevpc.gaming.atom.presentation;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/24/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageProducer {
    public static final int BACKGROUND = 1;

    public int getImagesCount(int type);

    public Image getImage(int type, int index);
}
