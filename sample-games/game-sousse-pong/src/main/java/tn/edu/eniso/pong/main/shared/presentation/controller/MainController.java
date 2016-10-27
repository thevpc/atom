/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.presentation.controller;

import net.vpc.gaming.atom.presentation.DefaultSceneController;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import tn.edu.eniso.pong.main.shared.engine.AbstractMainEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainController extends DefaultSceneController {
    private int player;
    public MainController(int player) {
        this.player=player;
    }

    @Override
    public void sceneStarted(Scene scene) {
        scene.addControlPlayer(player);
    }

    
    @Override
    public void keyPressed(SceneKeyEvent e) {
        AbstractMainEngine scene = e.getScene().getSceneEngine();

        switch (e.getKeyCode()) {
            case SceneKeyEvent.VK_LEFT: {
                scene.moveLeft(e.getPlayerId());
                break;
            }
            case SceneKeyEvent.VK_RIGHT: {
                scene.moveRight(e.getPlayerId());
                break;
            }
            case SceneKeyEvent.VK_SPACE: {
                scene.relaseBall(e.getPlayerId());
                break;
            }
        }
    }
}
