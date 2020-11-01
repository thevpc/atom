/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.server.presentation;

//import net.vpc.gaming.atom.debug.layers.DebugLayer;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.games.atom.examples.kombla.main.shared.prensentation.BomberScene;
import net.vpc.games.atom.examples.kombla.main.shared.prensentation.ScoreLayer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(
        id = "mainServer",
        engine = "mainServer",
        title = "Kombla - Server",
        tileWidth = 80,
        isometric = false
        ,cameraWidth = 0.5f
)
public class MainServerScene extends BomberScene {

    @OnInstall
    public void onInstall(){
        this.addLayer(new ScoreLayer());
    }
}
