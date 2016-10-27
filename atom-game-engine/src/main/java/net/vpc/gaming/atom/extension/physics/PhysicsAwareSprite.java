/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension.physics;

import net.vpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah
 */
public interface PhysicsAwareSprite extends Sprite {
    public double getMass();

    public float getElasticity();
}
