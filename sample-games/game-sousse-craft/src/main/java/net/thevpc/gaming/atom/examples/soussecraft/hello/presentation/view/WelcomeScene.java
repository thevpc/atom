/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.hello.presentation.view;

import java.awt.Color;
import java.awt.Font;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.model.Orientation;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Alignment;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.components.SButton;
import net.thevpc.gaming.atom.presentation.components.SImage;
import net.thevpc.gaming.atom.presentation.components.SceneComponentState;
import net.thevpc.gaming.atom.presentation.components.TextStyle;
import net.thevpc.gaming.atom.presentation.layers.FillScreenGradientLayer;
import net.thevpc.gaming.atom.presentation.layers.Layer;
import net.thevpc.gaming.atom.examples.soussecraft.hello.model.AppRole;
import net.thevpc.gaming.atom.examples.soussecraft.hello.presentation.controller.WelcomeController;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(id = "welcome")
public class WelcomeScene extends DefaultScene {

    private SButton join = new SButton("Join Game");
    private SButton host = new SButton("Host Game");
    private SButton start = new SButton("Press any key to start...");
    private SImage image;

    public WelcomeScene() {
        super(600, 400);
        addLayer(new FillScreenGradientLayer(new Color(3, 81, 126), Color.DARK_GRAY, Orientation.NORTH));
        addController(new WelcomeController());

        Font verdana24B = new Font("verdana", Font.BOLD, 24);

        join.setBounds(new ViewBox(200, 180, 200, 30));
        join.setTextStyle(SceneComponentState.DEFAULT, new TextStyle("join")
                .setFont(verdana24B)
                .setInsetX(40)
                .setInsetY(20)
                .setForeColor(Color.WHITE)
                .setBackgroundColor(Color.BLUE.darker())
                .setBackgroundColor2(Color.CYAN)
                .setAlignment(Alignment.CENTER)
        );

        host.setBounds(new ViewBox(200, 220, 200, 30));
        host.setTextStyle(SceneComponentState.DEFAULT, new TextStyle("host")
        .setInsetX(40)
        .setInsetY(20)
        .setFont(verdana24B)
        .setForeColor(Color.WHITE)
        .setBackgroundColor(Color.BLUE.darker())
        .setBackgroundColor2(Color.CYAN)
        .setAlignment(Alignment.CENTER)
        );

        ViewBox vp = getCamera().getViewPort();
        ViewBox r2 = new ViewBox(vp.getX(), vp.getY() + vp.getHeight() * 2 / 3, vp.getWidth(), vp.getHeight() / 3);
        start.setBounds(r2);
        start.setTextStyle(SceneComponentState.DEFAULT, new TextStyle("start")
        .setAlignment(Alignment.CENTER)
        .setForeColor(Color.WHITE)
        .setFont(verdana24B)
        .setBlinkPeriod(10)
        );
        //DisplayUtils.drawCenteredString(graphics, "Press any key to start", r2);

        image = new SImage("sousse-craft-logo","images/sousse-craft-logo.png",getClass());
        image.setBounds(new ViewBox(0, 0, getCamera().getViewBounds().getWidth(), 200));
        image.setLocation(new ViewPoint(vp.getX(), vp.getY()));
        image.setAlignment(Alignment.CENTER);

        addComponent(join, null, Layer.BACKGROUND_LAYER);
        addComponent(host, null, Layer.BACKGROUND_LAYER);
        addComponent(start, null, Layer.BACKGROUND_LAYER);
        addComponent(image, null, Layer.BACKGROUND_LAYER);
    }

    @Override
    protected void update() {
        host.setSelected(getRole() == AppRole.HOST_GAME);
        join.setSelected(getRole() == AppRole.JOIN_GAME);
    }

    public AppRole getRole() {
        return getGameEngine().getProperties().getProperty(AppRole.class.getName());
    }

}
