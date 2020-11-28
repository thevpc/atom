/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.model.ViewPoint;

/**
 * @author vpc
 */
public class DefaultSpriteViewConstraints implements SpriteViewConstraints {

    private boolean isometric=true;
    private SceneLayoutType sceneLayoutType = SceneLayoutType.FULL_BOUNDS;
    private ViewPoint margin;

    public DefaultSpriteViewConstraints() {
    }

    @Override
    public boolean isIsometric() {
        return isometric;
    }

    public void setIsometric(boolean isometric) {
        this.isometric = isometric;
    }

    @Override
    public SceneLayoutType getSceneLayoutType() {
        return sceneLayoutType;
    }

    public void setSceneLayoutType(SceneLayoutType sceneLayoutType) {
        this.sceneLayoutType = sceneLayoutType;
    }

    @Override
    public ViewPoint getMargin() {
        return margin;
    }

    public void setMargin(ViewPoint margin) {
        this.margin = margin;
    }
}
