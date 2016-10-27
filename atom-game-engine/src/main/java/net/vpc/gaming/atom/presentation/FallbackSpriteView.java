/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewPoint;

import java.awt.*;

/**
 * @author Taha Ben Salah
 */
public class FallbackSpriteView extends DefaultSpriteView {

    @Override
    public void draw(SpriteDrawingContext context) {
        Sprite sprite = context.getSprite();
        Scene scene = context.getScene();
        ViewPoint position = getLocation(sprite, scene);

        Graphics2D graphics = context.getGraphics();

        ViewBox viewBounds = scene.toViewBox(sprite);
        final int value = sprite.getClass().getName().hashCode();
//        byte r = (byte) ((value >> 24) & 0xff);
//        byte g = (byte) ((value >> 16) & 0xff);
//        byte b = (byte) ((value >> 8) & 0xff);
//        byte h = (byte) ((value >> 0) & 0xff);
        graphics.setColor(new Color(value, false));
        graphics.fillRect(position.getX(), position.getY(), viewBounds.getWidth(), viewBounds.getHeight());
    }

}
