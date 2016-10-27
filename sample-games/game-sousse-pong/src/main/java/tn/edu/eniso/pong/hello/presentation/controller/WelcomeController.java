/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.hello.presentation.controller;

import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.presentation.DefaultSceneController;
import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import tn.edu.eniso.pong.hello.model.WelcomeModel;
import tn.edu.eniso.pong.main.client.engine.MainEngineClient;
import tn.edu.eniso.pong.main.server.engine.MainEngineServer;
import tn.edu.eniso.pong.main.shared.model.AppRole;
import tn.edu.eniso.pong.main.shared.model.AppTransport;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeController extends DefaultSceneController {


    public WelcomeController() {
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        WelcomeModel model = e.getScene().getSceneEngine().getModel();
        GameEngine game = e.getScene().getGame().getGameEngine();
        switch (e.getKeyCode()) {
            case SceneKeyEvent.VK_SPACE: {
                switch (model.getRole()) {
                    case SERVER: {
                        game.setActiveSceneEngine(MainEngineServer.class);
                        break;
                    }
                    case CLIENT: {
                        game.setActiveSceneEngine(MainEngineClient.class);
                        break;
                    }
                }
                break;
            }
            case SceneKeyEvent.VK_UP: {
                model.setRole(AppRole.CLIENT);
                break;
            }
            case SceneKeyEvent.VK_DOWN: {
                model.setRole(AppRole.SERVER);
                break;
            }
            case SceneKeyEvent.VK_F1: {
                model.setTransport(AppTransport.TCP);
                break;
            }
            case SceneKeyEvent.VK_F2: {
                model.setTransport(AppTransport.UDP);
                break;
            }
            case SceneKeyEvent.VK_F3: {
                model.setTransport(AppTransport.MULTICAST);
                break;
            }
            case SceneKeyEvent.VK_F4: {
                model.setTransport(AppTransport.RMI);
                break;
            }
            case SceneKeyEvent.VK_F5: {
                model.setTransport(AppTransport.CORBA);
                break;
            }
        }
    }
}
