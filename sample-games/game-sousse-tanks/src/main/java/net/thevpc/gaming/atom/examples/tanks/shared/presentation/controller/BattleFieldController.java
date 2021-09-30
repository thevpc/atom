/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.tanks.shared.presentation.controller;

import java.awt.event.KeyEvent;

import net.thevpc.gaming.atom.examples.tanks.server.business.ServerBattleFieldEngine;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.presentation.DefaultSceneController;

/**
 *
 * @author Taha Ben Salah
 */
public class BattleFieldController extends DefaultSceneController {

    @Override
    public void keyPressed(SceneKeyEvent e) {
        ServerBattleFieldEngine business = e.getScene().getSceneEngine();
        switch (e.getKeyCode()) {
            case RIGHT: {
                business.rotateRight(e.getPlayerId());
                break;
            }
            case LEFT: {
                business.rotateLeft(e.getPlayerId());
                break;
            }
            case SPACE: {
                business.fire(e.getPlayerId());
                break;
            }
        }
    }
}
