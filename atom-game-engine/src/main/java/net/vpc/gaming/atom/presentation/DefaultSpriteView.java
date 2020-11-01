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
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class DefaultSpriteView implements SpriteView {
    private DefaultSpriteViewConstraints constraints = new DefaultSpriteViewConstraints();

    public DefaultSpriteView() {
    }

    public ViewPoint getLocation(Sprite sprite, Scene scene) {
        return new ViewBox(getShape(sprite, scene).getBounds()).getLocation();
    }

    public ViewBox getBounds(Sprite sprite, Scene scene) {
        return new ViewBox(getShape(sprite, scene).getBounds());
    }

    @Override
    public Shape getShape(Sprite sprite, Scene scene) {
        SpriteViewConstraints c = getSpriteViewConstraints(sprite);
        ViewBox b = scene.getLayoutBox(scene.toViewBox(sprite), getConstraints().isIsometric(), c.getSceneLayoutType(), scene.getScreenAffineTransform());
        ViewPoint m = c.getMargin();
        if (m != null) {
            return new ViewBox(
                    b.getX() + m.getX(),
                    b.getY() + m.getY(),
                    b.getZ() + m.getZ(),
                    b.getWidth(),
                    b.getHeight(),
                    b.getAltitude()
            ).toRectangle();
        }
        return b.toRectangle();
    }

    protected DefaultSpriteViewConstraints getConstraints() {
        return constraints;
    }

    public void setSceneLayoutType(SceneLayoutType sceneLayoutType) {
        getConstraints().setSceneLayoutType(sceneLayoutType);
    }

    @Override
    public SpriteViewConstraints getSpriteViewConstraints(Sprite sprite) {
        return getConstraints();
    }
}
