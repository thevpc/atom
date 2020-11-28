/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.pathfinder;

import net.thevpc.gaming.atom.model.ModelPoint;

/**
 * Available Moves from a Point
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface TransitionStrategy {

    /**
     * Available Moves from a Point
     *
     * @param from from point
     * @return Possible moves
     */
    public Transition[] getTransitions(ModelPoint from);
}
