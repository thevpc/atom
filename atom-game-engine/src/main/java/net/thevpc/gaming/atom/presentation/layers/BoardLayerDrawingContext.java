package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.presentation.Scene;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/28/13
 * Time: 12:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class BoardLayerDrawingContext extends LayerDrawingContext {
    private Tile tile;

    public BoardLayerDrawingContext(Graphics2D graphics, Scene scene, AffineTransform sceneTransform, AffineTransform mapTransform) {
        super(graphics, scene, sceneTransform, mapTransform);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}
