package tn.rnu.enit.ateliercorba.jcanon.gameengine.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 11:18:25
 */
public class SpriteContainer extends JComponent {
    private Collection<Sprite> sprites;
    private GameRendererManager gameRendererManager;


    public SpriteContainer(GameRendererManager gameRendererManager, int width, int heigth) {
        this.gameRendererManager = gameRendererManager;
        setPreferredSize(new Dimension(width, heigth));
    }

    public Collection<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(Collection<Sprite> sprites) {
        this.sprites = sprites;
        repaint();
    }


    public void paint(Graphics g) {
        Dimension s = getSize();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, s.width, s.height);
        paintSprites((Graphics2D) g);
    }

    public void paintSprites(Graphics2D g) {
        if (sprites != null) {
            for (Sprite sprite : sprites) {
                gameRendererManager.getSpriteView(sprite).draw(sprite, g);
            }
        }
    }
}
