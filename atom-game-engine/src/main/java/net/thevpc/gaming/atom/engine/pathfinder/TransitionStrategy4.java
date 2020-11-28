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
 * Simple Move Strategy according to the 4 directions UP,DOWN,LEFT end RIGHT
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TransitionStrategy4 extends AbstractTransitionStrategy {

    public TransitionStrategy4(SceneEngine scene, Sprite sprite, double maxMovesSight) {
        super(scene, sprite, maxMovesSight);
    }

    public Transition[] getTransitions(ModelPoint from) {
        ArrayList<Transition> moves = new ArrayList<Transition>(4);
        double xx = from.getX();
        double yy = from.getY();
        double speed = getSprite().getSpeed();
        addIfValid(from, new ModelPoint(xx - speed, yy), moves);
        addIfValid(from, new ModelPoint(xx + speed, yy), moves);
        addIfValid(from, new ModelPoint(xx, yy - speed), moves);
        addIfValid(from, new ModelPoint(xx, yy + speed), moves);
        return moves.toArray(new Transition[moves.size()]);
    }
}
