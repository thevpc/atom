/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

import net.vpc.gaming.atom.util.GeometryUtils;

/**
 * @author Taha Ben Salah
 */
public enum OrientationType {

    VERTICAL_ORIENTATION,
    HORIZONTAL_ORIENTATION,
    PLUS_ORIENTATION,
    STAR_ORIENTATION;

    public Orientation getOrientation(double theta, boolean isometric) {
        if (isometric) {
            theta = GeometryUtils.getUniformAngle(theta - Math.PI / 4);
        } else {
            theta = GeometryUtils.getUniformAngle(theta);
        }
        final double TWICE_PI = 2 * Math.PI;
        switch (this) {
            case HORIZONTAL_ORIENTATION: {
                double quotient = (theta / TWICE_PI) * 4;

                Orientation dir = Orientation.EAST;
                if (quotient < 1) {
                    dir = Orientation.EAST;
                } else if (quotient < 3) {
                    dir = Orientation.WEST;
                } else {
                    dir = Orientation.EAST;
                }
                return dir;
            }
            case VERTICAL_ORIENTATION: {
                double quotient = (theta / TWICE_PI) * 4;

                Orientation dir = Orientation.EAST;
                if (quotient < 2) {
                    dir = Orientation.SOUTH;
                } else {
                    dir = Orientation.NORTH;
                }
                return dir;
            }
            case PLUS_ORIENTATION: {
                double quotient = (theta / TWICE_PI) * 8;

                Orientation dir = Orientation.EAST;
                if (quotient < 1) {
                    dir = Orientation.EAST;
                } else if (quotient < 3) {
                    dir = Orientation.SOUTH;
                } else if (quotient < 5) {
                    dir = Orientation.WEST;
                } else if (quotient < 7) {
                    dir = Orientation.NORTH;
                } else {
                    dir = Orientation.EAST;
                }
                return dir;
            }
//            case STAR_ORIENTATION: {
//                double quotient = (theta / TWICE_PI) * 16;
//
//                Orientation dir = Orientation.EAST;
//                if (quotient < 1) {
//                    dir = Orientation.EAST;
//                } else if (quotient < 3) {
//                    dir = Orientation.SOUTH_EAST;
//                } else if (quotient < 5) {
//                    dir = Orientation.SOUTH;
//                } else if (quotient < 7) {
//                    dir = Orientation.SOUTH_WEST;
//                } else if (quotient < 9) {
//                    dir = Orientation.WEST;
//                } else if (quotient < 11) {
//                    dir = Orientation.NORTH_WEST;
//                } else if (quotient < 13) {
//                    dir = Orientation.NORTH;
//                } else if (quotient < 15) {
//                    dir = Orientation.NORTH_EAST;
//                } else {
//                    dir = Orientation.EAST;
//                }
//                return dir;
//            }
            case STAR_ORIENTATION: {
                double quotient = (theta / TWICE_PI) * 32;

                Orientation dir;
                if (quotient < 1) {
                    dir = Orientation.EAST;
                } else if (quotient < 7) {
                    dir = Orientation.SOUTH_EAST;
                } else if (quotient < 9) {
                    dir = Orientation.SOUTH;
                } else if (quotient < 15) {
                    dir = Orientation.SOUTH_WEST;
                } else if (quotient < 17) {
                    dir = Orientation.WEST;
                } else if (quotient < 23) {
                    dir = Orientation.NORTH_WEST;
                } else if (quotient < 25) {
                    dir = Orientation.NORTH;
                } else if (quotient < 31) {
                    dir = Orientation.NORTH_EAST;
                } else {
                    dir = Orientation.EAST;
                }
                return dir;
            }
        }

        throw new UnsupportedOperationException();
    }
}
