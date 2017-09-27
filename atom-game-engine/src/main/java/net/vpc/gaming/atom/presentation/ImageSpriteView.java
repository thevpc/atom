package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.util.AtomUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 10:57:33
 */
public class ImageSpriteView extends DefaultSpriteView {

    private ResizableImage[] images;
    private ModelPoint margin;
    private int framesPerImage = 1;
    private SpriteViewImageSelector imageSelector = FrameAnimatedImageSelector.INSTANCE;

    public ImageSpriteView(String imageMap, int cols, int rows,SpriteViewImageSelector imageSelector) {
        addImages(imageMap, cols, rows);
        setImageSelector(imageSelector);
    }

    public ImageSpriteView(String imageMap, int cols, int rows) {
        addImages(imageMap, cols, rows);
    }

    public ImageSpriteView(Class baseClass, String imageMap, int rows, int cols) {
        addImages(baseClass, imageMap, cols, rows);
    }

    public ImageSpriteView(String... images) {
        addImages(images);
    }

    public ImageSpriteView(Image... images) {
        setImages(images);
    }

    public ModelPoint getMargin() {
        return margin;
    }

    public void setMargin(ModelPoint margin) {
        this.margin = margin;
    }

    public int getFramesPerImage() {
        return framesPerImage;
    }

    public void setFramesPerImage(int framesPerImage) {
        this.framesPerImage = framesPerImage;
    }

    public void addImages(String imageMap, int cols, int rows) {
        addImages(AtomUtils.splitImage(imageMap, getClass(), cols, rows));
    }

    public void addImages(Class baseClass, String imageMap, int cols, int rows) {
        addImages(AtomUtils.splitImage(AtomUtils.createStream(imageMap, baseClass), cols, rows));
    }

    public void addImages(String... images) {
        Image[] im = new Image[images.length];
        for (int j = 0; j < im.length; j++) {
            im[j] = AtomUtils.createImage(images[j], getClass());
        }
        addImages(im);
    }

    public void addImages(Image... images) {
        ArrayList<ResizableImage> list = new ArrayList<>();
        if (this.images != null) {
            for (ResizableImage image : this.images) {
                list.add(image);
            }
        }
        if (images != null) {
            for (Image image : images) {
                list.add(new ResizableImage(image));
            }
        }
        this.images = list.toArray(new ResizableImage[list.size()]);
    }

    public void clearImages() {
        this.images = new ResizableImage[0];
    }

    public void setImages(Image[] images) {
        int size = images == null ? 0 : images.length;
        this.images = new ResizableImage[size];
        for (int i = 0; i < size; i++) {
            this.images[i] = new ResizableImage(images[i]);
        }
    }

    public void draw(SpriteDrawingContext context) {
        Sprite sprite = context.getSprite();
        Scene view = context.getScene();
        Graphics2D graphics = context.getGraphics();

        ViewBox rb = new ViewBox(context.getSpriteBounds());

        Image image = getImage(sprite, view, rb.getWidth(), rb.getHeight());

        //AGEDebug.DRAW_IMAGE_DRAW_COUNT++;
        //System.out.println(">> "+sprite.getId());
        graphics.drawImage(image, rb.getX(), rb.getY(), null);
//        graphics.setColor(Color.RED);
//        graphics.drawRect(b.getX(), b.getY(),b.getWidth(),b.getHeight());
//        graphics.setColor(Color.BLUE);
//        graphics.drawRect(t.getX(), t.getY(),t.getWidth(),t.getHeight());
//        graphics.setColor(Color.GREEN);
//        graphics.drawRect(f.getX(), f.getY(),f.getWidth(),f.getHeight());
//        if (debug) {
//            graphics.setColor(Color.red);
//            int swidth = (int)sprite.getWidth();
//            int sheight = (int)sprite.getHeight();
//            graphics.drawRect(position.x, position.y, swidth, sheight);
//            graphics.setColor(Color.YELLOW);
//            graphics.drawRect(position.x + 1, position.y + 1, swidth - 2, sheight - 2);
//            graphics.setColor(Color.red);

//            graphics.setColor(Color.BLUE);
//            graphics.drawString(sprite.toString(), rb.getX(), rb.getY());
//        }
    }

    public Image getImage(Sprite sprite, Scene scene, int width, int height) {
        long frame = scene.getFrame();
        int fps = getFramesPerImage();
        if (fps > 0) {
            frame = frame / fps;
        }
        int index = getImageSelector().getImageIndex(sprite, scene, frame, images.length);
        return getImage(index, width, height);
    }

    public Image getImage(int index, int width, int height) {
        index = index % images.length;
//        if (isResizeImages()) {
        return images[index].getImage(width, height);
//        } else {
//            return images[index].baseImage;
//        }
    }

    public Image getDefaultImage(int index) {
        return images[index].getBaseImage();
    }

    public int getImagesCount() {
        return images.length;
    }

//    public int getImageHeight() {
//        return imageHeight;
//    }
//
//    public int getImageWidth() {
//        return imageWidth;
//    }
//

    public SpriteViewImageSelector getImageSelector() {
        return imageSelector;
    }

    public void setImageSelector(SpriteViewImageSelector imageSelector) {
        this.imageSelector = imageSelector == null ? FrameAnimatedImageSelector.INSTANCE : imageSelector;
    }
}
