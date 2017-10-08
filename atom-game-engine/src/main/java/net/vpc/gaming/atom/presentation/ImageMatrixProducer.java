package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.util.AtomUtils;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/24/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageMatrixProducer implements ImageProducer {
    private Image[] fullImages;

    public ImageMatrixProducer(String imageUrl, int cols, int rows) {
        this.fullImages = AtomUtils.splitImage(AtomUtils.createStream(imageUrl), cols, rows,0,0);
    }

    public ImageMatrixProducer(String imageUrl, int cols, int rows,int xinset,int yinset) {
        this.fullImages = AtomUtils.splitImage(AtomUtils.createStream(imageUrl), cols, rows,xinset,yinset);
    }

    public ImageMatrixProducer(String imageUrl, ImageMap map) {
        this.fullImages = AtomUtils.splitImage(AtomUtils.loadBufferedImage(AtomUtils.createStream(imageUrl)), map);
    }

    @Override
    public int getImagesCount(int type) {
        return fullImages.length;
    }

    @Override
    public Image getImage(int type, int index) {
        return fullImages[index % fullImages.length];
    }
}
