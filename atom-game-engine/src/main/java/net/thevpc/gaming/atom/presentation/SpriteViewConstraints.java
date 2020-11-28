/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.model.ViewPoint;

/**
 * @author vpc
 */
public interface SpriteViewConstraints {

    public boolean isIsometric();

    public SceneLayoutType getSceneLayoutType();

    public ViewPoint getMargin();
}
