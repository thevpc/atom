/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.server.presentation;

//import net.thevpc.gaming.atom.debug.layers.DebugLayer;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.BomberScene;
import net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.ScoreLayer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(
        id = "mainServer",
        sceneEngine = "mainServer",
        title = "Kombla - Server",
        tileWidth = 80,
        isometric = false
        ,cameraWidth = 0.5f
)
public class MainServerScene extends BomberScene {

    @OnInit
    public void onInstall(){
        this.addLayer(new ScoreLayer());
    }
}
