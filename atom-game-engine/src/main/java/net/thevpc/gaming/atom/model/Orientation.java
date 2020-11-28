/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public enum Orientation implements Direction {

    NORTH(0, -Math.PI / 2),
    WEST(0, Math.PI),
    EAST(0, 0),
    SOUTH(0, Math.PI / 2),
    NORTH_EAST(0, -Math.PI / 4),
    NORTH_WEST(0, -3 * Math.PI / 4),
    SOUTH_EAST(0, Math.PI / 4),
    SOUTH_WEST(0, 3 * Math.PI / 4);
    private double factor;
    private double phase;

    private Orientation(double factor, double phase) {
        this.factor = factor;
        this.phase = phase;
    }

    public double getPhase() {
        return phase;
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    public double getFactor() {
        return factor;
    }

    @Override
    public double getDirectionAngle(double angle) {
        return angle * factor + phase;
    }

    public static Orientation forStep(int x, int y) {
        if (x == 0) {
            if (y == 0) {
                return null;
            } else if (y > 0) {
                return Orientation.NORTH;
            } else if (y < 0) {
                return Orientation.SOUTH;
            }
        } else if (x > 0) {
            if (y == 0) {
                return Orientation.EAST;
            } else if (y > 0) {
                return Orientation.NORTH_EAST;
            } else if (y < 0) {
                return Orientation.SOUTH_EAST;
            }
        } else if (x < 0) {
            if (y == 0) {
                return Orientation.WEST;
            } else if (y > 0) {
                return Orientation.NORTH_WEST;
            } else if (y < 0) {
                return Orientation.SOUTH_WEST;
            }
        }
        return null;
    }

    public static Orientation forStepNESW(double theta, boolean isometric) {
        if (isometric) {
            theta = theta - Math.PI / 2;
        }
        final double TWICE_PI = 2 * Math.PI;
        if (theta < 0) {
            theta += TWICE_PI;
        }
        while (theta > TWICE_PI) {
            theta -= TWICE_PI;
        }
        double directionCanon = (theta / TWICE_PI) * 8;

        Orientation dir = Orientation.SOUTH;
        if (directionCanon < 1) {
            dir = Orientation.EAST;
        } else if (directionCanon < 3) {
            dir = Orientation.SOUTH;
        } else if (directionCanon < 5) {
            dir = Orientation.WEST;
        } else if (directionCanon < 7) {
            dir = Orientation.NORTH;
        } else {
            dir = Orientation.EAST;
        }
        return dir;
    }

    //    public static Orientation8 forStepEW(double theta, boolean isometric) {
//        if (isometric) {
//            theta = theta - Math.PI / 2;
//        }
//        final double TWICE_PI = 2 * Math.PI;
//        if (theta < 0) {
//            theta += TWICE_PI;
//        }
//        while (theta > TWICE_PI) {
//            theta -= TWICE_PI;
//        }
//        double directionCanon = (theta / TWICE_PI) * 8;
//
//        Orientation8 dir = Orientation8.RIGHT;
//        if (directionCanon < 2) {
//            dir = Orientation8.RIGHT;
//        } else if (directionCanon < 6) {
//            dir = Orientation8.LEFT;
//        } else {
//            dir = Orientation8.RIGHT;
//        }
//        return dir;
//    }
//
//    public static Orientation8 forStepLR(double theta, boolean isometric) {
//        if (isometric) {
//            theta = theta - Math.PI / 2;
//        }
//        final double TWICE_PI = 2 * Math.PI;
//        if (theta < 0) {
//            theta += TWICE_PI;
//        }
//        while (theta > TWICE_PI) {
//            theta -= TWICE_PI;
//        }
//        double directionCanon = (theta / TWICE_PI) * 8;
//
//        Orientation8 dir = Orientation8.EAST;
//        if (directionCanon < 2) {
//            dir = Orientation8.EAST;
//        } else if (directionCanon < 6) {
//            dir = Orientation8.WEST;
//        } else {
//            dir = Orientation8.EAST;
//        }
//        return dir;
//    }
}
