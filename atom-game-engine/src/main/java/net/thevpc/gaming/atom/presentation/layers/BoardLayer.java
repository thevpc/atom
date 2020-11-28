package net.thevpc.gaming.atom.presentation.layers;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/22/13
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BoardLayer extends Layer {
    public void initDrawTiles(BoardLayerDrawingContext context);

    public void drawTile(BoardLayerDrawingContext context);
}
