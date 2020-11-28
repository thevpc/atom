/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.presentation.SequenceGenerator;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.Scene;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpritesSelectionLayer extends FlatBoardLayer {

    private BasicStroke[] strokes = {
            new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f}, 0f),
            new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f}, 2f),
            new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f}, 4f),
            new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f}, 6f)
    };
    private Color color;
    private SequenceGenerator strokeAnimator = SequenceGenerator.createUnsignedSequence(5, 5, 5, 5);

    public SpritesSelectionLayer() {
        this(Color.GREEN);
    }

    public SpritesSelectionLayer(Color color) {
        this(BACKGROUND_DECORATION_LAYER, color);
    }

    public SpritesSelectionLayer(int layer, Color color) {
        this.color = color;
        setLayer(layer);
    }

    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();

        for (Player player : scene.getControlPlayers()) {
            for (Sprite sprite : player.getSelection().getSelectedSprites()) {
                if (scene.isWithinScreen(sprite)) {
                    ViewBox xyRect = scene.toViewBox(sprite);
//                    ViewBox zRect = new ViewBox(scene.getSpriteView(sprite).getShape(sprite, scene).getBounds());
//                    ViewBox selectionRect = new ViewBox(
//                            (int) (xyRect.getCenterX() - zRect.getWidth() / 2),
//                            (int) (xyRect.getCenterY() - zRect.getHeight() / 2),
//                            zRect.getWidth(),
//                            zRect.getHeight()
//                    );
                    ViewBox selectionRect = new ViewBox(
                            (int) (xyRect.getCenterX() - xyRect.getWidth() / 2),
                            (int) (xyRect.getCenterY() - xyRect.getHeight() / 2),
                            xyRect.getWidth(),
                            xyRect.getHeight()
                    );


                    AffineTransform transform = graphics.getTransform();
                    Stroke stroke = graphics.getStroke();
                    int cycle = strokeAnimator.next(scene.getFrame());

                    graphics.setStroke(strokes[cycle - 1]);

                    graphics.setColor(color);
                    graphics.drawOval(selectionRect.getX(), selectionRect.getY(), selectionRect.getWidth(), selectionRect.getHeight());
                    graphics.setStroke(stroke);
                    graphics.setTransform(transform);
                }
            }
        }
    }
}
