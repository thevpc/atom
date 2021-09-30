package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.debug.DebugLayer;
import net.thevpc.gaming.atom.model.Orientation;

import java.awt.*;

public class Layers {
    public static FillBoardColorLayer fillBoard(Color c) {
        return new FillBoardColorLayer(c);
    }

    public static FillScreenColorLayer fillScreen(Color c) {
        return new FillScreenColorLayer(c);
    }

    public static FillScreenGradientLayer fillScreenGradient(Color color1, Color color2, Orientation direction) {
        return new FillScreenGradientLayer(color1, color2, direction);
    }

    public static FillScreenGradientLayer FillScreenGradientLayer(int layer, Color color1, Color color2, Orientation direction) {
        return new FillScreenGradientLayer(layer, color1, color2, direction);
    }

    public static FillScreenGradientLayer fillScreenGradient(int layer, Color color1, float x1, float y1, Color color2, float x2, float y2) {
        return new FillScreenGradientLayer(layer, color1, x1, y1, color2, x2, y2);
    }

    public static FillScreenGradientLayer fillScreenGradient(Color color1, float x1, float y1, Color color2, float x2, float y2) {
        return new FillScreenGradientLayer(color1, x1, y1, color2, x2, y2);
    }
    public static FillBoardGradientLayer fillBoardGradient(Color color1, Color color2, Orientation direction) {
        return new FillBoardGradientLayer(color1, color2, direction);
    }

    public static FillBoardGradientLayer FillBoardGradientLayer(int layer, Color color1, Color color2, Orientation direction) {
        return new FillBoardGradientLayer(layer, color1, color2, direction);
    }

    public static FillBoardGradientLayer fillBoardGradient(int layer, Color color1, float x1, float y1, Color color2, float x2, float y2) {
        return new FillBoardGradientLayer(layer, color1, x1, y1, color2, x2, y2);
    }

    public static FillBoardGradientLayer fillBoardGradient(Color color1, float x1, float y1, Color color2, float x2, float y2) {
        return new FillBoardGradientLayer(color1, x1, y1, color2, x2, y2);
    }
    public static BordersScrollLayer scroll() {
        return new BordersScrollLayer();
    }

    public static ImageBoardLayer fillBoardImage(String imageMap) {
        return new ImageBoardLayer(imageMap);
    }
    public static ImageBoardLayer fillBoardImage(int zIndex, String imageMap) {
        return new ImageBoardLayer(zIndex, imageMap);
    }
    public static ImageBoardLayer fillBoardImage(int zIndex, int rows, int cols, String imageMap) {
        return new ImageBoardLayer(zIndex, rows, cols, imageMap);
    }
    public static ImageBoardLayer fillBoardImage(int zIndex, boolean mapAligned, Class baseClass, int rows, int cols, String imageMap) {
        return new ImageBoardLayer(zIndex, mapAligned, baseClass, rows, cols, imageMap);
    }
    public static ImageBoardLayer fillBoardImage(int zIndex, boolean mapAligned, String... images) {
        return new ImageBoardLayer(zIndex, mapAligned, images);
    }
    public static ImageBoardLayer fillBoardImage(int zIndex, boolean mapAligned, Image... images) {
        return new ImageBoardLayer(zIndex, mapAligned, images);
    }


    public static ImageScreenLayer fillScreenImage(String imageMap) {
        return new ImageScreenLayer(imageMap);
    }
    public static ImageScreenLayer fillScreenImage(int zIndex, String imageMap) {
        return new ImageScreenLayer(zIndex, imageMap);
    }
    public static ImageScreenLayer fillScreenImage(int zIndex, int rows, int cols, String imageMap) {
        return new ImageScreenLayer(zIndex, rows, cols, imageMap);
    }
    public static ImageScreenLayer fillScreenImage(int zIndex, boolean mapAligned, Class baseClass, int rows, int cols, String imageMap) {
        return new ImageScreenLayer(zIndex, mapAligned, baseClass, rows, cols, imageMap);
    }
    public static ImageScreenLayer fillScreenImage(int zIndex, boolean mapAligned, String... images) {
        return new ImageScreenLayer(zIndex, mapAligned, images);
    }
    public static ImageScreenLayer fillScreenImage(int zIndex, boolean mapAligned, Image... images) {
        return new ImageScreenLayer(zIndex, mapAligned, images);
    }

    public static DebugLayer debug() {
        return debug(true);
    }

    public static DebugLayer debug(boolean enabled) {
        return new DebugLayer(enabled);
    }

}
