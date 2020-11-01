package tn.rnu.enit.ateliercorba.jcanon.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.SpriteView;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.ImageSpriteView;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;
import tn.rnu.enit.ateliercorba.jcanon.model.PlaneSprite;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class PlaneViewTwo extends ImageSpriteView {
    public PlaneViewTwo() {
        super(
           new Image[]{
                   new ImageIcon(PlayerView.class.getResource("plane2_0.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("plane2_1.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("plane2_2.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("plane2_3.png")).getImage()
           }
        );
    }
}
