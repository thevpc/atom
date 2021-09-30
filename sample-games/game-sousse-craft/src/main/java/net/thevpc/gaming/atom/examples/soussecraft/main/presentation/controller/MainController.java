/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.controller;

import java.awt.event.KeyEvent;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneController;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.presentation.SceneMouseEvent;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.MainEngine;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.MainEngineServer;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.GamePhase;

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
                        case SPACE: {
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
                    case LEFT: {
                        scene.getCamera().moveBy(new ViewPoint(-w, 0));
                        break;
                    }
                    case RIGHT: {
                        scene.getCamera().moveBy(new ViewPoint(w, 0));
                        break;
                    }
                    case UP: {
                        scene.getCamera().moveBy(new ViewPoint(0, -h));
                        break;
                    }
                    case DOWN: {
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
