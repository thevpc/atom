/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.client.presentation;

//import net.thevpc.gaming.atom.debug.layers.DebugLayer;

import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.BomberScene;
import net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation.ScoreLayer;
import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.examples.kombla.main.client.engine.MainClientEngine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(
        id = "mainClient",
        sceneEngine = "mainClient",
        title = "Kombla - Client",
        tileWidth = 80,
        isometric = false
        , cameraWidth = 0.5f
)
public class MainClientScene extends BomberScene {

    @Inject
    SceneEngine engine;

    public MainClientScene() {
    }

    @OnInit
    private void onInstall() {
        this.addLayer(new ScoreLayer());
        getCamera().followSprite(()->engine.findSpriteByPlayer(Sprite.class,((MainClientEngine)engine).getCurrentPlayerId()));
        engine.addPropertyChangeListener("modelChanged", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateControl();
            }
        });
    }

    protected void updateControl() {
        MainClientEngine sceneEngine = getSceneEngine();
        setControlPlayer(sceneEngine.getCurrentPlayerId());
    }

}
