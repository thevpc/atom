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
public class PlaneViewOne extends ImageSpriteView {

    public PlaneViewOne() {
        super(
           new Image[]{
                    new ImageIcon(PlayerView.class.getResource("plane1_0.png")).getImage()
                   ,new ImageIcon(PlayerView.class.getResource("plane1_1.png")).getImage()
                   ,new ImageIcon(PlayerView.class.getResource("plane1_2.png")).getImage()
                   ,new ImageIcon(PlayerView.class.getResource("plane1_3.png")).getImage()
                   //,new ImageIcon(PlayerView.class.getResource("plane1_2.png")).getImage()
           }
        );
    }
}
