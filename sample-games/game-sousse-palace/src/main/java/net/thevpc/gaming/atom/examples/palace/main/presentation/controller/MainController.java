/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.palace.main.presentation.controller;

import java.awt.event.KeyEvent;

import net.thevpc.gaming.atom.presentation.SceneController;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.examples.palace.main.engine.MainEngine;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainController implements SceneController {

    public MainController() {
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        process(e);
    }

    public void keyReleased(SceneKeyEvent e) {
        process(e);
    }

    public void process(SceneKeyEvent e) {
        MainEngine scene = e.getScene().getSceneEngine();
        boolean left = false;
        boolean right = false;
        boolean jump = false;
        for (int kc : e.getKeyCodes()) {
            switch (kc) {
                case KeyEvent.VK_LEFT: {
                    left = true;
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    right = true;
                    break;
                }
                case KeyEvent.VK_SPACE: {
                    jump = true;
                    break;
                }
            }
        }
        if (left && jump) {
            scene.jumpLeft();
        }else if (right && jump) {
            scene.jumpRight();
        } else if (left) {
            scene.moveLeft();
        } else if (right) {
            scene.moveRight();
        } else if(jump){
            scene.jumpUp();
        }else{
//            scene.hold();
        }
    }

}
