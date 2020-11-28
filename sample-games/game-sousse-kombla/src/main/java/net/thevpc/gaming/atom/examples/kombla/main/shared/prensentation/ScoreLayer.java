package net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation;

import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.layers.DefaultLayer;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;

import java.awt.*;

public class ScoreLayer extends DefaultLayer{
    Font arial = new Font("Arial", Font.BOLD, 20);
    public ScoreLayer() {
        setLayer(SCREEN_DASHBOARD_LAYER);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        int index=0;
        for (Sprite sprite : context.getSceneEngine().getSprites()) {
            if("Person".equals(sprite.getKind())){
                int pid = sprite.getPlayerId();
                Player p = context.getSceneEngine().getPlayer(pid);
                int life = sprite.getLife();
//                System.out.println(p.getName()+" ("+pid+") "+life);
                Graphics2D g = context.getGraphics();
                g.setFont(arial);

                g.setColor(Color.LIGHT_GRAY);
                g.drawString(p.getName()+" ("+pid+")",350,30+25*index);
                g.setColor(Color.BLACK);
                g.drawString(p.getName()+" ("+pid+")",350+2,30+25*index+2);

                g.setColor(Color.DARK_GRAY);
                g.drawString(""+life,440,30+25*index);
                g.setColor(Color.YELLOW);
                g.drawString(""+life,440+2,30+25*index+2);
                index++;
            }
        }
    }
}
