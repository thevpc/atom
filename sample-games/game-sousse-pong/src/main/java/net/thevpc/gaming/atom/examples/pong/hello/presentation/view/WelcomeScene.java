/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.hello.presentation.view;

import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.layers.FillScreenGradientLayer;
import net.thevpc.gaming.atom.examples.pong.hello.presentation.controller.WelcomeController;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeScene extends DefaultScene {

    public WelcomeScene() {
        super("WelcomeScene",new ViewDimension(600, 400));
        setTitle("ENISO :: Pong Welcome");
        addLayer(new FillScreenGradientLayer(Color.DARK_GRAY, 0, 0, Color.BLACK, 0, 1));
        addLayer(new WelcomeLayer());
        addController(new WelcomeController());
    }

}
