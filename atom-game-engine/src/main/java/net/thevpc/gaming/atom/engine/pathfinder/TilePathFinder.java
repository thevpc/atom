/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.pathfinder;

import net.thevpc.gaming.atom.model.ModelPoint;

import java.io.Serializable;

/**
 * Path Finder Algorithm contract
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface TilePathFinder extends Serializable {
    /**
     * @param start              start point
     * @param end                end point
     * @param transitionStrategy transition Strategy
     * @return Path from <code>start</code> to <code>end</code>
     */
    ModelPoint[] findPath(ModelPoint start, ModelPoint end, TransitionStrategy transitionStrategy);
}
