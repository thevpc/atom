/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.Sprite;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpriteDrawingContext {

    private Sprite sprite;
    private Scene scene;
    private Graphics2D graphics;

    public SpriteDrawingContext(Sprite sprite, Scene view, Graphics2D graphics) {
        this.sprite = sprite;
        this.scene = view;
        this.graphics = graphics;
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
}
