package tn.rnu.enit.ateliercorba.jcanon.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.ImageSpriteView;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class BombView extends ImageSpriteView {

    public BombView() {
        super(
           new Image[]{
                    new ImageIcon(PlayerView.class.getResource("bomb1_0.png")).getImage()
                   ,new ImageIcon(PlayerView.class.getResource("bomb1_1.png")).getImage()
                   ,new ImageIcon(PlayerView.class.getResource("bomb1_2.png")).getImage()
                   //,new ImageIcon(PlayerView.class.getResource("plane1_2.png")).getImage()
           }
        );
    }
}