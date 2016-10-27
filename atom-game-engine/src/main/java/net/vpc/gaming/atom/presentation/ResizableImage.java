package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.util.AtomUtils;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/19/13
 * Time: 1:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResizableImage {
    private String name;
    private Image baseImage;
    private Image cachedImage;
    private int cachedImageWidth = -1;
    private int cachedImageHeight = -1;

    public ResizableImage(Image baseImage) {
        this.baseImage = baseImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage(int gw, int gh) {
        if (cachedImageWidth < 0 || cachedImageHeight < 0 || cachedImageWidth != gw || cachedImageHeight != gh) {
//            System.out.println("Rescale "+name+"(" + cachedImageWidth + "," + cachedImageHeight + ") => (" + gw + "," + gh + ")");
            cachedImageWidth = gw;
            cachedImageHeight = gh;
            return cachedImage = AtomUtils.resizeImage(baseImage, gw, gh);
        }
        return cachedImage;
    }

    public Image getBaseImage() {
        return baseImage;
    }
}
