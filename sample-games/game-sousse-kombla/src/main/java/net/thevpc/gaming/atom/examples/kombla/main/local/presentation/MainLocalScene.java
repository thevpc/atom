/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.local.presentation;

//import net.thevpc.gaming.atom.debug.layers.DebugLayer;

import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.BomberScene;
import net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.ScoreLayer;
import net.thevpc.gaming.atom.annotations.AtomScene;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(
        id = "mainLocal",
        sceneEngine = "mainLocal",
        title = "Kombla",
        tileWidth = 100,
        isometric = false
        ,cameraWidth = 0.5f
)
public class MainLocalScene extends BomberScene {
    public MainLocalScene() {
    }

    @OnInit
    private void onInstall() {
        this.addLayer(new ScoreLayer());
    }
}
