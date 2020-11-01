/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.shared.prensentation;

import net.vpc.gaming.atom.annotations.AtomSceneController;
import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.DefaultSceneController;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import net.vpc.games.atom.examples.kombla.main.shared.engine.AbstractMainEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneController(
        scene = "mainLocal,mainClient,mainServer"
)
public class MainController extends DefaultSceneController {


    public MainController() {
    }
    @Override
    public void keyPressed(SceneKeyEvent e) {
        keyPressedTask(e);
    }

    public void keyPressedTask(SceneKeyEvent e) {
        AbstractMainEngine scene = e.getScene().getSceneEngine();
        switch (e.getKeyCode()) {
            case SceneKeyEvent.VK_LEFT: {
                scene.move(Orientation.WEST);
                break;
            }
            case SceneKeyEvent.VK_RIGHT: {
                scene.move(Orientation.EAST);
                break;
            }
            case SceneKeyEvent.VK_UP: {
                scene.move(Orientation.NORTH);
                break;
            }
            case SceneKeyEvent.VK_DOWN: {
                scene.move(Orientation.SOUTH);
                break;
            }
            case SceneKeyEvent.VK_SPACE: {
                scene.releaseBomb();
                break;
            }
        }
    }



}
