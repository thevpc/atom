/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.hello.presentation.view;

import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.presentation.DefaultScene;
import net.vpc.gaming.atom.presentation.layers.FillScreenGradientLayer;
import tn.edu.eniso.pong.hello.presentation.controller.WelcomeController;

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
        addSceneController(new WelcomeController());
    }

}
