/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.hello.presentation.view;

import net.thevpc.gaming.atom.examples.pong.main.shared.model.AppRole;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.AppTransport;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SequenceGenerator;
import net.thevpc.gaming.atom.presentation.layers.DefaultLayer;
import net.thevpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.thevpc.gaming.atom.util.AtomUtils;
import net.thevpc.gaming.atom.examples.pong.hello.model.WelcomeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeLayer extends DefaultLayer {

    private Font verdana20B = new Font("verdana", Font.BOLD, 20);
    private Font verdana10B = new Font("verdana", Font.BOLD, 10);
    private SequenceGenerator blinkSequence = SequenceGenerator.createUnsignedSequence(10, 10);
    private Image logo;
    //private CycleGenerator colorCycle = CycleGenerator.createCycle(10);

    public WelcomeLayer() {
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();
        int screenWidth = context.getScene().getCamera().getViewBounds().getWidth();

        WelcomeModel model = scene.getSceneEngine().getModel();
        AppRole r = model.getRole();
        if (logo == null) {
            logo = AtomUtils.createImage("/sousse-pong-logo.png");
        }
        ViewPoint p = AtomUtils.getCenteredPosition(logo.getWidth(null), logo.getHeight(null), new ViewBox(0, 0, screenWidth, 200));
        graphics.drawImage(logo, p.getX(), p.getY(), null);
//        drawLabel(graphics, "So Pong..."        , 5, 15, 70, 100, 400, 20, verdana50B, new Color(0,0,100+(int)(model.getFrame()%155)), null);

        drawLabel(graphics, "Client", 40, 20, 200, 180, 200, 30, verdana20B, Color.WHITE, Color.RED, r == AppRole.CLIENT, SwingConstants.CENTER);
        drawLabel(graphics, "Server", 40, 20, 200, 220, 200, 30, verdana20B, Color.WHITE, Color.RED, r == AppRole.SERVER, SwingConstants.CENTER);
        AppTransport tr = model.getTransport();

        int tw = 110;
        Color darkGreen = Color.GREEN.darker();
        drawLabel(graphics, "[F1] TCP", 5, 15, 10, 350, tw - 5, 20, verdana10B, Color.WHITE, darkGreen, tr == AppTransport.TCP, SwingConstants.CENTER);
        drawLabel(graphics, "[F2] UDP", 5, 15, 10 + 1 * tw, 350, tw - 5, 20, verdana10B, Color.WHITE, darkGreen, tr == AppTransport.UDP, SwingConstants.CENTER);
        drawLabel(graphics, "[F3] MULTICAST", 5, 15, 10 + 2 * tw, 350, tw - 5, 20, verdana10B, Color.WHITE, darkGreen, tr == AppTransport.MULTICAST, SwingConstants.CENTER);
        drawLabel(graphics, "[F4] RMI", 5, 15, 10 + 3 * tw, 350, tw - 5, 20, verdana10B, Color.WHITE, darkGreen, tr == AppTransport.RMI, SwingConstants.CENTER);
        drawLabel(graphics, "[F5] CORBA", 5, 15, 10 + 4 * tw, 350, tw - 5, 20, verdana10B, Color.WHITE, darkGreen, tr == AppTransport.CORBA, SwingConstants.CENTER);

        if (blinkSequence.next(scene.getFrame()) == 1) {
            String text = "PRESS SPACE";
            Graphics2D graphics2 = (Graphics2D) graphics.create();
            graphics2.setPaint(new GradientPaint(0, 0, Color.white, 0, 30, Color.YELLOW, true));
            graphics2.translate(190, 300);
            Font f = new Font("Serif", Font.ITALIC, 30);
            GlyphVector gv = f.createGlyphVector(graphics2.getFontRenderContext(), text);

            Shape shape = gv.getOutline();
            graphics2.setStroke(new BasicStroke(1.0f));
            graphics2.draw(shape);
//            graphics2.setStroke(oldStroke);
//            drawLabel(graphics, text, 5, 15, 0, 300, screenWidth, 20, verdana20B, Color.YELLOW, null, false, SwingConstants.CENTER);
        }
    }

    private void drawLabel(
            Graphics2D graphics,
            String value,
            int a, int b,
            int x, int y, int width, int height,
            Font font, Color fore, Color bkg, boolean selected, int halign) {

        if (font != null) {
            graphics.setFont(font);
        }

        if (bkg != null && selected) {
            int arc = 10;
            Paint oldPaint = graphics.getPaint();
            GradientPaint gp = new GradientPaint(x, y, Color.YELLOW, x, y + height, bkg, true);
            graphics.setPaint(gp);
//            g2d.setColor(bkg);
            graphics.fillRoundRect(x, y, width, height, arc, arc);
            graphics.setPaint(oldPaint);

            Stroke oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.setColor(bkg.darker().darker());
//            g2d.setColor(fore);
//            g2d.draw3DRect(x, y, width, height, true);
            graphics.drawRoundRect(x, y, width, height, arc, arc);
            graphics.setStroke(oldStroke);
        }
        if (fore != null) {
            graphics.setColor(fore);
        }
        if (halign == SwingConstants.CENTER) {
            AtomUtils.drawCenteredString(graphics, value, new ViewBox(x, y, width, height));
        } else {
            graphics.drawString(value, x + a, y + b);
        }
    }
}
