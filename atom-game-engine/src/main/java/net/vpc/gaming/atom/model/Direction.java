/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

/**
 * @author Taha Ben Salah
 */
public interface Direction {
    public double getDirectionAngle(double oldDirection);

    public boolean isRelative();
}
