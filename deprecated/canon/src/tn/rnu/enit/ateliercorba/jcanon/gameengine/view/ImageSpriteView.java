package tn.rnu.enit.ateliercorba.jcanon.gameengine.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 10:57:33
 */
public class ImageSpriteView extends JComponent implements SpriteView {

    private Image[] images;

    public ImageSpriteView(String[] images) {
        Image[] im = new Image[images.length];
        for (int j = 0; j < im.length; j++) {
            im[j] = new ImageIcon(getClass().getResource(images[j])).getImage();
        }
        init(im);
    }

    public ImageSpriteView(Image[] images) {
        init(images);
    }

    private void init(Image[] images) {
        this.images = images;
        setSize(images[0].getWidth(this), images[0].getHeight(this));
    }

    public void draw(Sprite sprite, Graphics2D graphics) {
        Image image = getImage(sprite);
        graphics.drawImage(image, sprite.getX(), sprite.getY(), this);
    }

    public Image getImage(Sprite sprite) {
        int mood = sprite.getMood();
        if (mood < 0 || mood >= images.length) {
            mood = 0;
        }
        return getImage(mood);
    }

    public Image getImage(int index) {
        return images[index];
    }
}
