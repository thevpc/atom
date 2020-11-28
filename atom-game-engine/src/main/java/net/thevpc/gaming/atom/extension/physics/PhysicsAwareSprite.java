/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.physics;

import net.thevpc.gaming.atom.model.Sprite;

/**
 * @author Taha Ben Salah
 */
public interface PhysicsAwareSprite extends Sprite {
    public double getMass();

    public float getElasticity();
}
