/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.view.layers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import net.vpc.gaming.atom.extension.strategy.players.StrategyGamePlayer;
import net.vpc.gaming.atom.extension.strategy.resources.Resource;
import net.vpc.gaming.atom.extension.strategy.resources.ResourceRepository;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.layers.DefaultLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import tn.edu.eniso.soussecraft.main.presentation.view.resources.MyResourceView;
import tn.edu.eniso.soussecraft.main.model.resources.Minerals;
import tn.edu.eniso.soussecraft.main.model.resources.Woods;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ScoreLayer extends DefaultLayer {

    private Font verdana10B = new Font("verdana", Font.BOLD, 10);
    private Map<Class<? extends Resource>, ScoreViewInfo> viewInfo = new HashMap<Class<? extends Resource>, ScoreViewInfo>();

    public ScoreLayer() {
        setLayer(SCREEN_FRONTEND_LAYER);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene view = context.getScene();

        Scene _view = getScene();
        StrategyGamePlayer player = (StrategyGamePlayer) _view.getControlPlayer();
        if (player == null) {
            graphics.drawString("Waiting for Game start...", 100, 100);
            return;
        }
        ResourceRepository dd = player.getResources();
        int x = 0;
        Class<? extends Resource>[] resourceTypes = dd.getResources();
        int rw = 75;
        for (int i = 0; i < resourceTypes.length; i++) {
            Class<? extends Resource> r = resourceTypes[i];
            int resourceValue = dd.getResource(r);
            int maxResourceValue = dd.getMaxResource(r);
            int mwidth = _view.getCamera().getViewPort().getWidth();
            int x0 = mwidth - 5 - rw * resourceTypes.length + x * rw;
            int y0 = 3;
            int w = 70;
            int h = 30;
            ScoreViewInfo nfo = getResourceImage(r, view);
            graphics.drawImage(nfo.getIcon(), x0, y0, null);

            if (maxResourceValue > 0) {
                graphics.setColor(nfo.color2);
//                g2d.setColor(Color.WHITE);
                graphics.fillRect(x0, y0 + 24, w, 6);
                graphics.setColor(nfo.color1);
                graphics.fillRect(x0, y0 + 24, (int) (w * (((double) resourceValue) / maxResourceValue)), 6);
            } else {
                graphics.setColor(nfo.color1);
                graphics.fillRect(x0, y0 + 24, w, 6);
            }
            graphics.setColor(nfo.color3);
            graphics.drawRect(x0, y0, w, h);
            graphics.setColor(Color.WHITE);
            graphics.setFont(verdana10B);
            if (maxResourceValue > 0) {
                graphics.drawString(String.valueOf(resourceValue), x0 + 22, y0 + 10);
                graphics.drawString(String.valueOf(maxResourceValue), x0 + 22, y0 + 20);
            } else {
                graphics.drawString(resourceValue + "", x0 + 22, y0 + 10);
            }
            x++;

        }
    }

    private static class ScoreViewInfo {

        private Image icon;
        private Color color1;
        private Color color2;
        private Color color3;

        public ScoreViewInfo(Image icon, Color color1, Color color2, Color color3) {
            this.icon = icon;
            this.color1 = color1;
            this.color2 = color2;
            this.color3 = color3;
        }

        public Color getColor1() {
            return color1;
        }

        public Color getColor2() {
            return color2;
        }

        public Color getColor3() {
            return color3;
        }

        public Image getIcon() {
            return icon;
        }
    }

    public ScoreViewInfo getResourceImage(Class<? extends Resource> st, Scene view) {
        ScoreViewInfo ii = viewInfo.get(st);
        if (ii == null) {
            Resource r = null;
            Color color1 = null;
            Color color2 = null;
            Color color3 = null;
            if (st.equals(Minerals.class)) {
                r = new Minerals(1, 1, 1);
                color1 = Color.BLUE;
                color2 = Color.CYAN;
                color3 = Color.BLUE.darker();
            } else if (st.equals(Woods.class)) {
                r = new Woods(1, 1, 1);
                color1 = Color.GREEN;
                color2 = Color.LIGHT_GRAY;
                color3 = Color.GREEN.darker();
            }
            MyResourceView v = (MyResourceView) view.getSpriteView(r);

            ii = new ScoreViewInfo(v.getImage(r, view, 0,0).getScaledInstance(20, 20, Image.SCALE_SMOOTH), color1, color2, color3);
            viewInfo.put(st, ii);
        }
        return ii;
    }
}
