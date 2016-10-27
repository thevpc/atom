/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.model;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ModelSegment {

    private ModelPoint a;
    private ModelPoint b;

    public ModelSegment(double ax, double ay, double bx, double by) {
        this(new ModelPoint(ax, ay, 0), new ModelPoint(bx, by, 0));
    }

    public ModelSegment(ModelPoint a, ModelPoint b) {
        this.a = a;
        this.b = b;
    }

    public ModelPoint getCenter() {
        return new ModelPoint((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2, (a.getZ() + b.getZ()) / 2);
    }

    public ModelPoint getA() {
        return a;
    }

    public ModelPoint getB() {
        return b;
    }

    @Override
    public String toString() {
        return "(" + a.getX() + "," + a.getY() + "->" + b.getX() + "," + b.getY() + ")";
    }

    public ModelPoint intersect(ModelSegment other) {
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();
        double x3 = other.a.getX();
        double y3 = other.a.getY();
        double x4 = other.b.getX();
        double y4 = other.b.getY();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
        ModelPoint p = new ModelPoint(xi, yi, 0);
        if (xi < Math.min(x1, x2) || xi > Math.max(x1, x2)) {
            if (!isZero(xi - Math.min(x1, x2), a, b, other.a, other.b)) {
                return null;
            } else if (!isZero(xi - Math.max(x1, x2), a, b, other.a, other.b)) {
                return null;
            }
        }
        if (xi < Math.min(x3, x4) || xi > Math.max(x3, x4)) {
            if (!isZero(xi - Math.min(x3, x4), a, b, other.a, other.b)) {
                return null;
            } else if (!isZero(xi - Math.max(x3, x4), a, b, other.a, other.b)) {
                return null;
            }
        }
        return p;
    }

    private boolean isZero(double d, ModelPoint... points) {
        double e = minDistance(points);
        if (Double.isInfinite(e)) {
            e = 10E-7;
        } else {
            e = e * 10E-7;
        }
        return d >= 0 ? d < e : d > e;
    }

    private double minDistance(ModelPoint... points) {
        double e = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {

            for (int j = 0; j < points.length; j++) {
                double d = points[i].distance(points[j]);
                if (d > 0 && d < e) {
                    e = d;
                }
            }
        }
        return e;
    }

    public ModelPoint intersect2(ModelSegment other) {
        double Ax = a.getX();
        double Ay = a.getY();
        double Bx = b.getX();
        double By = b.getY();
        double Cx = other.a.getX();
        double Cy = other.a.getY();
        double Dx = other.b.getX();
        double Dy = other.b.getY();
        double num1 = (Ay - Cy) * (Dx - Cx) - (Ax - Cx) * (Dy - Cy);
        double den1 = (Bx - Ax) * (Dy - Cy) - (By - Ay) * (Dx - Cx);

        double num2 = (Ay - Cy) * (Bx - Ax) - (Ax - Cx) * (By - Ay);
        double den2 = den1;

        if (num1 == 0 && den1 == 0) {
            // coincident segments
            return new ModelPoint(Double.NaN, Double.NaN, Double.NaN);
        } else if (den1 == 0) {
            //patallels no intersection
            return null;
        }

        double r = num1 / den1;
        double s = num2 / den2;
        double Px = Ax + r * (Bx - Ax);
        double Py = Ay + s * (By - Ay);
        return new ModelPoint(Px, Py, 0);
    }

    public double length() {
        return a.distance(b);
    }

    public double distance(ModelSegment other) {
        return Math.min(distance(other.a), distance(other.b));
    }

    public double distance(ModelPoint point) {
        return distanceFromLine(point.getX(), point.getY(), a.getX(), a.getY(), b.getX(), b.getY(), false);
    }

    public double lineDistance(ModelPoint point) {
        return distanceFromLine(point.getX(), point.getY(), a.getX(), a.getY(), b.getX(), b.getY(), false);
    }

    private static double distanceFromLine(double cx, double cy, double ax, double ay,
                                           double bx, double by, boolean distanceToLine) {
        double distanceSegment;

        double distanceLine;
        //
        // find the distance from the point (cx,cy) to the line
        // determined by the points (ax,ay) and (bx,by)
        //
        // distanceSegment = distance from the point to the line segment
        // distanceLine = distance from the point to the line (assuming
        //                                        infinite extent in both directions
        //
                /*
        
        Subject 1.02: How do I find the distance from a point to a line?
        
        
        Let the point be C (Cx,Cy) and the line be AB (Ax,Ay) to (Bx,By).
        Let P be the point of perpendicular projection of C on AB.  The parameter
        r, which indicates P's position along AB, is computed by the dot product 
        of AC and AB divided by the square of the length of AB:
        
        (1)    AC dot AB
        r = ---------  
        ||AB||^2
        
        r has the following meaning:
        
        r=0      P = A
        r=1      P = B
        r<0      P is on the backward extension of AB
        r>1      P is on the forward extension of AB
        0<r<1    P is interior to AB
        
        The length of a line segment in d dimensions, AB is computed by:
        
        L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 + ... + (Bd-Ad)^2)
        
        so in 2D:  
        
        L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )
        
        and the dot product of two vectors in d dimensions, U dot V is computed:
        
        D = (Ux * Vx) + (Uy * Vy) + ... + (Ud * Vd)
        
        so in 2D:  
        
        D = (Ux * Vx) + (Uy * Vy) 
        
        So (1) expands to:
        
        (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay)
        r = -------------------------------
        L^2
        
        The point P can then be found:
        
        Px = Ax + r(Bx-Ax)
        Py = Ay + r(By-Ay)
        
        And the distance from A to P = r*L.
        
        Use another parameter s to indicate the location along PC, with the 
        following meaning:
        s<0      C is left of AB
        s>0      C is right of AB
        s=0      C is on AB
        
        Compute s as follows:
        
        (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay)
        s = -----------------------------
        L^2
        
        
        Then the distance from C to P = |s|*L.
        
         */


        double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
        double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
        double r = r_numerator / r_denomenator;
//
        double px = ax + r * (bx - ax);
        double py = ay + r * (by - ay);
//    
        double s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay)) / r_denomenator;

        distanceLine = Math.abs(s) * Math.sqrt(r_denomenator);

//
// (xx,yy) is the point on the lineSegment closest to (cx,cy)
//
        double xx = px;
        double yy = py;

        if ((r >= 0) && (r <= 1)) {
            distanceSegment = distanceLine;
        } else {

            double dist1 = (cx - ax) * (cx - ax) + (cy - ay) * (cy - ay);
            double dist2 = (cx - bx) * (cx - bx) + (cy - by) * (cy - by);
            if (dist1 < dist2) {
                xx = ax;
                yy = ay;
                distanceSegment = Math.sqrt(dist1);
            } else {
                xx = bx;
                yy = by;
                distanceSegment = Math.sqrt(dist2);
            }


        }
        if (distanceToLine) {
            return distanceLine;
        } else {
            return distanceSegment;
        }
    }
}
