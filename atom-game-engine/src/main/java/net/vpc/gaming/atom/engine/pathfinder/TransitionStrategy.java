/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.pathfinder;

import net.vpc.gaming.atom.model.ModelPoint;

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
