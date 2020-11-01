/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.local.presentation;

//import net.vpc.gaming.atom.debug.layers.DebugLayer;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.presentation.DefaultScene;
import net.vpc.gaming.atom.presentation.ImageGrid;
import net.vpc.gaming.atom.presentation.ImageMatrixProducer;
import net.vpc.games.atom.examples.kombla.main.shared.prensentation.BomberScene;
import net.vpc.games.atom.examples.kombla.main.shared.prensentation.ScoreLayer;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(
        id = "mainLocal",
        engine = "mainLocal",
        title = "Kombla",
        tileWidth = 100,
        isometric = false
        ,cameraWidth = 0.5f
)
public class MainLocalScene extends BomberScene {

    @OnInstall
    private void onInstall() {
        this.addLayer(new ScoreLayer());
    }
}
