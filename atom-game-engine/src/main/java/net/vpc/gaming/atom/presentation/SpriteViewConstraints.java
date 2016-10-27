/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.ViewPoint;

/**
 * @author vpc
 */
public interface SpriteViewConstraints {

    public boolean isIsometric();

    public SceneLayoutType getSceneLayoutType();

    public ViewPoint getMargin();
}
