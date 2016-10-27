/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

/**
 * @author Taha Ben Salah
 */
public enum DirectionTransform implements Direction {

    HORIZONTAL_MIRROR(-1, 2 * Math.PI),
    VERTICAL_MIRROR(-1, Math.PI),
    BACKWARD(1, Math.PI);
    private double factor;
    private double phase;

    private DirectionTransform(double factor, double phase) {
        this.factor = factor;
        this.phase = phase;
    }

    @Override
    public double getDirectionAngle(double angle) {
        return angle * factor + phase;
    }

    @Override
    public boolean isRelative() {
        return true;
    }
}
