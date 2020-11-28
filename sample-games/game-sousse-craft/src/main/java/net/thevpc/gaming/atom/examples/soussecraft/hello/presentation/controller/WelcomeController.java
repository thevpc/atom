/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.hello.presentation.controller;

import java.awt.event.KeyEvent;

import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneController;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.examples.soussecraft.hello.model.AppRole;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.MainEngineClient;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.MainEngineServer;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeController implements SceneController {

    public WelcomeController() {
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        Scene scene = e.getScene();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN: {
                setRole(AppRole.HOST_GAME,scene.getGameEngine());
                break;
            }
            case KeyEvent.VK_UP: {
                setRole(AppRole.JOIN_GAME,scene.getGameEngine());
                break;
            }
            case KeyEvent.VK_SPACE: {
                GameEngine gameEngine = scene.getSceneEngine().getGameEngine();
                switch (getRole(gameEngine)) {
                    case HOST_GAME: {
                        gameEngine.setActiveSceneEngine(MainEngineServer.class);
                        break;
                    }
                    case JOIN_GAME: {
                        gameEngine.setActiveSceneEngine(MainEngineClient.class);
                        break;
                    }
                }
                break;
            }
        }
    }
    public void setRole(AppRole role,GameEngine ge) {
        ge.getProperties().setProperty(AppRole.class.getName(),role);
    }
    public AppRole getRole(GameEngine ge) {
        return ge.getProperties().getProperty(AppRole.class.getName());
    }
}
