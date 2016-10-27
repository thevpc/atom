/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.pathfinder;

import net.vpc.gaming.atom.model.ModelPoint;

/**
 * default implementation of the Move contract
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultTransition implements Transition {
    private ModelPoint from;
    private ModelPoint to;
    private double cost;

    public DefaultTransition(ModelPoint from, ModelPoint to, double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public ModelPoint getFrom() {
        return from;
    }

    public ModelPoint getTo() {
        return to;
    }

}
