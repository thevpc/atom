/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.fogofwar;

import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;
import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
class FogOfWarLayer extends FlatBoardLayer {

    private Composite[] compositeArray;

    public FogOfWarLayer() {
        this(SKY_LAYER);
    }

    public FogOfWarLayer(int zIndex) {
        setLayer(zIndex);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene view = context.getScene();

        Scene sv = view;
        if (compositeArray == null || compositeArray.length != FogOfWarSceneEngineExtension.FOG_MAX) {
            compositeArray = new Composite[FogOfWarSceneEngineExtension.FOG_MAX - 1];
            for (int i = 0; i < compositeArray.length; i++) {
                compositeArray[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (i + 1F) / compositeArray.length);
            }
        }
//        HashMap<Integer, Area> fogs = new HashMap<>();
        final Collection<Tile> visibleCells = view.findDisplayTiles();
        Composite composite = graphics.getComposite();
        graphics.setColor(Color.DARK_GRAY);
        FogOfWarSceneEngineExtension e = view.getSceneEngine().getExtension(FogOfWarSceneEngineExtension.class);
        for (Player player : view.getControlPlayers()) {
            FogOfWarSceneEngineExtension.FogOfWarLayerInfo playerInfo = e.getPlayerInfo(player);
            for (Tile tile : visibleCells) {
                final ViewBox r = sv.toViewBox(tile.getBounds());
                int col = tile.getColumn();
                int row = tile.getRow();
                int v = playerInfo.getTileFog(col, row);
                if (v > 0) {
//                    Area a = new Area(r);
//                    final Area a0 = fogs.get(v - 1);
//                    if (a0 == null) {
//                        fogs.put(v - 1, a);
//                    } else {
//                        a0.add(a);
//                    }
                    graphics.setComposite(compositeArray[v - 1]);
                    graphics.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                }
            }
        }
//        for (Entry<Integer, Area> entry : fogs.entrySet()) {
//            graphics.setComposite(compositeArray[entry.getKey()]);
//            graphics.fill(entry.getValue());
//        }
        graphics.setComposite(composite);
    }
}
