/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.debug.AtomDebug;
import net.thevpc.gaming.atom.model.SceneModel;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.ResizableImage;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ImageScreenLayer extends DefaultLayer {

    private ResizableImage[] images;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private boolean resizeImages = false;
    private boolean viewPortImage = false;
    private Image[] resizedImages;
    private int cachedImageWidth = -1;
    private int cachedImageHeight = -1;

    public ImageScreenLayer(String imageMap) {
        this(SCREEN_BACKGROUND_LAYER, 1, 1, imageMap);
    }

    public ImageScreenLayer(int zIndex, String imageMap) {
        this(zIndex, 1, 1, imageMap);
    }

    public ImageScreenLayer(int zIndex, int rows, int cols, String imageMap) {
        init(zIndex, AtomUtils.splitImage(AtomUtils.createStream(imageMap, getClass()), cols, rows,0,0));
    }

    public ImageScreenLayer(int zIndex, boolean mapAligned, Class baseClass, int rows, int cols, String imageMap) {
        init(zIndex, AtomUtils.splitImage(AtomUtils.createStream(imageMap, getClass()), cols, rows,0,0));
    }

    public ImageScreenLayer(int zIndex, boolean mapAligned, String... images) {
        Image[] im = new Image[images.length];
        for (int j = 0; j < im.length; j++) {
            im[j] = AtomUtils.createImage(images[j], getClass());
        }
        init(zIndex, im);
    }

    public ImageScreenLayer(int zIndex, boolean mapAligned, Image... images) {
        init(zIndex, images);
    }

    public boolean isViewPortImage() {
        return viewPortImage;
    }

    public void setViewPortImage(boolean viewPortImage) {
        this.viewPortImage = viewPortImage;
        resizedImages = null;
    }

    public boolean isResizeImages() {
        return resizeImages;
    }

    public void setResizeImages(boolean resizeImages) {
        this.resizeImages = resizeImages;
        resizedImages = null;
    }

    private void init(int zIndex, Image[] images) {
        setResizeImages(true);
        setLayer(zIndex);
        this.images = new ResizableImage[images.length];
        for (int i = 0; i < images.length; i++) {
            this.images[i] = new ResizableImage(images[i]);

        }
        imageWidth = images[0].getWidth(null);
        imageHeight = images[0].getHeight(null);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene view = context.getScene();
        AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
        graphics.drawImage(getImage(view), 0, 0, null);
    }

    public int getImageIndex(Scene scene) {
        return (int) (Math.abs(scene.getFrame()) % images.length);
    }

    public Image getImage(Scene view) {
        return getImage(view, getImageIndex(view));
    }

    public Image getImage(Scene scene, int index) {
        index = index % images.length;
        Image defaultImage = getDefaultImage(index);
        if (isResizeImages()) {
            SceneModel m = scene.getModel();
            ViewBox viewBox = scene.getCamera().getViewPort();
            ViewDimension mg = m.getSceneSize();
            int gw = isViewPortImage() ? viewBox.getWidth() : mg.getWidth();
            int gh = isViewPortImage() ? viewBox.getHeight() : mg.getHeight();
            if (cachedImageWidth < 0 || cachedImageHeight < 0 || cachedImageWidth != gw || cachedImageHeight != gh) {
                cachedImageWidth = gw;
                cachedImageHeight = gh;
                resizedImages = new Image[images.length];
            }
            Image ii = resizedImages[index];
            if (ii == null) {
                return resizedImages[index] = images[index].getImage(gw, gh);
            } else {
                return ii;
            }
        } else {
            return defaultImage;
        }
    }

    public Image getDefaultImage(int index) {
        return images[index].getBaseImage();
    }

    public int getImagesCount() {
        return images.length;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }
}
