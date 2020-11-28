/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.presentation.controller;

import net.thevpc.gaming.atom.presentation.DefaultSceneController;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.examples.pong.main.shared.engine.AbstractMainEngine;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainController extends DefaultSceneController {
    private int player;
    public MainController(int player) {
        this.player=player;
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
                scene.releaseBall(e.getPlayerId());
                break;
            }
        }
    }
}
