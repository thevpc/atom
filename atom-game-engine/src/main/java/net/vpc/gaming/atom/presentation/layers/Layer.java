/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.presentation.Scene;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface Layer {

    public static final int SCREEN_BACKGROUND_LAYER = -1000;
    public static final int SCREEN_DECORATION_LAYER = 1000;
    public static final int SCREEN_DASHBOARD_LAYER = 2000;
    public static final int SCREEN_COMPONENT_LAYER = 3000;
    public static final int SCREEN_FRONTEND_LAYER = 100000;

    public static final int UNDERGROUND_LAYER = -1000;
    public static final int BACKGROUND_LAYER = 0;
    public static final int BACKGROUND_DECORATION_LAYER = 100;
    public static final int SPRITES_LAYER = 1000;
    public static final int BACKGROUND_OVERLAPPING_LAYER = 5000;
    public static final int SKY_LAYER = 100000;
//
//    public static final int UNDERGROUND_LAYER = -(2 << 28);
//    public static final int SCREEN_DECORATION_LAYER = -(2 << 25);
//    public static final int SPRITES_LAYER = 0;
//    public static final int SCREEN_FRONTEND_LAYER = (2 << 10);
//    public static final int SKY_LAYER = (2 << 20);
//    public static final int SCREEN_DASHBOARD_LAYER = (2 << 25);
//    public static final int FOREGROUND_LAYER = (2 << 28);

    public void draw(LayerDrawingContext context);

    public Scene getScene();

    public void setScene(Scene view);

    public void nextFrame();

    public int getLayer();

    public void setLayer(int zIndex);

    public boolean isEnabled();

    public void setEnabled(boolean enabled);
}
