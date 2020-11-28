package net.thevpc.gaming.atom.presentation;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/24/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultImageProducer implements ImageProducer {
    private Image[] fullImages;

    public DefaultImageProducer(Image[] images) {
        this.fullImages = images;
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
