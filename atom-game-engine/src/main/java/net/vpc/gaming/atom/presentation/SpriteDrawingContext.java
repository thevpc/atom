/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewBox;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpriteDrawingContext {

    private Sprite sprite;
    private Scene scene;
    private Graphics2D graphics;
    private SpriteView spriteView;

    public SpriteDrawingContext(Sprite sprite, Scene view, Graphics2D graphics,SpriteView spriteView) {
        this.sprite = sprite;
        this.scene = view;
        this.graphics = graphics;
        this.spriteView = spriteView;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Scene getScene() {
        return scene;
    }

    public ViewBox getSpriteBounds() {
        return new ViewBox(getSpriteShape().getBounds());
    }

    public Shape getSpriteShape() {
        return spriteView.getShape(sprite,scene);
    }
}
