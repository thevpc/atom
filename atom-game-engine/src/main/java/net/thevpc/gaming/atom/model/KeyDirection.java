/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public enum KeyDirection implements Direction {

    UP(0, -Math.PI / 2, false),

    LEFT(0, Math.PI, false),

    RIGHT(0, 0, false),

    DOWN(0, Math.PI / 2, false),


    UP_RIGHT(0, -Math.PI / 4, false),

    UP_LEFT(0, -3 * Math.PI / 4, false),

    DOWN_RIGHT(0, Math.PI / 4, false),

    DOWN_LEFT(0, 3 * Math.PI / 4, false),

    HORIZONTAL_MIRROR(-1, 2 * Math.PI, true),
    VERTICAL_MIRROR(-1, Math.PI, true),
    BACKWARD(1, Math.PI, true);

    private boolean relative;
    private double factor;
    private double phase;

    private KeyDirection(double factor, double phase, boolean relative) {
        this.factor = factor;
        this.phase = phase;
        this.relative = relative;
    }

    @Override
    public boolean isRelative() {
        return relative;
    }


    public double getFactor() {
        return factor;
    }

    public double getDirectionAngle(double angle) {
        return angle * factor + phase;
    }

    public static KeyDirection forStep(int x, int y) {
        if (x == 0) {
            if (y == 0) {
                return null;
            } else if (y > 0) {
                return KeyDirection.UP;
            } else if (y < 0) {
                return KeyDirection.DOWN;
            }
        } else if (x > 0) {
            if (y == 0) {
                return KeyDirection.RIGHT;
            } else if (y > 0) {
                return KeyDirection.UP_RIGHT;
            } else if (y < 0) {
                return KeyDirection.DOWN_RIGHT;
            }
        } else if (x < 0) {
            if (y == 0) {
                return KeyDirection.LEFT;
            } else if (y > 0) {
                return KeyDirection.UP_LEFT;
            } else if (y < 0) {
                return KeyDirection.DOWN_LEFT;
            }
        }
        return null;
    }

//    public static Key8 forStepNESW(double theta, boolean isometric) {
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
//        Key8 dir = Key8.SOUTH;
//        if (directionCanon < 1) {
//            dir = Key8.EAST;
//        } else if (directionCanon < 3) {
//            dir = Key8.SOUTH;
//        } else if (directionCanon < 5) {
//            dir = Key8.WEST;
//        } else if (directionCanon < 7) {
//            dir = Key8.NORTH;
//        } else {
//            dir = Key8.EAST;
//        }
//        return dir;
//    }
//
//    public static Key8 forStepEW(double theta, boolean isometric) {
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
//        Key8 dir = Key8.RIGHT;
//        if (directionCanon < 2) {
//            dir = Key8.RIGHT;
//        } else if (directionCanon < 6) {
//            dir = Key8.LEFT;
//        } else {
//            dir = Key8.RIGHT;
//        }
//        return dir;
//    }
//
//    public static Key8 forStepLR(double theta, boolean isometric) {
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
//        Key8 dir = Key8.EAST;
//        if (directionCanon < 2) {
//            dir = Key8.EAST;
//        } else if (directionCanon < 6) {
//            dir = Key8.WEST;
//        } else {
//            dir = Key8.EAST;
//        }
//        return dir;
//    }
}
