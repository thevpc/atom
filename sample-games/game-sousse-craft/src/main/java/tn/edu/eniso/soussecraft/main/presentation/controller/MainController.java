/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.main.presentation.controller;

import java.awt.event.KeyEvent;

import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneController;
import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import net.vpc.gaming.atom.presentation.SceneMouseEvent;
import tn.edu.eniso.soussecraft.main.business.MainEngine;
import tn.edu.eniso.soussecraft.main.business.MainEngineServer;
import tn.edu.eniso.soussecraft.main.model.GamePhase;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainController implements SceneController {

    public MainController() {
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        Scene scene = e.getScene();
        MainEngine engine = scene.getSceneEngine();
        GamePhase phase = engine.getGamePhase();
        switch (phase) {
            case CONNECTING: {
                break;
            }
            case STARING: {
                if (engine instanceof MainEngineServer) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_SPACE: {
                            ((MainEngineServer) engine).startGame();
                            break;
                        }
                    }
                }
                break;
            }
            case STARTED: {
                int w = scene.getTileSize().getWidth();
                int h = scene.getTileSize().getHeight();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: {
                        scene.getCamera().moveBy(new ViewPoint(-w, 0));
                        break;
                    }
                    case KeyEvent.VK_RIGHT: {
                        scene.getCamera().moveBy(new ViewPoint(w, 0));
                        break;
                    }
                    case KeyEvent.VK_UP: {
                        scene.getCamera().moveBy(new ViewPoint(0, -h));
                        break;
                    }
                    case KeyEvent.VK_DOWN: {
                        scene.getCamera().moveBy(new ViewPoint(0, h));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(SceneMouseEvent e) {
        MainEngine mainScene = e.getScene().getSceneEngine();
        GamePhase phase = mainScene.getGamePhase();
        if (phase == GamePhase.STARTED) {
            Object object = e.getObject();
            if (e.isLeftMouseButton()) {
                if (object instanceof Sprite) {
                    Sprite sprite = (Sprite) object;
                    mainScene.selectSprite(e.getPlayerId(), e.getPoint(), sprite == null ? null : sprite.getId(), e.isControlDown());
                } else if (object instanceof Tile) {
                    mainScene.selectTile(e.getPlayerId(), e.getPoint(), ((Tile) object).getId());
                }
            } else if (e.isRightMouseButton()) {
                if (object instanceof Sprite) {
                    Sprite sprite = (Sprite) object;
                    mainScene.moveSelectionToSprite(e.getPlayerId(), e.getPoint(), sprite == null ? null : sprite.getId());
                } else if (object instanceof Tile) {
                    mainScene.moveSelectionToTile(e.getPlayerId(), e.getPoint(), ((Tile) object).getId());
                }
            }
        }
    }
}
