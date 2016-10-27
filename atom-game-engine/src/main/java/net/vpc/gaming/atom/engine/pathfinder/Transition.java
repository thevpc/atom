/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.pathfinder;

import net.vpc.gaming.atom.model.ModelPoint;

/**
 * the Move/Transition that can be used by the Path Finding Algorithm
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface Transition {

    /**
     * from point
     *
     * @return from point
     */
    public ModelPoint getFrom();

    /**
     * to point
     *
     * @return to point
     */
    public ModelPoint getTo();

    /**
     * move cost
     *
     * @return move cost
     */
    public double getCost();
}
