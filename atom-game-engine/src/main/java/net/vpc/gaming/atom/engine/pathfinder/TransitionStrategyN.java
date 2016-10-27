/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine.pathfinder;

import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Sprite;

import java.util.ArrayList;

/**
 * Simple Move Strategy according to the 8 directions NORTH,NORTH_EAST,EAST, ...
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class TransitionStrategyN extends AbstractTransitionStrategy {
    private int n;
    private double[] dx;
    private double[] dy;

    public TransitionStrategyN(SceneEngine scene, Sprite sprite, int n, double maxMovesSight) {
        super(scene, sprite, maxMovesSight);
        this.n = n;
        dx = new double[n];
        dy = new double[n];
        for (int i = 0; i < n; i++) {
            dx[i] = Math.cos(2 * i * Math.PI / n);
            dy[i] = Math.sin(2 * i * Math.PI / n);
        }
    }

    public Transition[] getTransitions(ModelPoint from) {
        ArrayList<Transition> moves = new ArrayList<Transition>(8);
        double xx = from.getX();
        double yy = from.getY();
        double speed = getSprite().getSpeed();
        for (int i = 0; i < n; i++) {
            addIfValid(from, new ModelPoint(xx + dx[i] * speed, yy + dy[i] * speed), moves);
        }
        Transition[] ok = moves.toArray(new Transition[moves.size()]);
        Transition[] ok8 = null;
        if (n == 8) {
            ArrayList<Transition> moves8 = new ArrayList<Transition>(8);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        addIfValid(from, new ModelPoint(xx + i * speed, yy + j * speed), moves8);
                    }
                }
            }
            ok8 = moves8.toArray(new Transition[moves8.size()]);
        }
        return ok;
    }
}
