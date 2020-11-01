package tn.rnu.enit.ateliercorba.jcanon.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.ImageSpriteView;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;
import tn.rnu.enit.ateliercorba.jcanon.model.PlayerSprite;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class PlayerView extends ImageSpriteView {

    public PlayerView() {
        super(
           new Image[]{
                   new ImageIcon(PlayerView.class.getResource("player1_0.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("player1_1.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("player1_2.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("player2_0.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("player2_1.png")).getImage()
                  ,new ImageIcon(PlayerView.class.getResource("player2_2.png")).getImage()
           }
        );
    }

    public Image getImage(Sprite sprite){
       PlayerSprite p=(PlayerSprite) sprite;
       int player=p.getPlayer();
       int mood=p.getMood();
       return getImage(mood+(player==1?0:3));
    }
}
