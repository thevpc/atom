/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.shared.presentation.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import net.thevpc.gaming.atom.annotations.AtomSpriteView;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.DefaultSpriteView;
import net.thevpc.gaming.atom.presentation.SpriteDrawingContext;

/**
 *
 * @author Taha Ben Salah
 */
@AtomSpriteView(
        kind = "Bullet"
)
public class BulletView extends DefaultSpriteView{

    @Override
    public void draw(SpriteDrawingContext context) {
        Graphics2D stylo = context.getGraphics();
        Sprite s = context.getSprite();
        Rectangle r=context.getSpriteBounds().toRectangle();
        stylo.setColor(Color.RED.darker());
        stylo.fillOval(r.x+r.width/4, r.y+r.height/4, r.width/2, r.height/2);
        stylo.setColor(Color.RED.brighter());
        stylo.drawOval(r.x+r.width/4, r.y+r.height/4, r.width/2, r.height/2);
    }
    
}
