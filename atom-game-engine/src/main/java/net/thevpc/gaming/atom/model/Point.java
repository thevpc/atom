package net.thevpc.gaming.atom.model;

public class Point {
    public static RatioPoint ratio(float x, float y) {
        return new RatioPoint(x, y);
    }
    public static RatioPoint ratio(float x, float y,float z) {
        return new RatioPoint(x, y,z);
    }

    public static ModelPoint model(double x, double y) {
        return new ModelPoint(x, y);
    }
    public static ModelPoint model(double x, double y,double z) {
        return new ModelPoint(x, y,z);
    }

    public static ViewPoint view(int x, int y) {
        return new ViewPoint(x, y);
    }
    public static ViewPoint view(int x, int y,int z) {
        return new ViewPoint(x, y,z);
    }


}
