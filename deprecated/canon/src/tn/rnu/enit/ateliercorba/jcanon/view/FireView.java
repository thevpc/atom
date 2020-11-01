package tn.rnu.enit.ateliercorba.jcanon.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.SpriteView;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.ImageSpriteView;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;
import tn.rnu.enit.ateliercorba.jcanon.model.FireSprite;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:24:32
 */
public class FireView
        implements SpriteView
{

    public FireView() {
    }


    public void draw(Sprite sprite, Graphics2D graphics) {
        FireSprite psprite =(FireSprite) sprite;
        graphics.setColor(psprite.getPlayer()==1?Color.RED:Color.WHITE);
        graphics.drawRoundRect(
                sprite.getX(),
                sprite.getY(),
                sprite.getWidth(),
                sprite.getHeight(),
                2,
                2
                );
        graphics.setColor(psprite.getPlayer()!=1?Color.RED:Color.WHITE);
        graphics.fillRoundRect(
                sprite.getX(),
                sprite.getY(),
                sprite.getWidth(),
                sprite.getHeight(),
                2,
                2
                );
    }
}
