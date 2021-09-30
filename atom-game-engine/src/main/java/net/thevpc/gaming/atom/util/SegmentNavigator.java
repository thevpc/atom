package net.thevpc.gaming.atom.util;

import net.thevpc.gaming.atom.model.ModelPoint;

public class SegmentNavigator {
    private ModelPoint from;
    private ModelPoint to;

    public SegmentNavigator(ModelPoint from, ModelPoint to) {
        this.from = from;
        this.to = to;
    }

    public ModelPoint getAbsolutePoint(double distance) {
        return GeometryUtils.nextLinePoint(from, to, distance);
    }

    public ModelPoint getRelativePoint(float f) {
        if (from.equals(to)) {
            return from;
        }
        return GeometryUtils.nextLinePoint(from, to, length() * f);
    }

    public double length() {
        double a = to.getX() - from.getX();
        double b = to.getY() - from.getY();
        return Math.sqrt(a * a + b * b);
    }
}
