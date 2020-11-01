/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.view.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Collection;
import java.util.Collections;

import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.ComponentDrawingContext;
import net.vpc.gaming.atom.presentation.DefaultSceneComponent;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteView;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import tn.edu.eniso.soussecraft.main.presentation.view.etc.DashboardSpriteView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DetailsComponent extends DefaultSceneComponent {

    public DetailsComponent(ViewDimension size) {
        setSize(size);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        if(!isVisible()){
            return;
        }
        Graphics2D g = context.getGraphics();
        ViewPoint p0 = getLocation();
        ViewDimension s = getSize();
        Scene view = getScene();
        Player player = view.getControlPlayer();
        Collection<Sprite> sprites = player==null? Collections.EMPTY_LIST: player.getSelection().getSelectedSprites();
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.5f, new float[]{3f, 15f}, 50f));
        g.setColor(Color.BLACK);
        g.drawRoundRect(p0.getX(), p0.getY(), s.getWidth() - 1, s.getHeight() - 1, 10, 10);
        g.setStroke(stroke);
        
//        g.setColor(Color.WHITE);
//        g.fillRoundRect(p0.x, p0.y, s.width - 1, s.height - 1, 10, 10);
//        g.setColor(Color.BLACK);

        switch (sprites.size()) {
            case 0: {
                g.drawString("No selection", p0.getX() + 20, p0.getY() + 60);
                break;
            }
            case 1: {
                for (Sprite sp : sprites) {
                    if (sp != null) {
                        SpriteView vv = view.getSpriteView(sp);
                        if (vv instanceof DashboardSpriteView) {
                            DashboardSpriteView v = (DashboardSpriteView) vv;
                            v.drawDetails(sp, g, view, p0, s);
                        }
                    }
                }
                break;
            }
            default: {
                int ww = 40;
                int hh = 40;
                int maxCol = 6;
                int maxRow = 6;
                int row = 0;
                int col = 0;
                ViewDimension dim = new ViewDimension(ww, hh);
                for (Sprite sp : sprites) {
                    SpriteView vv = view.getSpriteView(sp);
                    if (vv instanceof DashboardSpriteView) {
                        DashboardSpriteView v = (DashboardSpriteView) vv;
                        v.drawDetails(sp, g, view, new ViewPoint(p0.getX()+col * ww, p0.getY()+row * hh), dim);
                        col++;
                        if (col == maxCol) {
                            col = 0;
                            row++;
                        }
                        if (row > 6) {
                            break;
                        }
                    }
                }
                break;
            }
        }
    }
}
