/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine.pathfinder;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;

import java.util.ArrayList;

/**
 * Simple Move Strategy according to the 8 directions NORTH,NORTH_EAST,EAST, ...
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TransitionStrategy8 extends AbstractTransitionStrategy {

    public TransitionStrategy8(SceneEngine scene, Sprite sprite, double maxMovesSight) {
        super(scene, sprite, maxMovesSight);
    }

    public Transition[] getTransitions(ModelPoint from) {
        ArrayList<Transition> moves = new ArrayList<Transition>(8);
        double xx = from.getX();
        double yy = from.getY();
        //double speed = getSprite().getSpeed();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    addIfValid(from, new ModelPoint((int) (xx + i), (int) (yy + j)), moves);
                }
            }
        }
        return moves.toArray(new Transition[moves.size()]);
    }
}
